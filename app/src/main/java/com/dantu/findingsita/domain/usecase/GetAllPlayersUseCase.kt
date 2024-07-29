package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.entities.Player

interface GetAllPlayersUseCase {
    suspend operator fun invoke() : List<Player>
}