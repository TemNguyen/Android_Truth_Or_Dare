package com.jthanh.truthordare.model.rooms;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.jthanh.truthordare.model.entities.Player;

import java.util.List;
@Dao
public interface PlayerDao {
    @Query("SELECT * FROM Player")
    List<Player> getAllPlayer();

    @Insert
    void insertAllPlayer(List<Player> players);

    @Delete
    void deletePlayer(Player player);

    @Query("DELETE FROM Player")
    void deleteAllPlayer();
}
