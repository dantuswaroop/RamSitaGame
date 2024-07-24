package com.dantu.findingsita.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameStatus(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val gameId : String,
    val playerId : Int,
    val score : Int
)