package com.jthanh.truthordare.model.rooms;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jthanh.truthordare.model.entities.Player;
import com.jthanh.truthordare.model.entities.Question;
import com.jthanh.truthordare.model.entities.QuestionPackage;

@Database(entities = {Player.class, Question.class, QuestionPackage.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract PlayerDao playerDao();
    public abstract QuestionDao questionDao();
    public abstract QuestionPackageDao questionPackageDao();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {

            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "truth_or_dare_db").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return instance;
    }
}
