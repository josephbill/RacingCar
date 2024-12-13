package com.joseph.racingcar.di

import android.content.Context
import com.joseph.racingcar.data.repo.HighscoreRepositoryImpl
import com.joseph.racingcar.data.source.highscoreDataStore
import com.joseph.racingcar.domain.repo.HighscoreRepository
import com.joseph.racingcar.utils.SoundRepository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun providesHighscoreRepository(
        @ApplicationContext context: Context
    ): HighscoreRepository {
        return HighscoreRepositoryImpl(context.highscoreDataStore)
    }

    @Provides
    @Singleton
    fun providesSoundManager(
        @ApplicationContext context: Context
    ): SoundRepository {
        return SoundRepository(context)
    }


}