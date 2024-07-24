package com.dantu.findingsita.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey
    val id : String,
    val name : String? = null,
    val created : String
)