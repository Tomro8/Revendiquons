package com.example.revendiquons.db.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.revendiquons.Activities.testActivity;
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

    public void update(Proposition proposition, DBOperationCallback dbOperationCallback, DBBeforeOperation dbBeforeOperation) {
        new VoteAsyncTask(propDao, dbOperationCallback, dbBeforeOperation).execute(proposition);
    }

    private static class VoteAsyncTask extends AsyncTask<Proposition, Void, Void> {

        private PropositionDao propDao;
        private DBOperationCallback dbOperationCallback;
        private DBBeforeOperation dbBeforeOperation;

        VoteAsyncTask(PropositionDao propDao, DBOperationCallback dbOperationCallback, DBBeforeOperation dbBeforeOperation) {
            this.propDao = propDao;
            this.dbOperationCallback = dbOperationCallback;
            this.dbBeforeOperation = dbBeforeOperation;
        }

        @Override
        protected void onPreExecute() {
            dbBeforeOperation.onPreExecute();
        }

        @Override
        protected Void doInBackground(Proposition... propositions) {
            Log.i("db", "Updating Prop !");
            propDao.updatePropositions(propositions);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dbOperationCallback.onOperationCompleted();
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

    public void deletePropositions() {
        new deleteAsyncTask(propDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private PropositionDao propDao;

        deleteAsyncTask(PropositionDao propDao) {
            this.propDao = propDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            propDao.nukeTable();
            Log.i("db", "Delete props from db");
            return null;
        }

    }
}
