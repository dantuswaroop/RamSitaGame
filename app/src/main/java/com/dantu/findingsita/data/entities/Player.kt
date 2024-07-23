package com.dantu.findingsita.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Player(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    var name : String,
    var pin : Int,
    var wins : Int = 0,
    var profilePic : String? = null
)