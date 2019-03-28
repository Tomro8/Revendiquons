package com.example.revendiquons.room.dao;

import com.example.revendiquons.room.entity.User;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Single;

@Dao
public interface UserDao {
    @Insert
    void insertAll(User... users);

    @Update
    void updateUsers(User... users);

    @Query("SELECT * FROM User")
    Single<List<User>> getAll();

    @Delete
    public void deleteUsers(User... users);
}
