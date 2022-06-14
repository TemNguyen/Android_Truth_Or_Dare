package com.jthanh.truthordare.model;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Player.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "truth_or_dare_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return instance;
    }
}
