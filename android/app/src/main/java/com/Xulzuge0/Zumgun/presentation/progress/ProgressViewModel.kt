package com.Xulzuge0.Zumgun.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.data.sample.SampleData
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.BonusRules
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUiState())
    val state: StateFlow<ProgressUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.value = map(progress)
            }
        }
    }

    private fun map(progress: BazaarProgress): ProgressUiState = ProgressUiState(
        level = progress.level,
        xpIntoLevel = progress.xpIntoLevel,
        xpPerLevel = GameConfig.XP_PER_LEVEL,
        slots = SampleData.stalls.map { stall ->
            StreetSlot(
                stallId = stall.id,
                symbolId = stall.featuredSymbolId,
                lit = progress.litStalls.contains(stall.id)
            )
        },
        totalSpins = progress.totalSpins,
        missions = progress.missionsDone,
        biggestWin = progress.biggestWin,
        bestStreakTenths = progress.bestStreakTenths,
        hitRate = progress.hitRatePercent,
        coinsEarned = progress.coinsEarned,
        bonusPercent = BonusRules.setBonusPercent(
            progress.unlockedDecorations,
            SampleData.decorations
        )
    )
}
