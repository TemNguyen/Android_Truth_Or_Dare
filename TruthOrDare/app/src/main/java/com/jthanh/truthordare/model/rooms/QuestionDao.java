package com.jthanh.truthordare.model.rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.jthanh.truthordare.model.entities.Question;

import java.util.List;

@Dao
public interface QuestionDao {
    @Query("SELECT * FROM Question")
    List<Question> getAllQuestion();

    @Query("SELECT * FROM Question WHERE id = :id")
    Question getQuestionById(String id);

    @Query("SELECT * FROM Question WHERE packageId = :packageId")
    List<Question> getQuestionByPackageId(String packageId);

    @Insert
    void insertAllQuestion(Question... questions);

    @Update
    void updateQuestion(Question question);

    @Delete
    void DeleteQuestion(Question question);

    @Query("DELETE FROM Question")
    void deleteAll();
}
