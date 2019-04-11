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

import com.example.revendiquons.R;
import com.example.revendiquons.WebService;
import com.example.revendiquons.db.repository.DBOperationCallback;
import com.example.revendiquons.db.repository.PropositionRepository;
import com.example.revendiquons.db.entity.Proposition;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.disposables.CompositeDisposable;

public class PropCreationActivity extends AppCompatActivity {
    //todo: boutton cancel / retour
    private Button createProp_btn;
    private TextInputLayout title_textInput;
    private TextInputLayout desc_textInput;

    private CompositeDisposable compositeDisposable; //Todo: remove if no RxJava

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

        //Do not display keyboard because edittext is on focus at activity start
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        createProp_btn = findViewById(R.id.creation_btn_submit);
        createProp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!filledInputs()) {
                    Toast.makeText(PropCreationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Get current user ID
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(PropCreationActivity.this);
                int user_id = preferences.getInt("user_id", -1); //-1 = default value
                Log.i("pref", "user_id: " + user_id);

                String title = title_textInput.getEditText().getText().toString();
                String desc = desc_textInput.getEditText().getText().toString();

                final Proposition prop = new Proposition(0, user_id, title, desc, 0, 0);

                //Insert Prop into Remote DB
                WebService.getInstance(getApplicationContext()).createPropAPICall(Integer.toString(user_id), title, desc);


                //Insert Prop into local DB
                PropositionRepository.getInstance(getApplication()).insert(prop, new DBOperationCallback() {
                    @Override
                    public void onOperationCompleted() {
                        Intent channelActivity = new Intent(PropCreationActivity.this, ChannelActivity.class);
                        startActivity(channelActivity);
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

}
