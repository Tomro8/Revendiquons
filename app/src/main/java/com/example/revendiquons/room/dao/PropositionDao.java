package com.example.revendiquons.room.dao;

import com.example.revendiquons.room.entity.Proposition;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface PropositionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Proposition... Propositions);

    @Update
    void updatePropositions(Proposition... Propositions);

    @Query("UPDATE Proposition SET positive = positive+1 WHERE id = :prop_id")
    public void upVote(int prop_id);

    @Query("UPDATE Proposition SET positive = negative-1 WHERE id = :prop_id")
    public void downVote(int prop_id);

    @Query("UPDATE Proposition SET positive = positive+1 WHERE id = :prop_id")
    public void neutralVote(int prop_id);

    @Query("SELECT * FROM Proposition")
    LiveData<List<Proposition>> getAll();

    @Delete
    public void deletePropositions(Proposition... Propositions);

    @Query("DELETE FROM Proposition")
    public void nukeTable();
}
