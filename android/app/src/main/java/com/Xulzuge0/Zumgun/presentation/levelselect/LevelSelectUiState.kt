package com.Xulzuge0.Zumgun.presentation.levelselect

data class StallRow(
    val id: Int,
    val symbolId: Int,
    val target: Int,
    val requiredLevel: Int,
    val decorationId: Int,
    val unlocked: Boolean,
    val lit: Boolean,
    val selected: Boolean
)

data class LevelSelectUiState(
    val level: Int = 1,
    val stalls: List<StallRow> = emptyList(),
    val allLit: Boolean = false
)
