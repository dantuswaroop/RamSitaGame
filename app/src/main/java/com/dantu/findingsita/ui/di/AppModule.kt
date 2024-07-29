package com.dantu.findingsita.ui.di

import android.content.Context
import com.dantu.findingsita.data.DataBaseHelper
import com.dantu.findingsita.data.GameDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideDataBaseHelper(@ApplicationContext context: Context) : GameDataBase {
        return DataBaseHelper.getInstance(context)
    }

}