package com.dantu.findingsita.domain.usecase

import com.dantu.findingsita.data.repositories.PlayerRepository
import javax.inject.Inject

class CreateOrUpdatePlayerUseCaseDefault @Inject constructor(private val playerRepository: PlayerRepository) : CreateOrUpdatePlayerUseCase {
    override suspend fun invoke(id : Int?, name: String, pin: Int) {
        playerRepository.createOrUpdatePlayer(id, name, pin)
    }
}