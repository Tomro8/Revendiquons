package com.example.revendiquons.ViewModel;

import android.app.Application;
import android.util.Log;

import com.example.revendiquons.repository.DBOperationCallback;
import com.example.revendiquons.repository.PropositionRepository;
import com.example.revendiquons.repository.VoteRepository;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.room.entity.Vote;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class ChannelViewModel extends AndroidViewModel {
    private PropositionRepository propRepository;
    private VoteRepository voteRepository;
    private LiveData<List<Proposition>> allProps;
    private LiveData<List<Vote>> allVotes;

    public ChannelViewModel(Application application) {
        super(application);
        propRepository = PropositionRepository.getInstance(application);
        voteRepository = new VoteRepository(application);
        allProps = propRepository.getAllProps();
        allVotes = voteRepository.getAllVotes();
        Log.i("arch","ViewModelCreated");
    }

    public LiveData<List<Proposition>> getAllProps() {
        return allProps;
    }
    public LiveData<List<Vote>> getAllVotes() {
        return allVotes;
    }

    public void insertProp(Proposition prop, DBOperationCallback dbOperationCallback) {
        propRepository.insert(prop, dbOperationCallback);
    }

    public void updateProp(Proposition prop) {
        propRepository.update(prop);
    }

    public void insertVote(Vote vote) {
        voteRepository.insert(vote);
    }
}
