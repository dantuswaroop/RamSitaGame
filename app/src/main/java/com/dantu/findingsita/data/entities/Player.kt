package com.dantu.findingsita.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true)
    val playerId : Int,
    val name : String,
    val pin : Int,
    val wins : Int,
    val profilePic : String?
)