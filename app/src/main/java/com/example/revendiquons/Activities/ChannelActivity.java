package com.example.revendiquons.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.revendiquons.ExpandableRecyclerView.ParentPropModel;
import com.example.revendiquons.ExpandableRecyclerView.ExpandablePropAdapter;
import com.example.revendiquons.ExpandableRecyclerView.ChildPropModel;
import com.example.revendiquons.R;
import com.example.revendiquons.ViewModel.ChannelViewModel;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.entity.Vote;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ChannelActivity extends AppCompatActivity {

    private ChannelViewModel viewModel;
    private Button createProp_btn;
    private RecyclerView recyclerView;

    private ExpandablePropAdapter expandableAdapter;
    private List<ParentPropModel> parentPropModelList;
    private List<ChildPropModel> childPropModel;



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

        //todo: testing only
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", 1);
        editor.apply();


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

                parentPropModelList = new ArrayList<>();

                for (Proposition prop : propositions) {
                    int voteValue = viewModel.getVoteValue(prop.getUser_id(), prop.getId());
                    List<ChildPropModel> childrenList = new ArrayList<>();
                    childrenList.add(new ChildPropModel(prop.getDescription(), voteValue));

                    ParentPropModel parent = new ParentPropModel(prop, childrenList);
                    parentPropModelList.add(parent);
                }

                //Set adapter
                expandableAdapter = new ExpandablePropAdapter(parentPropModelList);
                recyclerView.setAdapter(expandableAdapter);

                //viewModel.getAllProps().removeObservers(ChannelActivity.this);
            }
        });

        viewModel.getAllVotes().observe(this, new Observer<List<Vote>>() {
            @Override
            public void onChanged(List<Vote> votes) {
                Log.i("UI", "Vote modified: " + votes.toString());

                //Only if view is ready
                if (expandableAdapter != null) {
                    //Applique le vote à l'UI (pour que l'iconne reste affiché comme tel)

                    //On parcourt Modèles des parents des propositions, on compare à la liste de vote jusqu'à trouver celui qui correspond.
                    //On change la valeur du vote dans le modèle de l'enfant
                    for (ParentPropModel parentPropModel : parentPropModelList) {
                        for (Vote vote : votes) {
                            Proposition bindProp = parentPropModel.getProposition();
                            if (bindProp.getId() == vote.getId_proposition()) {
                                parentPropModel.getItems().get(0).setVoteValue(vote.getForOrAgainst());
                            }
                        }
                    }

                    //On affiche les changements
                    expandableAdapter.notifyDataSetChanged();
                }
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

    }

    public ChannelViewModel getViewModel() {
        return  viewModel;
    }

    public void heyhey() { //todo delete
        expandableAdapter.notifyDataSetChanged();
        //expandableAdapter.
    }

}
