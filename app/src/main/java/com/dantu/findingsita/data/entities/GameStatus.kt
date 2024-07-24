package com.dantu.findingsita.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameStatus(
    @PrimaryKey(autoGenerate = true)
    val gameStatusId : Int = 0,
    val gameId : String,
    @Embedded val player : Player,
    var score : Int
)