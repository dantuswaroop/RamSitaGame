package com.dantu.findingsita.data.repositories

import com.dantu.findingsita.data.GameDataBase
import com.dantu.findingsita.data.entities.Player
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PlayerRepositoryDefault @Inject constructor(private val gameDataBase: GameDataBase) : PlayerRepository {
    override suspend fun getPlayers(): List<Player> =
        gameDataBase.playerDao().getAllPlayers().first()

    override suspend fun createOrUpdatePlayer(id: Int?, name: String, pin: Int) {
        id?.let {
            gameDataBase.playerDao().updatePlayer(Player(id, name, pin))
        } ?: run {
            gameDataBase.playerDao().addPlayer(Player(name = name, pin = pin))
        }
    }

    override suspend fun deletePlayer(playerId: Int) {
        gameDataBase.playerDao().deletePlayer(Player(playerId = playerId, "", 0))
    }
}