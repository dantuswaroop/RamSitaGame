package com.dantu.findingsita.data.repositories

import com.dantu.findingsita.data.entities.Player

interface PlayerRepository {

    suspend fun getPlayers() : List<Player>
    suspend fun createOrUpdatePlayer(id : Int?, name: String, pin: Int)
    suspend fun deletePlayer(playerId: Int)

}