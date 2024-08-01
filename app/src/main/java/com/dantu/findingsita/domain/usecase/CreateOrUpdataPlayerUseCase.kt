package com.dantu.findingsita.domain.usecase

interface CreateOrUpdatePlayerUseCase {

    suspend operator fun invoke(
        id: kotlin.Int,
        name: kotlin.String,
        pin: kotlin.Int,
        profilePictureUri: kotlin.String?
    )
}