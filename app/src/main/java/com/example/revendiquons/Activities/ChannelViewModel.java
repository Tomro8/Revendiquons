package com.example.revendiquons.Activities;

import android.app.Application;
import android.util.Log;

import com.example.revendiquons.db.repository.DBOperationCallback;
import com.example.revendiquons.db.repository.PropositionRepository;
import com.example.revendiquons.db.repository.VoteRepository;
import com.example.revendiquons.db.entity.Proposition;
import com.example.revendiquons.db.entity.Vote;

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
        voteRepository = VoteRepository.getInstance(application);
        allProps = propRepository.getAllProps();
        allVotes = voteRepository.getAllVotes();
        Log.i("arch","ViewModelCreated");
    }

    public LiveData<List<Proposition>> getAllProps() {
        return allProps;
    }

    public void insertProp(Proposition prop, DBOperationCallback dbOperationCallback) {
        propRepository.insert(prop, dbOperationCallback);
    }

    /*
    public void updateProp(Proposition prop) {
        propRepository.update(prop);
    }
    */

    public LiveData<List<Vote>> getAllVotes() {
        return allVotes;
    }

    public int getVoteValue(int user_id, int prop_id) { //Todo: Transform to querry
        List<Vote> votes = allVotes.getValue();
        if (votes != null) {
            for (Vote vote : votes) {
                if (user_id == vote.getId_user() && prop_id == vote.getId_proposition()) {
                    return vote.getForOrAgainst();
                }
            }
        }

        return 0;
    }

    public void insertVote(Vote vote) {
        voteRepository.insert(vote);
    }
}
