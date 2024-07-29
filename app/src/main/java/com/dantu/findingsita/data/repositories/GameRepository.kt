package com.dantu.findingsita.data.repositories

import com.dantu.findingsita.data.entities.Player

interface GameRepository {

    suspend fun createGame(players: List<Player>) : String
}