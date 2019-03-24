package com.example.revendiquons.Activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;

import com.example.revendiquons.ExpandableRecyclerView.ExpandablePropAdapter;
import com.example.revendiquons.ExpandableRecyclerView.ExpandableProp;
import com.example.revendiquons.ExpandableRecyclerView.ExpandedProp;
import com.example.revendiquons.R;
import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppDatabase db;

    /**
     *  This is an object containing disposable objects. When the activity is destroyed,
     *  we dispose this disposable container to destroy what is inside.
     *  This is a way to avoid memory leaks (i.e references that would stay in memory even if not in reach).
     *  We put objects likely to cause memory leaks in this disposable container.
     */
    private CompositeDisposable compositeDisposable;

    @Override
    protected void onDestroy() {
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        compositeDisposable = new CompositeDisposable();

        setContentView(R.layout.test_db);

        //Get reference to the database
        db = AppDatabase.getAppDatabase(this);

        Single<List<User>> single = db.UserDao().getAll();
        single.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<List<User>>() {

                @Override
                public void onSubscribe(Disposable d) {
                    compositeDisposable.add(d);
                }

                @Override
                public void onSuccess(List<User> users) {
                    Log.i("db", "WORKING");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("db", "Erreur");
                }

            });

        //generateRecyclerView();

    }

    /*
    private class getAuthentications extends AsyncTask<Activity, Void, List<User>> {

        private Activity activity;

        getAuthentications(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected List<User> doInBackground(Activity... activities) {
            Log.i("snd", "Retrieving data in background");
            AppDatabase db = AppDatabase.getAppDatabase(activities[0]);
            Log.i("snd", "returning the datas");
            return db.UserDao().getAll();
        }

        @Override
        protected void onPostExecute(List<User> authenticationList) {
            ((MainActivity)activity).displayUsers(authenticationList);
        }
    }
    */

    public void displayUsers(List<User> authenticationList) {
        Log.i("Tom","Users: " + authenticationList.toString());
    }

    private void generateRecyclerView() {

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
