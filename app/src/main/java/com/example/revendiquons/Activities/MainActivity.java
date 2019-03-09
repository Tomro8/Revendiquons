package com.example.revendiquons.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.revendiquons.Adapters.ExpandablePropAdapter;
import com.example.revendiquons.Models.ExpandableProp;
import com.example.revendiquons.Models.ExpandedProp;
import com.example.revendiquons.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create children for 1st grp
        ExpandedProp ch1 = new ExpandedProp("Description de la proposition numero 1");
        ExpandedProp ch2 = new ExpandedProp("Description de la proposition numero 2");
        ExpandedProp ch3 = new ExpandedProp("Description de la proposition numero 3");
        //Add children to a list for 1st grp
        List<ExpandedProp> chList1 = new ArrayList<ExpandedProp>();
        chList1.add(ch1);
        chList1.add(ch2);
        chList1.add(ch3);
        Log.i("Tom", "childList1 created" + chList1);

        //Create children for 2nd grp
        ExpandedProp ch4 = new ExpandedProp("Description de la proposition numero 4");
        ExpandedProp ch5 = new ExpandedProp("Description de la proposition numero 5");
        //Add children to a list for 2nd grp
        List<ExpandedProp> chList2 = new ArrayList<ExpandedProp>();
        chList2.add(ch1);
        chList2.add(ch2);
        chList2.add(ch3);
        Log.i("Tom", "childList2 created" + chList2);

        //Add children list to parent groups
        ExpandableProp gr1 = new ExpandableProp("Titre Proposition 1", chList1);
        Log.i("Tom", "parent1 created" + gr1);
        ExpandableProp gr2 = new ExpandableProp("Titre Proposition 2", chList2);
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
