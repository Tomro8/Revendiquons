package com.example.revendiquons.repository;

import android.app.Application;
import android.os.AsyncTask;

import com.example.revendiquons.room.AppDatabase;
import com.example.revendiquons.room.dao.VoteDao;
import com.example.revendiquons.room.entity.Vote;

import java.util.List;

import androidx.lifecycle.LiveData;

public class VoteRepository {
    private VoteDao voteDao;
    private LiveData<List<Vote>> allVotes;

    public VoteRepository(Application application) {
        AppDatabase db = AppDatabase.getAppDatabase(application);
        voteDao = db.voteDao();
        allVotes = voteDao.getAll();
    }

    public LiveData<List<Vote>> getAllVotes() {
        return allVotes;
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
            return null;
        }
    }
}
