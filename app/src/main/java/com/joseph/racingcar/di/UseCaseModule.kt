package com.joseph.racingcar.di

import com.joseph.racingcar.domain.repo.HighscoreRepository
import com.joseph.racingcar.domain.usecase.GetHighscoreUseCase
import com.joseph.racingcar.domain.usecase.SaveHighscoreUseCase
import com.joseph.racingcar.utils.SoundRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetHighscoreUseCase(
        highscoreRepository: HighscoreRepository
    ): GetHighscoreUseCase {
        return GetHighscoreUseCase(highscoreRepository)
    }

    @Provides
    @Singleton
    fun providesSaveHighscoreUseCase(
        highscoreRepository: HighscoreRepository,
        soundRepository: SoundRepository,
    ): SaveHighscoreUseCase {
        return SaveHighscoreUseCase(highscoreRepository, soundRepository)
    }
}