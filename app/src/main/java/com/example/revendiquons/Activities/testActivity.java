package com.example.revendiquons.Activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.R;
import com.example.revendiquons.RequestQueueSingleton;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class testActivity extends AppCompatActivity {

    Button testBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_db);

        testBtn = findViewById(R.id.test_btn);
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPropositionAPI(view.getContext());
            }
        });
    }

    void getPropositionAPI(final Context context) {
        String url = Server.address + "listeProp.php";

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

                        propositions.add(new Proposition(id, user_id, title, description, positive, negative));
                    }

                    //Todo: format de retour de ./listeProp est chelou
                    //Todo: refaire schéma proposition avec négatif et positif
                    Log.i("volley", "got props: " + propositions);

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
