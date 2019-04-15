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

import com.android.volley.Response;
import com.example.revendiquons.R;
import com.example.revendiquons.WebService;
import com.example.revendiquons.db.AppDatabase;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

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

                //API call to log user
                WebService.getInstance(getApplicationContext()).loginAPICall(
                        email_textInput.getEditText().getText().toString(),
                        pwd_textInput.getEditText().getText().toString(),
                        loginCallBack());
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

    public Response.Listener<String> loginCallBack() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "Login API Call. Response from server: " + response.toString());
                try {
                    JSONObject json = new JSONObject(response);

                    if (json.has("success")) {
                        int user_id = json.getInt("user_id");
                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("user_id", user_id);
                        editor.apply();

                        Log.i("db", "User logged in. User id: " + preferences.getInt("user_id", -1));

                        //Populate Vote Entity with user votes from Remote, clear table before hand
                        AppDatabase.getAppDatabase(getApplicationContext()).populateVoteEntity(getApplicationContext(), user_id);

                        //Redirect to channel activity
                        Intent channelActivity = new Intent(getApplicationContext(), ChannelActivity.class);
                        startActivity(channelActivity);
                    } else {
                        switch (json.getString("error")) {
                            case "user not exist":
                                email_textInput.setError("Account does not exist");
                                break;
                            case "account not activated" :
                                email_textInput.setError("Account not activated");
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
        };
    }

}
