package com.Xulzuge0.Zumgun.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.domain.model.ReelSpeed
import com.Xulzuge0.Zumgun.domain.model.StartingStall
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import com.Xulzuge0.Zumgun.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collect { settings ->
                _state.value = SettingsUiState(
                    reelSpeed = settings.reelSpeed,
                    payLineHighlight = settings.payLineHighlight,
                    fullCelebration = settings.fullCelebration,
                    startingStall = settings.startingStall
                )
            }
        }
    }

    fun setReelSpeed(speed: ReelSpeed) {
        settingsRepository.update { it.copy(reelSpeed = speed) }
    }

    fun setPayLineHighlight(enabled: Boolean) {
        settingsRepository.update { it.copy(payLineHighlight = enabled) }
    }

    fun setFullCelebration(enabled: Boolean) {
        settingsRepository.update { it.copy(fullCelebration = enabled) }
    }

    fun setStartingStall(mode: StartingStall) {
        settingsRepository.update { it.copy(startingStall = mode) }
    }

    fun resetBazaar() {
        progressRepository.resetBazaar()
    }
}
