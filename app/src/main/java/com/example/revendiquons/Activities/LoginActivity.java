package com.example.revendiquons.Activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.revendiquons.utils.Server;
import com.google.android.material.textfield.TextInputLayout;

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
        //todo: finish transition
        //overridePendingTransition( R.anim.slide_in_up, R.anim.slide_out_up );

        //Do not display keyboard because edittext is on focus at activity start
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        email_textInput = findViewById(R.id.connection_editText_mail);
        email_textInput = findViewById(R.id.connection_editText_password);

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
                if (!inputFilled()) {
                    Toast.makeText(getApplicationContext(), "You must fill all the inputs", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userExist()) {
                    email_textInput.setError("Unknown Account");
                    return;
                }
                email_textInput.setErrorEnabled(false);

                if (!checkPassword()) {
                    pwd_textInput.setError("Wrong Password");
                    return;
                }
                pwd_textInput.setErrorEnabled(false);

                Intent channelActivity = new Intent(getApplicationContext(), ChannelActivity.class);
                startActivity(channelActivity);
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

    public boolean userExist() {
        //TODO userExist Query
        String url = Server.address + "userExist.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mail", email_textInput.getEditText().getText().toString());
                return params;
            }
        };

        return false;
    }

    public boolean checkPassword() {
        //TODO check password Query
        return false;
    }
}
