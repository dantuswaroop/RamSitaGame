package com.dantu.findingsita.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey
    val name : String,
    val pin : Int,
    val wins : Int = 0,
    val profilePic : String? = null
)