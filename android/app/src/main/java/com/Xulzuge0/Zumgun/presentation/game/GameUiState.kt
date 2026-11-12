package com.Xulzuge0.Zumgun.presentation.game

import com.Xulzuge0.Zumgun.core.config.GameConfig

data class GameUiState(
    val stallId: Int = 1,
    val targetGoods: Int = 1,
    val featuredSymbolId: Int = 1,
    val goods: Int = 0,
    val spinIndex: Int = 0,
    val balance: Int = 0,
    val grid: List<Int> = List(GameConfig.GRID_SIZE) { 1 },
    val spinningReels: Set<Int> = emptySet(),
    val winningCells: Set<Int> = emptySet(),
    val winningLine: List<Int> = emptyList(),
    val lastWin: Int = 0,
    val showBanner: Boolean = false,
    val noDeal: Boolean = false,
    val streakTenths: Int = GameConfig.STREAK_BASE_TENTHS,
    val charge: Int = 0,
    val goldenReady: Boolean = false,
    val spinning: Boolean = false,
    val paused: Boolean = false,
    val payLineHighlight: Boolean = true,
    val fullCelebration: Boolean = true,
    val speedPermille: Int = 1000,
    val setBonusPercent: Int = 0
) {
    val spinsLeft: Int get() = GameConfig.SPINS_PER_RUN - spinIndex
}

sealed interface GameEvent {
    data object RunFinished : GameEvent
    data object LeftToMenu : GameEvent
}
