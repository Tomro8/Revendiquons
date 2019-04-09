package com.example.revendiquons.db;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.example.revendiquons.WebService;
import com.example.revendiquons.db.repository.DBOperationCallback;
import com.example.revendiquons.db.repository.PropositionRepository;
import com.example.revendiquons.db.repository.VoteRepository;
import com.example.revendiquons.db.dao.PropositionDao;
import com.example.revendiquons.db.dao.UserDao;
import com.example.revendiquons.db.dao.VoteDao;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.entity.User;
import com.example.revendiquons.db.entity.Vote;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                    populatePropEntity(context);
                }

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    Log.i("db", "Db is being opened");
                    populatePropEntity(context);
                    //Todo: animation chargement
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

    public void populateVoteEntity(Context context, int user_id) {
        Log.i("db", "Population votes from userid: " + user_id);
        WebService.getInstance(context).getUserVotes(Integer.toString(user_id), getUserVoteAPICallback(context));
    }

    private static void populatePropEntity(Context context) {
        WebService.getInstance(context.getApplicationContext()).getPropositionAPICall(populatePropEntityCallback(context));
    }

    private static Response.Listener<String> getUserVoteAPICallback(final Context context) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "populate vote response from server: " + response);
                try {
                    //Convert string response to JSONObject
                    JSONObject json = new JSONObject(response);
                    Log.i("volley", "jsonObject: " + json);

                    if (json.has("error")) {
                        Log.i("volley", "Error from server: " + json.get("error"));
                    } else {
                        JSONArray jsonArray = json.getJSONArray("votes");
                        Log.i("volley", "json array: " + jsonArray);
                        for (int i=0; i<jsonArray.length(); i++) {
                            int id = jsonArray.getJSONObject(i).getInt("id");
                            int user_id = jsonArray.getJSONObject(i).getInt("id_user");
                            int proposition_id = jsonArray.getJSONObject(i).getInt("id_proposition");
                            int voteValue = jsonArray.getJSONObject(i).getInt("forOrAgainst");

                            Vote vote = new Vote(id, user_id, proposition_id, voteValue);
                            VoteRepository.getInstance((Application)context).insert(vote);
                            Log.i("db", "insert vote into DB: " + vote);
                        }
                    }
                } catch (JSONException e) {
                    Log.i("db", "ResponseListener populateVoteEntity: in Exception:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        };
    }

    private static Response.Listener<String> populatePropEntityCallback(final Context context) {
        return new Response.Listener<String>() {
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

                        //insert(context, new Proposition(id, user_id, title, description, positive, negative));
                        PropositionRepository.getInstance((Application)context).
                                insert(new Proposition(id, user_id, title, description, positive, negative), new DBOperationCallback() {
                                    @Override
                                    public void onOperationCompleted() {
                                        //Nothing to do;
                                    }
                                });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
