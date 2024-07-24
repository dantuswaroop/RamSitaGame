package com.dantu.findingsita.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.dantu.findingsita.data.entities.Game

@Dao
interface GameDao {
    @Insert
    fun createGame(game: Game)

    @Query("SELECT * FROM Game")
    fun getAllGames() : List<Game>
}