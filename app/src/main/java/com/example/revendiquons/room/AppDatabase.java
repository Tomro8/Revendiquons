package com.example.revendiquons.room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.Activities.ChannelActivity;
import com.example.revendiquons.Activities.RegisterActivity;
import com.example.revendiquons.Activities.testActivity;
import com.example.revendiquons.RequestQueueSingleton;
import com.example.revendiquons.room.dao.PropositionDao;
import com.example.revendiquons.room.dao.UserDao;
import com.example.revendiquons.room.dao.VoteDao;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.room.entity.User;
import com.example.revendiquons.room.entity.Vote;
import com.example.revendiquons.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {User.class, Proposition.class, Vote.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract UserDao UserDao();
    public abstract PropositionDao PropositionDao();
    public abstract VoteDao voteDao();

    public static AppDatabase getAppDatabase(final Context context) {
        Log.i("db", "getAppdatabase call");
        if (INSTANCE == null) {

            RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    Log.i("db", "Db is being created");
                    AppDatabase.getPropositionAPI(context);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    Log.i("db", "Db is being opened");
                    AppDatabase.getPropositionAPI(context);
                }
            };

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "user-database").addCallback(rdc).build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    private static void insert(Context ctx, Proposition prop) {
        PropositionDao propDao = AppDatabase.getAppDatabase(ctx).PropositionDao();
        new insertAsyncTask(propDao).execute(prop);
    }

    private static class insertAsyncTask extends AsyncTask<Proposition, Void, Void> {

        private PropositionDao propDao;

        insertAsyncTask(PropositionDao propDao) {
            this.propDao = propDao;
        }

        @Override
        protected Void doInBackground(Proposition... propositions) {
            Log.i("db", "adding prop to db: " + propositions[0]);
            propDao.insertAll(propositions);
            return null;
        }
    }

    private static void getPropositionAPI(final Context context) {
        String url = Server.address + "listeProp.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response from server: " + response);
                try {
                    //Convert string response to JSONObject
                    JSONArray json = new JSONArray(response);
                    Log.i("volley", "json: " + json);

                    for (int i=0; i<json.length(); i++) {
                        int id = json.getJSONObject(i).getInt("id");
                        int user_id = json.getJSONObject(i).getInt("id_user");
                        String title = json.getJSONObject(i).getString("titre");
                        String description = json.getJSONObject(i).getString("description");
                        int positive = json.getJSONObject(i).getInt("positive");
                        int negative = json.getJSONObject(i).getInt("negative");

                        insert(context, new Proposition(id, user_id, title, description, positive, negative));
                    }

                    //Todo: format du JSON est chelou

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

        //Add request to queue
        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }
}
