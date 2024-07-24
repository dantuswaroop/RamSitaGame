package com.dantu.findingsita.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.dantu.findingsita.data.entities.GameStatus

@Dao
interface GameStatusDao {

    @Query("SELECT * FROM GameStatus where gameId LIKE :gameId")
    fun getGameStatus(gameId : String) : List<GameStatus>

    @Insert
    fun insertPlayerRecord(gameStatus: GameStatus)

    @Update
    fun updatePlayerScore(gameStatus: GameStatus)
}