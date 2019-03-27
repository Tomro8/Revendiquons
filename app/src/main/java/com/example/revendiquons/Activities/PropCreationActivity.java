package com.example.revendiquons.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.revendiquons.R;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PropCreationActivity extends AppCompatActivity {

    private Button createProp_btn;
    private TextInputLayout title_textInput;
    private TextInputLayout desc_textInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        createProp_btn = findViewById(R.id.creation_btn_submit);
        createProp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        title_textInput = findViewById(R.id.creation_editText_court);
        desc_textInput = findViewById(R.id.creation_editText_desc);
    }
}
