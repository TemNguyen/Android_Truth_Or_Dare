package com.jthanh.truthordare.model;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface DatabaseDao {
    @Query("SELECT * FROM Player")
    List<Player> getAllPlayer();

    @Insert
    void insertAllPlayer(List<Player> players);

    @Delete
    void deletePlayer(Player player);

    @Query("DELETE FROM Player")
    void deleteAllPlayer();
}
