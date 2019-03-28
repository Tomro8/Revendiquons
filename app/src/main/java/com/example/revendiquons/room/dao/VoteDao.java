package com.example.revendiquons.room.dao;

import com.example.revendiquons.room.entity.Vote;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface VoteDao {
    @Insert
    void insertAll(Vote... votes);

    @Update
    void updateVotes(Vote... votes);

    @Query("SELECT * FROM Vote")
    LiveData<List<Vote>> getAll();

    @Delete
    public void deleteVotes(Vote... votes);
}
