package com.example.revendiquons.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.R;
import com.example.revendiquons.RequestQueueSingleton;
import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.dao.PropositionDao;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.repository.PropositionRepository;
import com.example.revendiquons.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class testActivity extends AppCompatActivity {
    Button insertBtn;
    Button deleteBtn;
    Button readBtn;
    PropositionRepository rp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_db);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //rp = new PropositionRepository(this.getApplication());
        //rp.insert(new Proposition(0, 0, "titre", "desc", 0, 0));

        insertBtn = findViewById(R.id.insert_btn);
        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert(new Proposition(0, 2, "titre", "desc", 0, 0));
            }
        });

        readBtn = findViewById(R.id.read_btn);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                read();
            }
        });

        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    public void insert(Proposition prop) {
        PropositionDao propDao = AppDatabase.getAppDatabase(this).PropositionDao();
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

    public void delete() {
        PropositionDao propDao = AppDatabase.getAppDatabase(getApplicationContext()).PropositionDao();
        new deleteAsyncTask(propDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private PropositionDao propDao;

        deleteAsyncTask(PropositionDao propDao) {
            this.propDao = propDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            propDao.nukeTable();
            Log.i("db", "Delete props from db: ");
            return null;
        }

    }

    public void read() {
        PropositionDao propDao = AppDatabase.getAppDatabase(this).PropositionDao();
        new readAsyncTask(propDao).execute();
    }

    private static class readAsyncTask extends AsyncTask<Void, Void, Void> {

        private PropositionDao propDao;

        readAsyncTask(PropositionDao propDao) {
            this.propDao = propDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            List<Proposition> props = propDao.getAll().getValue();
            Log.i("db", "Read props from db: " + props);
            return null;
        }

    }

    void getPropositionAPI(final Context context) {
        String url = Constants.address + "listeProp.php";

        final ArrayList<Proposition> propositions = new ArrayList<>();

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response from server: " + response);
                try {
                    //Todo: itérer pour toutes les propositions
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

                        //propositions.add(new Proposition(id, user_id, title, description, positive, negative));
                        //rp.insert(new Proposition(id, user_id, title, description, positive, negative));
                    }

                    //Todo: format de retour de ./listeProp est chelou
                    //Todo: refaire schéma proposition avec négatif et positif
                    //Log.i("volley", "got props: " + propositions);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(context, "Constants Error", Toast.LENGTH_SHORT).show();
            }
        });

        //Add request to queue
        RequestQueueSingleton.getInstance(context).addToRequestQueue(stringRequest);
    }

}
