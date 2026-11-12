package com.Xulzuge0.Zumgun.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.data.sample.SampleData
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.BonusRules
import com.Xulzuge0.Zumgun.domain.model.DecorationSet
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CollectionUiState())
    val state: StateFlow<CollectionUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            progressRepository.progress.collect { progress ->
                _state.value = map(progress)
            }
        }
    }

    private fun map(progress: BazaarProgress): CollectionUiState {
        val groups = DecorationSet.entries.map { group ->
            val tiles = SampleData.decorations
                .filter { it.group == group }
                .map { decoration ->
                    val hint = AssetMap.unlockHint(decoration)
                    DecorationTile(
                        id = decoration.id,
                        symbolId = decoration.spriteSymbolId,
                        unlocked = progress.unlockedDecorations.contains(decoration.id),
                        hintRes = hint.textRes,
                        hintAmount = hint.amount
                    )
                }
            val owned = tiles.count { it.unlocked }
            DecorationGroupUi(
                group = group,
                tiles = tiles,
                ownedCount = owned,
                complete = tiles.isNotEmpty() && owned == tiles.size
            )
        }
        return CollectionUiState(
            groups = groups,
            unlocked = progress.unlockedDecorations.size,
            total = SampleData.decorations.size,
            bonusPercent = BonusRules.setBonusPercent(
                progress.unlockedDecorations,
                SampleData.decorations
            )
        )
    }
}
