package com.Xulzuge0.Zumgun.presentation.collection

import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.domain.model.DecorationSet

data class DecorationTile(
    val id: Int,
    val symbolId: Int,
    val unlocked: Boolean,
    val hintRes: Int,
    val hintAmount: Int
)

data class DecorationGroupUi(
    val group: DecorationSet,
    val tiles: List<DecorationTile>,
    val ownedCount: Int,
    val complete: Boolean
)

data class CollectionUiState(
    val groups: List<DecorationGroupUi> = emptyList(),
    val unlocked: Int = 0,
    val total: Int = GameConfig.DECORATION_COUNT,
    val bonusPercent: Int = 0
)
