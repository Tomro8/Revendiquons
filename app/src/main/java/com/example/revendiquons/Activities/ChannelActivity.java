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
import com.example.revendiquons.ViewModel.PropositionViewModel;
import com.example.revendiquons.room.AppDatabase;
import com.example.revendiquons.room.entity.Proposition;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChannelActivity extends AppCompatActivity {

    private PropositionViewModel viewModel;
    private Button createProp_btn;
    private CompositeDisposable compositeDisposable;

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
        setContentView(R.layout.activity_channel);

        //Observe on viewModel
        viewModel = ViewModelProviders.of(this).get(PropositionViewModel.class);
        viewModel.getAllProps().observe(this, new Observer<List<Proposition>>() {
            @Override
            public void onChanged(List<Proposition> propositions) {
                Log.i("arch", "Channel UI refreshed, props: " + propositions.toString());
            }
        });

        compositeDisposable = new CompositeDisposable();

        createProp_btn = findViewById(R.id.channel_revendiquer_btn);
        createProp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.insert(new Proposition(3, 2, "More Choco", "More Choco desc"));
                /*
                Intent createPropActivity = new Intent(ChannelActivity.this, PropCreationActivity.class);
                startActivity(createPropActivity);
                */
            }
        });

        //generateSampleRecyclerView();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int user_id = preferences.getInt("user_id", -1); //-1 = default value

        /*
        AppDatabase db = AppDatabase.getAppDatabase(this);

        Single<List<Proposition>> single = db.PropositionDao().getAll();
        single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Proposition>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(List<Proposition> props) {
                        Log.i("db", "Retrieved propositions from local db: " + props);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("db", "Erreur while retrieving propositions from the local DB");
                    }

                });

        */
    }

    public void hydrateList() {
        /*
        final List<ExpandableProp> grpList = new ArrayList<ExpandableProp>();

        AppDatabase db = AppDatabase.getAppDatabase(this);
        Single<List<Proposition>> single = db.PropositionDao().getAll();
        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<List<Proposition>>() {
                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(List<Proposition> propositions) {

                    for (Proposition prop : propositions) {
                        ArrayList<ExpandedProp> children = new ArrayList<>();
                        children.add(new ExpandedProp(prop.getDescription()));
                        grpList.add(new ExpandableProp(prop.getTitle(), prop.))
                    }


                }

                @Override
                public void onError(Throwable e) {
                    Log.e("db", "Error while trying to retrieve propositions from local DB");
                }
            });
        */

    }

    private void generateSampleRecyclerView() {

        //Create children for 1st grp
        ExpandedProp ch1 = new ExpandedProp("Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description Description");
        //Add children to a list for 1st grp
        List<ExpandedProp> chList1 = new ArrayList<ExpandedProp>();
        chList1.add(ch1);
        Log.i("Tom", "childList1 created" + chList1);

        //Create children for 2nd grp
        ExpandedProp ch4 = new ExpandedProp("Description de la proposition numero 2");
        //Add children to a list for 2nd grp
        List<ExpandedProp> chList2 = new ArrayList<ExpandedProp>();
        chList2.add(ch4);
        Log.i("Tom", "childList2 created" + chList2);

        //Add children list to parent groups
        ExpandableProp gr1 = new ExpandableProp("Coucou", 133, chList1);
        Log.i("Tom", "parent1 created" + gr1);
        ExpandableProp gr2 = new ExpandableProp("1abdefghijklmnopqrstuvwxyz2abcdefghijklmnopqrstuvwxyz3abdefghijklmnopqrstuvwxyz4abdefghijklmnopqrstuvwxyz5abdefghijklmnopqrstuvwxyz6abdefghijklmnopqrstuvwxyz7abdefghijklmnopqrstuvwxyz8abdefghijklmnopqrstuvwxyz", 0, chList2);
        Log.i("Tom", "parent2 created" + gr2);

        //Put parent groups into a list to provide the adapter with
        List<ExpandableProp> grpList = new ArrayList<ExpandableProp>();
        grpList.add(gr1);
        grpList.add(gr2);

        //Set adapter
        ExpandablePropAdapter ad = new ExpandablePropAdapter(grpList);
        Log.i("Tom", "Adapter created");

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        Log.i("Tom", "Retreived RecyclerView");

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Log.i("Tom", "Linear Manager set");

        recyclerView.setAdapter(ad);
        Log.i("Tom", "Adapter set to recyclerView");
    }
}
