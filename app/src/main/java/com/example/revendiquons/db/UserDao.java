package com.example.revendiquons.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {
    @Insert
    void insertAll(User... users);

    @Query("SELECT * FROM User")
    List<User> getAll();
}
