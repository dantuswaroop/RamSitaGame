package com.dantu.findingsita.domain

import com.dantu.findingsita.data.entities.Player

interface GetAllPlayersUseCase {
    suspend operator fun invoke() : List<Player>
}