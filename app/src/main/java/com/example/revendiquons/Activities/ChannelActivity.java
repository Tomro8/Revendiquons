package com.example.revendiquons.Activities;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Response;
import com.example.revendiquons.ExpandableRecyclerView.ParentPropModel;
import com.example.revendiquons.ExpandableRecyclerView.ExpandablePropAdapter;
import com.example.revendiquons.ExpandableRecyclerView.ChildPropModel;
import com.example.revendiquons.R;
import com.example.revendiquons.WebService;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.entity.Vote;
import com.example.revendiquons.db.repository.DBOperationCallback;
import com.example.revendiquons.db.repository.PropositionRepository;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ChannelActivity extends AppCompatActivity {

    private ChannelViewModel viewModel;
    private Button createProp_btn;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ExpandablePropAdapter expandableAdapter;
    private List<ParentPropModel> parentPropModelList;

    private Observer<List<Proposition>> loadPropositionObserver;
    private Observer<List<Proposition>> refreshPropositionObserver;
    private int user_id;
    //todo: in MVVM I should not put data in the view. But as long as I don't have a logout button, I cannot put user_id in viewModel, when switching user, I currently can't recreate viewModel

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        Log.i("UI", "ChannelActivity creation started");

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i("UI","User requested Refreshing data");
                WebService.getInstance(ChannelActivity.this).getAllPropositionsAPI(getRefreshResponseListener());
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        user_id = preferences.getInt("user_id", -1);

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
                    int voteValue = viewModel.getVoteValue(user_id, prop.getId());
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
                                /*
                                Log.i("UI","Setting score: " +
                                        Integer.toString(prop.getPositive() - prop.getNegative()) +
                                        " to prop: " + prop.getTitle());
                                */
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

    public Response.Listener<String> getRefreshResponseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("volley", "populate prop entity: response from server: " + response);
                try {
                    //Stop to observe Prop LivaData while updating data in entity
                    removeLoadPropositionObserver();

                    //Empty table
                    PropositionRepository.getInstance((Application)getApplicationContext()).deletePropositions();

                    //Convert string response to JSONObject
                    JSONArray json = new JSONArray(response);

                    for (int i=0; i<json.length(); i++) {
                        int id = json.getJSONObject(i).getInt("id");
                        int user_id = json.getJSONObject(i).getInt("id_user");
                        String title = json.getJSONObject(i).getString("titre");
                        String description = json.getJSONObject(i).getString("description");
                        int positive = json.getJSONObject(i).getInt("positive");
                        int negative = json.getJSONObject(i).getInt("negative");

                        //Todo: insert list of prop instead of once at a time, purpose: having a callback after everything is inserted
                        //Populate Table
                        PropositionRepository.getInstance((Application)getApplicationContext()).
                                insert(new Proposition(id, user_id, title, description, positive, negative), new DBOperationCallback() {
                                    @Override
                                    public void onOperationCompleted() {
                                        //Nothing to do
                                    }
                                });
                    }

                    //Data loading is finished, observe again to display the new data
                    setLoadPropositionObserver();
                    swipeRefreshLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }
}
