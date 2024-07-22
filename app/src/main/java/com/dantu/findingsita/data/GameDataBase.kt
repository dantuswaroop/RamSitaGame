package com.dantu.findingsita.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dantu.findingsita.data.dao.PlayerDao
import com.dantu.findingsita.data.entities.Player

@Database(entities = [Player::class], version = 1)
abstract class GameDataBase : RoomDatabase() {
    abstract fun playerDao() : PlayerDao
}