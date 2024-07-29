package com.dantu.findingsita.domain.usecase

interface DeletePlayerUseCase {

    suspend operator fun invoke(playerId : Int)
}