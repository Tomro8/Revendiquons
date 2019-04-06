package com.example.revendiquons.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.R;
import com.example.revendiquons.RequestQueueSingleton;
import com.example.revendiquons.room.AppDatabase;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.utils.Server;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

public class PropCreationActivity extends AppCompatActivity {
    //todo: bouton cancel
    private Button createProp_btn;
    private TextInputLayout title_textInput;
    private TextInputLayout desc_textInput;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        compositeDisposable = new CompositeDisposable();

        createProp_btn = findViewById(R.id.creation_btn_submit);
        createProp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!filledInputs()) {
                    Toast.makeText(PropCreationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PropCreationActivity.this);
                int user_id = preferences.getInt("user_id", -1); //-1 = default value
                Log.i("pref", "user_id: " + user_id);

                final Proposition prop = new Proposition(-1, user_id,
                        title_textInput.getEditText().getText().toString(),
                        desc_textInput.getEditText().getText().toString(), 0, 0);

                final AppDatabase db = AppDatabase.getAppDatabase(PropCreationActivity.this);
                Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Exception {
                        db.PropositionDao().insertAll(prop);
                    }
                })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            compositeDisposable.add(d);
                        }

                        @Override
                        public void onComplete() {
                            Intent channelActivity = new Intent(PropCreationActivity.this, ChannelActivity.class);
                            startActivity(channelActivity);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.i("db", "Failed to insert new proposition into local DB");
                            Log.e("db", e.toString());
                        }
                    });

            }
        });

        title_textInput = findViewById(R.id.creation_editText_court);
        desc_textInput = findViewById(R.id.creation_editText_desc);
    }

    public boolean filledInputs() {
        if (title_textInput.getEditText().getText().toString().isEmpty()) {
            return false;
        }
        if (desc_textInput.getEditText().getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public void createPropAPICall() {
        String url = Server.address + "createProp.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //Convert string response to JSONObject
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        //Go to channel Activity
                        Intent channelActivity = new Intent(PropCreationActivity.this, ChannelActivity.class);
                        startActivity(channelActivity);
                    } else {
                        Log.e("volley", "Request returned an error" + json.getString("error"));
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("volley", error.toString());
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override //Override method of anonymous class StringRequest
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("title", title_textInput.getEditText().getText().toString());
                params.put("text", desc_textInput.getEditText().getText().toString());
                return params;
            }
        };

        //Add request to queue
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
