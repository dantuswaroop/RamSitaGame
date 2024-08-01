package com.dantu.findingsita.data.repositories

import com.dantu.findingsita.data.GameDataBase
import com.dantu.findingsita.data.entities.Player
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PlayerRepositoryDefault @Inject constructor(private val gameDataBase: GameDataBase) : PlayerRepository {
    override suspend fun getPlayers(): List<Player> =
        gameDataBase.playerDao().getAllPlayers().first()

    override suspend fun createOrUpdatePlayer(
        id: Int,
        name: String,
        pin: Int,
        profileImage: String?
    ) {
        if(id == 0) {
            gameDataBase.playerDao().addPlayer(Player(name = name, pin = pin, profilePic =  profileImage))
        } else {
            gameDataBase.playerDao().updatePlayer(Player(id, name, pin, profilePic =  profileImage))
        }
    }

    override suspend fun deletePlayer(playerId: Int) {
        gameDataBase.playerDao().deletePlayer(Player(playerId = playerId, "", 0))
    }

    override suspend fun getPlayer(playerId: Int) : Player? =
        gameDataBase.playerDao().getPlayer(playerId)

}