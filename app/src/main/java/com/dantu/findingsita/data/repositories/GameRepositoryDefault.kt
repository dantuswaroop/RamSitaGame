package com.dantu.findingsita.data.repositories

import com.dantu.findingsita.data.GameDataBase
import com.dantu.findingsita.data.entities.Game
import com.dantu.findingsita.data.entities.GameStatus
import com.dantu.findingsita.data.entities.Player
import java.util.Date
import java.util.UUID
import javax.inject.Inject

class GameRepositoryDefault @Inject constructor(private val gameDataBase: GameDataBase) : GameRepository {

    override suspend fun createGame(players: List<Player>) : String{
        val gameId = UUID.randomUUID().toString()
        gameDataBase.gameDao().createGame(Game(id = gameId, created = Date().toString()))
        with(gameDataBase.gameStatusDao()) {
            players.forEach {
                insertPlayerRecord(GameStatus(gameId = gameId, player = it, score = 0))
            }
        }
        return gameId
    }
}