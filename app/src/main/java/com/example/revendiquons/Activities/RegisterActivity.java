package com.example.revendiquons.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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
import com.example.revendiquons.WebService;
import com.example.revendiquons.utils.Server;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private Button goToLoginBtn;
    private TextInputLayout mail_textInput;
    private TextInputLayout pwd1_textInput;
    private TextInputLayout pwd2_textInput;
    private Button submit_btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Do not display keyboard because edittext is on focus at activity start
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        goToLoginBtn = findViewById(R.id.register_btn_connection);
        goToLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
            }
        });

        submit_btn = findViewById(R.id.register_btn_submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mail_textInput.setErrorEnabled(false);
                pwd1_textInput.setErrorEnabled(false);
                pwd2_textInput.setErrorEnabled(false);

                if (!inputAreFilled()) {
                    Toast.makeText(getApplicationContext(), "You must fill all the inputs", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mailIsCompatible()) {
                    Toast.makeText(getApplicationContext(), "Email address must be Efrei or equivalent", Toast.LENGTH_SHORT).show();
                    mail_textInput.setError("Incorrect Email Address");
                    return;
                }

                if (!pwdLength()) {
                    Toast.makeText(getApplicationContext(), "Password must be 8 characters minimum", Toast.LENGTH_SHORT).show();
                    pwd1_textInput.setError("8 characters minimum");
                    pwd2_textInput.setError("8 characters minimum");
                    return;
                }

                if (!pwdAreIdentical()) {
                    Toast.makeText(getApplicationContext(), "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    pwd1_textInput.setError("Different passwords");
                    pwd2_textInput.setError("Different passwords");
                    return;
                }

                WebService.getInstance(getApplicationContext()).registerAPICall(
                        mail_textInput.getEditText().getText().toString(),
                        pwd1_textInput.getEditText().getText().toString(),
                        registerCallBack());
            }
        });

        mail_textInput = findViewById(R.id.editText_mail);
        pwd1_textInput = findViewById(R.id.editText_mdp1);
        pwd2_textInput = findViewById(R.id.editText_mdp2);
    }
/*
    public void registerAPICall() {
        String url = Server.address + "register.php";

        //Request a string response from the URL
        StringRequest stringRequest = new StringRequest (Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response from server: " + response.toString());
                try {
                    //Convert string response to JSONObject
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("user_id",json.getInt("user_id"));
                        editor.apply();

                        //Go to channel Activity
                        Intent channelActivity = new Intent(RegisterActivity.this, ChannelActivity.class);
                        startActivity(channelActivity);
                    } else {
                        if (json.getString("error").equals("mail already taken")) {
                            mail_textInput.setError("Account already existing");
                        } else {
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
            @Override //Override method of anonymous class StringRequest
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mail", mail_textInput.getEditText().getText().toString());
                params.put("password", pwd1_textInput.getEditText().getText().toString());
                return params;
            }
        };

        //Add request to queue
        RequestQueueSingleton.getInstance(this).addToRequestQueue(stringRequest);
    }
    */

    public Response.Listener<String> registerCallBack() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "response from server: " + response.toString());
                try {
                    //Convert string response to JSONObject
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("user_id",json.getInt("user_id"));
                        editor.apply();

                        //Go to channel Activity
                        Intent channelActivity = new Intent(RegisterActivity.this, ChannelActivity.class);
                        startActivity(channelActivity);
                    } else {
                        if (json.getString("error").equals("mail already taken")) {
                            mail_textInput.setError("Account already existing");
                        } else {
                            Log.e("volley", "Request returned an error" + json.getString("error"));
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public boolean mailIsCompatible() {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9]*\\@(efrei|esigetel|efreitech)(\\.net|\\.fr)");
        return pattern.matcher(mail_textInput.getEditText().getText().toString()).matches();
    }

    public boolean pwdLength() {
        if (pwd1_textInput.getEditText().getText().toString().length() >= 8) {
            return true;
        }
        return false;
    }

    public boolean pwdAreIdentical() {
        return pwd1_textInput.getEditText().getText().toString().equals(pwd2_textInput.getEditText().getText().toString());
    }

    public boolean inputAreFilled() {
        if (TextUtils.isEmpty(mail_textInput.getEditText().getText())) {
            return false;
        }
        if (TextUtils.isEmpty(pwd1_textInput.getEditText().getText())) {
            return false;
        }
        if (TextUtils.isEmpty(pwd2_textInput.getEditText().getText())) {
            return false;
        }
        return true;
    }
}
