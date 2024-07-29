package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.entities.Player

interface CreateGameUseCase {

    suspend operator fun invoke(players: List<Player>) : String
}