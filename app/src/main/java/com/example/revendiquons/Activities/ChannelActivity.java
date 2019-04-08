package com.example.revendiquons.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.revendiquons.ExpandableRecyclerView.ExpandableProp;
import com.example.revendiquons.ExpandableRecyclerView.ExpandablePropAdapter;
import com.example.revendiquons.ExpandableRecyclerView.ExpandedProp;
import com.example.revendiquons.R;
import com.example.revendiquons.ViewModel.ChannelViewModel;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.room.entity.Vote;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelActivity extends AppCompatActivity {

    private ChannelViewModel viewModel;
    private Button createProp_btn;
    private RecyclerView recyclerView;
    private ExpandablePropAdapter expandableAdapter;
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        expandableAdapter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        expandableAdapter.onRestoreInstanceState(savedInstanceState);
    }
    */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Log.i("Tom", "ChannelActivity Created");

        //Set up RecyclerView
        recyclerView = findViewById(R.id.my_recycler_view);

        //Set a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChannelActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //Observe on viewModel
        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        viewModel.getAllProps().observe(this, new Observer<List<Proposition>>() {
            @Override
            public void onChanged(List<Proposition> propositions) {
                Log.i("UI", "Channel UI refreshed, props: " + propositions.toString());

                List<ExpandableProp> parentGrp = new ArrayList<>();

                for (Proposition prop : propositions) {
                    ExpandedProp child = new ExpandedProp(prop.getDescription(), prop.getId());
                    List<ExpandedProp> childGrp = new ArrayList<>();
                    childGrp.add(child);

                    ExpandableProp parent = new ExpandableProp(prop, childGrp);
                    parentGrp.add(parent);
                }

                //Set adapter
                expandableAdapter = new ExpandablePropAdapter(parentGrp);
                recyclerView.setAdapter(expandableAdapter);

                //viewModel.getAllProps().removeObservers(ChannelActivity.this);
            }
        });

        createProp_btn = findViewById(R.id.channel_revendiquer_btn);
        createProp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPropActivity = new Intent(ChannelActivity.this, PropCreationActivity.class);
                startActivity(createPropActivity);
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int user_id = preferences.getInt("user_id", -1); //-1 = default value
    }

    public ChannelViewModel getViewModel() {
        return  viewModel;
    }

    public void heyhey() {
        expandableAdapter.notifyDataSetChanged();
        //expandableAdapter.
    }

}
