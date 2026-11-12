package com.Xulzuge0.Zumgun.presentation.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.data.sample.SampleData
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.GameSettings
import com.Xulzuge0.Zumgun.domain.model.RunResult
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import com.Xulzuge0.Zumgun.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ResultViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResultUiState())
    val state: StateFlow<ResultUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progressRepository.lastRun,
                progressRepository.progress,
                settingsRepository.settings
            ) { run, progress, settings -> map(run, progress, settings) }
                .collect { mapped -> _state.value = mapped }
        }
    }

    fun grantCoins() {
        progressRepository.update { it.copy(balance = it.balance + GameConfig.MARKET_GRANT) }
    }

    private fun map(
        run: RunResult?,
        progress: BazaarProgress,
        settings: GameSettings
    ): ResultUiState {
        if (run == null) {
            return ResultUiState(
                hasResult = false,
                balance = progress.balance,
                fullCelebration = settings.fullCelebration
            )
        }
        val stall = SampleData.stallById(run.stallId)
        return ResultUiState(
            hasResult = true,
            stallId = run.stallId,
            symbolId = stall.featuredSymbolId,
            outcome = run.outcome,
            goods = run.goods,
            target = run.target,
            coinsWon = run.coinsWon,
            bestStreakTenths = run.bestStreakTenths,
            seed = run.seed,
            decorationId = run.decorationId,
            xpGained = run.xpGained,
            balance = progress.balance,
            fullCelebration = settings.fullCelebration
        )
    }
}
