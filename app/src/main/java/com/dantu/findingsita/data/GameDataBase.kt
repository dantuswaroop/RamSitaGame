package com.dantu.findingsita.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dantu.findingsita.data.dao.GameDao
import com.dantu.findingsita.data.dao.GameStatusDao
import com.dantu.findingsita.data.dao.PlayerDao
import com.dantu.findingsita.data.entities.Game
import com.dantu.findingsita.data.entities.GameStatus
import com.dantu.findingsita.data.entities.Player

@Database(entities = [Player::class, Game::class, GameStatus::class], version = 1)
abstract class GameDataBase : RoomDatabase() {
    abstract fun playerDao() : PlayerDao
    abstract fun gameDao() : GameDao
    abstract fun gameStatusDao() : GameStatusDao
}