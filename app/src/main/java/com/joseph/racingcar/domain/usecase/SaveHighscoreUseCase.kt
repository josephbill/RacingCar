package com.joseph.racingcar.domain.usecase

import com.joseph.racingcar.domain.repo.HighscoreRepository
import com.joseph.racingcar.ui.viewmodel.MainViewModel
import com.joseph.racingcar.utils.SoundRepository
import kotlinx.coroutines.flow.first

class SaveHighscoreUseCase(
    private val highscoreRepository: HighscoreRepository,
    private val soundRepository: SoundRepository
) {

    suspend fun execute(score: Int) {
        val currentHighscore = highscoreRepository.getHighScore().first()
        if (score > currentHighscore) {
            highscoreRepository.saveHighScore(score)
            soundRepository.playSound(MainViewModel.NEW_HIGHSCORE_SOUND_ID)
        }
    }
}