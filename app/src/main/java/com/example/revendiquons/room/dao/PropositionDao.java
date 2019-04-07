package com.example.revendiquons.room.dao;

import com.example.revendiquons.room.entity.Proposition;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface PropositionDao {
    @Insert
    void insertAll(Proposition... Propositions);

    @Update
    void updatePropositions(Proposition... Propositions);

    @Query("SELECT * FROM Proposition")
    List<Proposition> getAll();

    @Delete
    public void deletePropositions(Proposition... Propositions);

    @Query("DELETE FROM Proposition")
    public void nukeTable();
}