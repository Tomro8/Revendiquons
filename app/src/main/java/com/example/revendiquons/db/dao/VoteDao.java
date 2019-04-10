package com.example.revendiquons.db.dao;

import com.example.revendiquons.db.entity.Vote;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface VoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Vote... votes);

    @Update
    void updateVotes(Vote... votes);

    @Query("SELECT * FROM Vote")
    LiveData<List<Vote>> getAll();

    @Delete
    public void deleteVotes(Vote... votes);

    @Query("DELETE FROM Vote")
    public void nukeTable();
}
