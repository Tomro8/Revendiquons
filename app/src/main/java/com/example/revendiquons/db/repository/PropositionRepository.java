package com.example.revendiquons.db.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.dao.PropositionDao;
import com.example.revendiquons.db.entity.Proposition;

import java.util.List;

import androidx.lifecycle.LiveData;

public class PropositionRepository {

    private static PropositionRepository instance = null;
    private PropositionDao propDao;
    private LiveData<List<Proposition>> allProps;

    public static PropositionRepository getInstance(Application application) {
        if (instance == null) {
            instance = new PropositionRepository(application);
        }
        return instance;
    }

    private PropositionRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        propDao = db.PropositionDao();
        allProps = propDao.getAll();
        Log.i("arch","Repository initiated with props from DB");
        Log.i("arch","RepositoryCreated");
    }

    public LiveData<List<Proposition>> getAllProps() {
        return allProps;
    }

    public void update(Proposition proposition) {
        new VoteAsyncTask(propDao).execute(proposition);
    }

    private static class VoteAsyncTask extends AsyncTask<Proposition, Void, Void> {

        private PropositionDao propDao;

        VoteAsyncTask(PropositionDao propDao) {
            this.propDao = propDao;
        }

        @Override
        protected Void doInBackground(Proposition... propositions) {
            propDao.updatePropositions(propositions);
            return null;
        }
    }

    public void insert(Proposition prop, DBOperationCallback dbOperationCallback) {
        Log.i("db", "PropRepo inserting: " + prop);
        new insertAsyncTask(propDao, dbOperationCallback).execute(prop);
    }

    private static class insertAsyncTask extends AsyncTask<Proposition, Void, Void> {

        private PropositionDao propDao;
        private DBOperationCallback dbOperationCallback;

        insertAsyncTask(PropositionDao propDao, DBOperationCallback dbOperationCallback) {
            this.propDao = propDao;
            this.dbOperationCallback = dbOperationCallback;
        }

        @Override
        protected Void doInBackground(Proposition... propositions) {
            propDao.insertAll(propositions);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dbOperationCallback.onOperationCompleted();
        }
    }
}
