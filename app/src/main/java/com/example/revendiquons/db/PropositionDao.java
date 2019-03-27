package com.example.revendiquons.db;

import java.util.List;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

public interface PropositionDao {
    @Insert
    void insertAll(Proposition... Propositions);

    @Update
    void updatePropositions(Proposition... Propositions);

    @Query("SELECT * FROM Proposition")
    Single<List<Proposition>> getAll();

    @Delete
    public void deletePropositions(Proposition... Propositions);
}
