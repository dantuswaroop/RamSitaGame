package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.entities.Player

interface GetPlayerUseCase {
    suspend operator fun invoke(playerId : Int) : Player?
}