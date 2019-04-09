package com.example.revendiquons;

import android.app.Application;
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
import com.example.revendiquons.repository.DBOperationCallback;
import com.example.revendiquons.repository.PropositionRepository;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.utils.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class WebService {
    private static WebService instance;
    private Context appContext;

    public static WebService getInstance(Context applicationCtx) {
        if (instance == null) {
            instance = new WebService(applicationCtx);
        }
        return instance;
    }

    private WebService(Context applicationCtx) {
        super();
        this.appContext = applicationCtx;
    }

    public void getUserVotes(final String user_id, Response.Listener<String> responseListener) {
        String url = Server.address + "getUserVotes.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override //Override method of anonymous class StringRequest
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                return params;
            }
        };

        //Add request to queue
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
    }

    public void registerAPICall(final String mail, final String password, Response.Listener<String> responseListener) {
        String url = Server.address + "register.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override //Override method of anonymous class StringRequest
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail", mail);
                params.put("password", password);
                return params;
            }
        };

        //Add request to queue
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
    }

    public void loginAPICall(final String email, final String password, Response.Listener<String> responseListener) {
        String url = Server.address + "login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mail", email);
                params.put("password", password);
                return params;
            }
        };

        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
    }

    public void createPropAPICall(final String user_id, final String title, final String desc) {
        Log.i("volley", "in create PropAPI Call");
        String url = Server.address + "createProp.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Convert string response to JSONObject
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        Log.i("volley","Proposition successfully added to remote DB");
                    } else {
                        Log.i("volley", "Request returned an error: " + json.getString("error"));
                        Toast.makeText(appContext, "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override //Override method of anonymous class StringRequest
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("title", title);
                params.put("text", desc);
                return params;
            }
        };

        //Add request to queue
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
        Log.i("volley", "Request added to queue");
    }

    public void getPropositionAPICall(Response.Listener<String> responseListener) {
        String url = Server.address + "listeProp.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });

        //Add request to queue
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
    }
}
