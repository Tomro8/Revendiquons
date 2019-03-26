package com.example.revendiquons.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.revendiquons.ExpandableRecyclerView.ExpandablePropAdapter;
import com.example.revendiquons.ExpandableRecyclerView.ExpandableProp;
import com.example.revendiquons.ExpandableRecyclerView.ExpandedProp;
import com.example.revendiquons.R;
import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;
    private Button submit;

    /**
     *  This is an object containing disposable objects. When the activity is destroyed,
     *  we dispose this disposable container to destroy what is inside.
     *  This is a way to avoid memory leaks (i.e references that would stay in memory even if not in reach).
     *  We put objects likely to cause memory leaks in this disposable container.
     */
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        setContentView(R.layout.test_db);

        //Get reference to the database
        db = AppDatabase.getAppDatabase(this);

        Single<List<User>> single = db.UserDao().getAll();
        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<List<User>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(List<User> users) {
                    Log.i("db", "WORKING");
                    ((TextView)findViewById(R.id.RxJava_Text)).setText("RxJava+Room: Retrieved data from local DB: users: " + users.toString());
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("db", "Erreur");
                    ((TextView)findViewById(R.id.RxJava_Text)).setText("RxJava+Room: Failed to retrive data from local DB");
                }

            });


        submit = findViewById(R.id.test_btn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doStuf();
            }
        });

    }

    public void doStuf() {

        //Instanciate the request queue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://104.248.245.22/test.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ((TextView) findViewById(R.id.Volley_Text)).setText("Got response from server: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((TextView) findViewById(R.id.Volley_Text)).setText("Error during request");
            }
        });

        //Add request to queue
        queue.add(stringRequest);


        String url1 = "http://104.248.245.22/register.php";

        //Request a string response from the URL
        StringRequest stringRequest1 = new StringRequest (Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "Got response from server string: " + response);
                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.i("volley", "Got response from server JSON: " + json);

                boolean dd = json.optBoolean("success");
                Log.i("volley", "success is: " + dd);


                ((TextView) findViewById(R.id.VolleyPOST_Text)).setText("Got response from server: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
            }
        }) {
            @Override //Override method of anynomous class StringRequest
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail","tom@efrei.net");
                params.put("password","my_pwd");
                return params;
            }
        };

        //Add request to queue
        queue.add(stringRequest1);
    }

    public void displayUsers(List<User> authenticationList) {
        Log.i("Tom","Users: " + authenticationList.toString());
    }
}
