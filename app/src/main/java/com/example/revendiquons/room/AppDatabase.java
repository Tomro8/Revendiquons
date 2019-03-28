package com.example.revendiquons.room;

import android.content.Context;

import com.example.revendiquons.room.dao.PropositionDao;
import com.example.revendiquons.room.dao.UserDao;
import com.example.revendiquons.room.dao.VoteDao;
import com.example.revendiquons.room.entity.Proposition;
import com.example.revendiquons.room.entity.User;
import com.example.revendiquons.room.entity.Vote;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, Proposition.class, Vote.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract UserDao UserDao();
    public abstract PropositionDao PropositionDao();
    public abstract VoteDao voteDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "user-database").build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
