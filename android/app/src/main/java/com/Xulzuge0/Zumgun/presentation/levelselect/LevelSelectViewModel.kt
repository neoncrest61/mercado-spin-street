package com.Xulzuge0.Zumgun.presentation.levelselect

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.data.sample.SampleData
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LevelSelectViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LevelSelectUiState())
    val state: StateFlow<LevelSelectUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.value = map(progress)
            }
        }
    }

    fun select(stallId: Int) {
        progressRepository.update { it.copy(selectedStallId = stallId) }
    }

    private fun map(progress: BazaarProgress): LevelSelectUiState {
        val rows = SampleData.stalls.map { stall ->
            StallRow(
                id = stall.id,
                symbolId = stall.featuredSymbolId,
                target = stall.targetGoods,
                requiredLevel = stall.requiredLevel,
                decorationId = stall.decorationId,
                unlocked = progress.level >= stall.requiredLevel,
                lit = progress.litStalls.contains(stall.id),
                selected = progress.selectedStallId == stall.id
            )
        }
        return LevelSelectUiState(
            level = progress.level,
            stalls = rows,
            allLit = rows.isNotEmpty() && rows.all { it.lit }
        )
    }
}
