package com.dantu.findingsita.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.dantu.findingsita.data.entities.Player

@Dao
interface PlayerDao {

    @Query("SELECT * FROM player")
    fun getAllPlayers() : List<Player>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addPlayer(player: Player)

    @Update
    fun updatePlayer(player: Player)

    @Delete
    fun deletePlayer(player: Player)
}