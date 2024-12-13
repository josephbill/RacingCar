package com.joseph.racingcar.domain.usecase

import com.joseph.racingcar.domain.repo.HighscoreRepository
import kotlinx.coroutines.flow.Flow

class GetHighscoreUseCase(
    private val highscoreRepository: HighscoreRepository,
) {
    fun execute(): Flow<Int> = highscoreRepository.getHighScore()
}