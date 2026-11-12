package com.Xulzuge0.Zumgun.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.data.sample.SampleData
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.GameSettings
import com.Xulzuge0.Zumgun.domain.model.StartingStall
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import com.Xulzuge0.Zumgun.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                progressRepository.progress,
                settingsRepository.settings
            ) { progress, settings -> map(progress, settings) }
                .collect { mapped -> _state.value = mapped }
        }
    }

    private fun map(progress: BazaarProgress, settings: GameSettings): MenuUiState {
        val selected = SampleData.stallById(progress.selectedStallId)
        val playStall = when (settings.startingStall) {
            StartingStall.ALWAYS_FIRST -> SampleData.stalls.first().id
            StartingStall.LAST_PLAYED -> selected.id
        }
        return MenuUiState(
            balance = progress.balance,
            level = progress.level,
            litStalls = progress.litStalls.size,
            selectedStallId = selected.id,
            selectedTarget = selected.targetGoods,
            selectedSymbolId = selected.featuredSymbolId,
            playStallId = playStall,
            unlockedDecorations = progress.unlockedDecorations.size
        )
    }
}
