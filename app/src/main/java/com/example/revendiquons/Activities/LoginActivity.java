package com.example.revendiquons.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.revendiquons.R;
import com.google.android.material.textfield.TextInputLayout;

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
        setContentView(R.layout.activity_connection);

        //Do not display keyboard because edittext is on focus at activity start
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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

    public boolean userExist() {
        //TODO userExist Query
        return false;
    }

    public boolean checkPassword() {
        //TODO check password Query
        return false;
    }
}
