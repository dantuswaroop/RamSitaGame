package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.data.repositories.PlayerRepository
import javax.inject.Inject

class GetAllPlayersUseCaseDefault @Inject constructor(
    private val playerRepository: PlayerRepository
) : GetAllPlayersUseCase {
    override suspend fun invoke(): List<Player> =
        playerRepository.getPlayers()

}