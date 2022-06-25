package com.jthanh.truthordare.model.rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jthanh.truthordare.model.entities.QuestionPackage;

import java.util.List;

@Dao
public interface QuestionPackageDao {
    @Query("SELECT * FROM QuestionPackage")
    List<QuestionPackage> getAllQuestionPackage();

    @Query("SELECT * FROM QuestionPackage WHERE id = :id")
    QuestionPackage findQuestionPackageById(String id);

    @Query("SELECT COUNT(id) FROM QuestionPackage")
    int getQuestionPackageCount();

    @Query("SELECT EXISTS(SELECT * FROM QuestionPackage WHERE id = :id)")
    boolean isPackageExist(String id);

    @Insert
    void insertAllQuestionPackage(QuestionPackage... questionPackages);

    @Update
    void updateQuestionPackage(QuestionPackage questionPackage);

    @Delete
    void DeleteQuestionPackage(QuestionPackage questionPackage);

    @Query("DELETE FROM QuestionPackage")
    void deleteAll();
}
