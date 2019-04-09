package com.example.revendiquons.db.dao;

import com.example.revendiquons.db.entity.Proposition;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface PropositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Proposition... Propositions);

    @Update
    void updatePropositions(Proposition... Propositions);

    @Query("SELECT * FROM Proposition")
    LiveData<List<Proposition>> getAll();

    @Delete
    public void deletePropositions(Proposition... Propositions);

    @Query("DELETE FROM Proposition")
    public void nukeTable();
}
