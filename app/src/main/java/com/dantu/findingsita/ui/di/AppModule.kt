package com.dantu.findingsita.ui.di

import android.content.Context
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.GameDataBase
import com.dantu.findingsita.data.repositories.PlayerRepository
import com.dantu.findingsita.data.repositories.PlayerRepositoryDefault
import com.dantu.findingsita.domain.GetAllPlayersUseCase
import com.dantu.findingsita.domain.GetAllPlayersUseCaseDefault
import com.dantu.findingsita.domain.usecase.CreateOrUpdatePlayerUseCase
import com.dantu.findingsita.domain.usecase.CreateOrUpdatePlayerUseCaseDefault
import com.dantu.findingsita.domain.usecase.DeletePlayerUseCase
import com.dantu.findingsita.domain.usecase.DeletePlayerUseCaseDefault
import com.dantu.findingsita.domain.usecase.GetPlayerUseCase
import com.dantu.findingsita.domain.usecase.GetPlayerUseCaseDefault
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideDataBaseHelper(@ApplicationContext context: Context): GameDataBase {
        return DataBaseHelper.getInstance(context)
    }

    @Provides
    fun provideGetAllPlayersUseCase(playerRepository: PlayerRepository): GetAllPlayersUseCase =
        GetAllPlayersUseCaseDefault(playerRepository)

    @Provides
    fun providePlayerRepository(gameDataBase: GameDataBase): PlayerRepository =
        PlayerRepositoryDefault(gameDataBase)

    @Provides
    @Singleton
    fun provideCreateUpdatePlayerUSeCase(playerRepository: PlayerRepository): CreateOrUpdatePlayerUseCase =
        CreateOrUpdatePlayerUseCaseDefault(playerRepository)

    @Provides
    @Singleton
    fun provideDeletePlayerUseCase(playerRepository: PlayerRepository): DeletePlayerUseCase =
        DeletePlayerUseCaseDefault(playerRepository)

    @Provides
    @Singleton
    fun provideGetPlayerUseCase(playerRepository: PlayerRepository): GetPlayerUseCase =
        GetPlayerUseCaseDefault(playerRepository)

}