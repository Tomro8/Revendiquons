package com.example.revendiquons.ViewModel;

import android.app.Application;
import android.util.Log;

import com.example.revendiquons.repository.PropositionRepository;
import com.example.revendiquons.room.entity.Proposition;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class PropositionViewModel extends AndroidViewModel {
    private PropositionRepository propRepository;
    private LiveData<List<Proposition>> allProps;

    public PropositionViewModel(Application application) {
        super(application);
        propRepository = new PropositionRepository(application);
        allProps = propRepository.getAllProps();
        Log.i("arch","ViewModelCreated");
    }

    public LiveData<List<Proposition>> getAllProps() {
        return allProps;
    }

    public void insert(Proposition prop) {
        propRepository.insert(prop);
    }
}
