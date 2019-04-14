package com.example.revendiquons.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.revendiquons.ExpandableRecyclerView.ParentPropModel;
import com.example.revendiquons.ExpandableRecyclerView.ExpandablePropAdapter;
import com.example.revendiquons.ExpandableRecyclerView.ChildPropModel;
import com.example.revendiquons.R;
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

    private Observer<List<Proposition>> loadPropositionObserver;
    private Observer<List<Proposition>> refreshPropositionObserver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Log.i("UI", "ChannelActivity creation started");

        //todo: testing only
        /*
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("user_id", 1);
        editor.apply();
        */

        //Set up RecyclerView
        recyclerView = findViewById(R.id.my_recycler_view);

        //Set a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ChannelActivity.this);
        recyclerView.setLayoutManager(layoutManager);

        //Setup observer
        loadPropositionObserver = new Observer<List<Proposition>>() {
            @Override
            public void onChanged(List<Proposition> propositions) {
                Log.i("UI", "Channel UI reloaded, props: " + propositions.toString());

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
            }
        };

        //Observer pour refresh uniquement le score des view des propositions
        refreshPropositionObserver = new Observer<List<Proposition>>() {
            @Override
            public void onChanged(List<Proposition> propositions) {
                Log.i("UI", "Channel UI refreshed, props: " + propositions.toString());
                if (expandableAdapter != null) {
                    //Pour chaque view proposition, on met le score de la proposition correspondante
                    for (ParentPropModel parentPropModel : parentPropModelList) {
                        for (Proposition prop : propositions) {
                            if (prop.getId() == parentPropModel.getProposition().getId()) {
                                parentPropModel.setScore(prop.getPositive() - prop.getNegative());
                                Log.i("UI","Setting score: " +
                                        Integer.toString(prop.getPositive() - prop.getNegative()) +
                                        " to prop: " + prop.getTitle());
                            }
                        }
                    }

                    expandableAdapter.notifyDataSetChanged();
                }
            }
        };

        //Observe on viewModel
        viewModel = ViewModelProviders.of(this).get(ChannelViewModel.class);
        setLoadPropositionObserver();

        //Applique les votes aux ChildPropViewHolder sur l'UI (pour que la bonne flèche soit sélectionnée et le reste)
        viewModel.getUserVote().observe(this, new Observer<List<Vote>>() {
            @Override
            public void onChanged(List<Vote> votes) {
                Log.i("UI", "User votes LiveData changed: " + votes.toString());
                //Only if view is ready
                if (expandableAdapter != null) {

                    //Reset all displayed votes beforehand (in the case when LiveData had more votes previously)
                    //Otherwise it can refresh votes if votes is empty
                    for (ParentPropModel parentPropModel : parentPropModelList) {
                        ChildPropModel childPropModel = parentPropModel.getItems().get(0);
                        childPropModel.setVoteValue(0);
                    }

                    //On parcourt Modèles des parents des propositions, on compare à la liste de vote jusqu'à trouver celui qui correspond.
                    //On change la valeur du vote dans le modèle de l'enfant
                    for (ParentPropModel parentPropModel : parentPropModelList) {
                        for (Vote vote : votes) {
                            Proposition bindProp = parentPropModel.getProposition();
                            if (bindProp.getId() == vote.getId_proposition()) {
                                ChildPropModel childPropModel = parentPropModel.getItems().get(0);
                                childPropModel.setVoteValue(vote.getForOrAgainst());
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

    public void setLoadPropositionObserver() {
        Log.i("UI", "LoadProposition Observer setup");
        viewModel.getAllProps().observe(this, loadPropositionObserver);
    }

    public void removeLoadPropositionObserver() {
        Log.i("UI", "LoadProposition Observer removed");
        viewModel.getAllProps().removeObserver(loadPropositionObserver);
    }

    public void setRefreshPropositionObserver() {
        Log.i("UI", "Refresh Proposition Observer setup");
        viewModel.getAllProps().observe(this, refreshPropositionObserver);
    }

    public void removeRefreshPropositionObserver() {
        Log.i("UI", "Refresh proposition Observer removed");
        viewModel.getAllProps().removeObserver(refreshPropositionObserver);
    }
}
