package com.example.revendiquons.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {
    @Insert
    void insertAll(User... users);

    @Update
    void updateUsers(User... users);

    @Query("SELECT * FROM User")
    List<User> getAll();

    @Delete
    public void deleteUsers(User... users);
}
