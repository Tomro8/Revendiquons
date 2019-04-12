package com.example.revendiquons;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.db.entity.Vote;
import com.example.revendiquons.utils.Constants;

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
        String url = Constants.address + "getUserVotes.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Constants Error", Toast.LENGTH_SHORT).show();
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
        String url = Constants.address + "register.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Constants Error", Toast.LENGTH_SHORT).show();
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
        String url = Constants.address + "login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(appContext, "Constants Error", Toast.LENGTH_SHORT).show();
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
        String url = Constants.address + "createProp.php";

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
                Toast.makeText(appContext, "Constants Error", Toast.LENGTH_SHORT).show();
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

    public void getAllPropositionsAPI(Response.Listener<String> responseListener) {
        String url = Constants.address + "listeProp.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
            }
        });

        //Add request to queue
        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
    }

    public void forwardVoteToAPI(final Vote vote, Response.Listener<String> responseListener) {
        String url = Constants.address + "vote.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, responseListener, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("user_id", Integer.toString(vote.getId_user()));
                params.put("prop_id", Integer.toString(vote.getId_proposition()));
                params.put("forOrAgainst", Integer.toString(vote.getForOrAgainst()));
                return params;
            }
        };

        RequestQueueSingleton.getInstance(appContext).addToRequestQueue(stringRequest);
    }
}
