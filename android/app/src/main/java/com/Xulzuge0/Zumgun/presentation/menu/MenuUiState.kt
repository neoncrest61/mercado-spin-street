package com.Xulzuge0.Zumgun.presentation.menu

import com.Xulzuge0.Zumgun.core.config.GameConfig

data class MenuUiState(
    val balance: Int = GameConfig.START_BALANCE,
    val level: Int = 1,
    val litStalls: Int = 0,
    val selectedStallId: Int = 1,
    val selectedTarget: Int = 0,
    val selectedSymbolId: Int = 1,
    val playStallId: Int = 1,
    val unlockedDecorations: Int = 0
)
