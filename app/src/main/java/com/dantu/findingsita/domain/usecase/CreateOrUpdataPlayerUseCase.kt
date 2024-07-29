package com.dantu.findingsita.domain.usecase

interface CreateOrUpdatePlayerUseCase {

    suspend operator fun invoke(id : Int?, name : String, pin : Int)
}