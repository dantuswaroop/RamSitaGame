package com.dantu.findingsita.data.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class Player @JvmOverloads constructor(
    @PrimaryKey(autoGenerate = true)
    var playerId : Int = 0,
    var name : String,
    var pin : Int,
    var wins : Int = 0,
    var profilePic : String? = null,
    @Ignore var selected : Boolean = false,
    @Ignore var character: Character? = null
)