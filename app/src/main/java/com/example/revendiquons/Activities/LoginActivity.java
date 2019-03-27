package com.example.revendiquons.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.R;
import com.example.revendiquons.RequestQueueSingleton;
import com.example.revendiquons.utils.Server;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Button goToLoginBtn;
    private Button submitBtn;
    private TextInputLayout email_textInput;
    private TextInputLayout pwd_textInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //todo: finish transition, same for login activity
        //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        //Do not display keyboard because edittext is on focus at activity start
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        email_textInput = findViewById(R.id.connection_editText_mail);
        pwd_textInput = findViewById(R.id.connection_editText_password);

        goToLoginBtn = findViewById(R.id.connection_btn_register);
        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerActivity = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(registerActivity);
            }
        });

        submitBtn = findViewById(R.id.connection_btn_connexion);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_textInput.setErrorEnabled(false);
                pwd_textInput.setErrorEnabled(false);

                if (!inputFilled()) {
                    Toast.makeText(getApplicationContext(), "You must fill all the inputs", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginAPICall();
            }
        });
    }

    public boolean inputFilled() {
        if (email_textInput.getEditText().getText().toString().isEmpty()) {
            return false;
        }
        if (pwd_textInput.getEditText().getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    public void loginAPICall() {
        String url = Server.address + "login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response from server: " + response.toString());
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("user_id",json.getInt("user_id"));
                        editor.apply();

                        Intent channelActivity = new Intent(getApplicationContext(), ChannelActivity.class);
                        startActivity(channelActivity);
                    } else {
                        switch (json.getString("error")) {
                            case "user not exist":
                                email_textInput.setError("Account does not exist");
                                break;
                            case "wrong password":
                                pwd_textInput.setError("Password is incorrect");
                                break;
                            default:
                                Log.e("volley", "Request returned an error" + json.getString("error"));
                                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
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
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mail", email_textInput.getEditText().getText().toString());
                params.put("password", pwd_textInput.getEditText().getText().toString());
                return params;
            }
        };

        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
}
