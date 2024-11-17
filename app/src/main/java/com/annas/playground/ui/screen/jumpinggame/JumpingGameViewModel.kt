package com.annas.playground.ui.screen.jumpinggame

import androidx.compose.runtime.mutableIntStateOf
import com.annas.playground.helper.PreferenceHelper
import com.annas.playground.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class JumpingGameViewModel @Inject constructor(
    private val preferenceHelper: PreferenceHelper
) : BaseViewModel() {

    var highestScore = mutableIntStateOf(preferenceHelper.getInt(HIGHEST_SCORE))
        private set

    fun saveScore(value: Int) {
        if (value > highestScore.intValue) {
            preferenceHelper.saveInt(HIGHEST_SCORE, value)
            highestScore.intValue = value
        }
    }

    companion object KEY {
        const val HIGHEST_SCORE = "HIGHEST_SCORE"
    }
}
