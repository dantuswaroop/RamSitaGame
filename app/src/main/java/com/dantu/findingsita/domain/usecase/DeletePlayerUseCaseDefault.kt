package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.repositories.PlayerRepository
import javax.inject.Inject

class DeletePlayerUseCaseDefault @Inject constructor(private val playerRepository: PlayerRepository) : DeletePlayerUseCase {
    override suspend fun invoke(playerId: Int) {
        playerRepository.deletePlayer(playerId)
    }
}