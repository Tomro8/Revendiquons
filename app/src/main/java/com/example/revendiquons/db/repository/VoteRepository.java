package com.example.revendiquons.db.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.example.revendiquons.db.AppDatabase;
import com.example.revendiquons.db.dao.VoteDao;
import com.example.revendiquons.db.entity.Vote;

import java.util.List;

import androidx.lifecycle.LiveData;

public class VoteRepository {
    private VoteDao voteDao;
    private LiveData<List<Vote>> allVotes;
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
        allVotes = voteDao.getAll();
    }

    public LiveData<List<Vote>> getAllVotes() {
        return allVotes;
    }

    public void insert(Vote vote) {
        new insertAsyncTask(voteDao).execute(vote);
        Log.i("db", "VoteRepo, inserting: " + vote);
    }

    private static class insertAsyncTask extends AsyncTask<Vote, Void, Void> {
        private VoteDao voteDao;

        insertAsyncTask(VoteDao voteDao) {
            this.voteDao = voteDao;
        }

        @Override
        protected Void doInBackground(Vote... votes) {
            voteDao.insertAll(votes);
            return null;
        }
    }
}