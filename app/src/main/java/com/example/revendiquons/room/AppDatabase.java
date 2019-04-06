package com.example.revendiquons.room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.revendiquons.RequestQueueSingleton;
import com.example.revendiquons.room.dao.PropositionDao;
import com.example.revendiquons.room.dao.UserDao;
import com.example.revendiquons.room.dao.VoteDao;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.room.entity.User;
import com.example.revendiquons.room.entity.Vote;
import com.example.revendiquons.utils.Server;

import org.json.JSONException;
import org.json.JSONObject;

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
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "user-database").build();

            RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            getAppDatabase(context).PropositionDao().insertAll();
                        }
                    });
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };
        }
        return INSTANCE;
    }

    void getPropositionAPI(final Context context) {
        String url = Server.address + "listProp.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response from server: " + response.toString());
                try {
                    //Convert string response to JSONObject
                    JSONObject json = new JSONObject(response);
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

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
