package com.example.revendiquons.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.revendiquons.room.AppDatabase;
import com.example.revendiquons.room.dao.PropositionDao;
import com.example.revendiquons.room.entity.Proposition;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PropositionRepository {

    private PropositionDao propDao;
    private LiveData<List<Proposition>> allProps;

    public PropositionRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        propDao = db.PropositionDao();
        allProps = propDao.getAll();
        Log.i("arch","Repository initiated with props from DB");
        Log.i("arch","RepositoryCreated");
    }

    public LiveData<List<Proposition>> getAllProps() {
        return allProps;
    }

    public void insert(Proposition prop) {
        Log.i("db", "PropRepo inserting: " + prop);
        new insertAsyncTask(propDao).execute(prop);
    }

    private static class insertAsyncTask extends AsyncTask<Proposition, Void, Void> {

        private PropositionDao propDao;

        insertAsyncTask(PropositionDao propDao) {
            this.propDao = propDao;
        }

        @Override
        protected Void doInBackground(Proposition... propositions) {
            propDao.insertAll(propositions);
            return null;
        }
    }
}
