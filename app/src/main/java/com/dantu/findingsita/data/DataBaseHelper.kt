package com.dantu.findingsita.data

import android.content.Context
import androidx.room.Room

object DataBaseHelper {

    var database : GameDataBase? = null
    fun getInstance(context: Context): GameDataBase {
        if (database == null) {
            database =
                Room.databaseBuilder(context, GameDataBase::class.java, "game-database").build()
        }
        return database!!
    }
}