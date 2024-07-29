package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.entities.Player
import com.dantu.findingsita.data.repositories.GameRepository
import javax.inject.Inject

class CreateGameUseCaseDefault @Inject constructor(private val gameRepository : GameRepository) : CreateGameUseCase {

    override suspend fun invoke(players: List<Player>) : String = gameRepository.createGame(players)
}