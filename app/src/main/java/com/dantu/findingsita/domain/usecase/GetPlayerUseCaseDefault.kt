package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.data.repositories.PlayerRepository
import javax.inject.Inject

class GetPlayerUseCaseDefault @Inject constructor(private val playerRepository: PlayerRepository) : GetPlayerUseCase {
    override suspend fun invoke(playerId: Int): Player? =
        playerRepository.getPlayer(playerId)
}