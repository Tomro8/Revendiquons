package com.example.revendiquons.db.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.toolbox.StringRequest;
import com.example.revendiquons.Activities.testActivity;
import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.dao.PropositionDao;
import com.example.revendiquons.db.dao.VoteDao;
import com.example.revendiquons.db.entity.Vote;

import java.util.List;

import androidx.lifecycle.LiveData;

public class VoteRepository {
    private VoteDao voteDao;
    private LiveData<List<Vote>> userVotes;
    static private VoteRepository instance;

    static public VoteRepository getInstance(Application application) {
        if (instance == null) {
            instance = new VoteRepository(application);
        }
        return instance;
    }

    private VoteRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        voteDao = db.voteDao();
        userVotes = voteDao.getUserVote();
        Log.i("db","VoteRepository created, now exposes LiveData user votes");
    }

    public LiveData<List<Vote>> getUserVotes() {
        return userVotes;
    }

    public void insert(Vote vote) {
        new insertAsyncTask(voteDao).execute(vote);
    }

    private static class insertAsyncTask extends AsyncTask<Vote, Void, Void> {
        private VoteDao voteDao;

        insertAsyncTask(VoteDao voteDao) {
            this.voteDao = voteDao;
        }

        @Override
        protected Void doInBackground(Vote... votes) {
            voteDao.insertAll(votes);

            StringBuilder str = new StringBuilder();
            for (Vote vote : votes) {
                str.append(vote.toString());
            }

            Log.i("db", "Insert votes into DB: " + str);
            return null;
        }
    }

    public void deleteVotes() {
        new deleteAsyncTask(voteDao).execute();
    }

    private static class deleteAsyncTask extends AsyncTask<Void, Void, Void> {

        private VoteDao voteDao;

        deleteAsyncTask(VoteDao voteDao) {
            this.voteDao = voteDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            voteDao.nukeTable();
            Log.i("db", "Delete votes from db");
            return null;
        }

    }
}
