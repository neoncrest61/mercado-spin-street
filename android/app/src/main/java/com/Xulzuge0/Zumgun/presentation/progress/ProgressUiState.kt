package com.Xulzuge0.Zumgun.presentation.progress

import com.Xulzuge0.Zumgun.core.config.GameConfig

data class StreetSlot(
    val stallId: Int,
    val symbolId: Int,
    val lit: Boolean
)

data class ProgressUiState(
    val level: Int = 1,
    val xpIntoLevel: Int = 0,
    val xpPerLevel: Int = GameConfig.XP_PER_LEVEL,
    val slots: List<StreetSlot> = emptyList(),
    val totalSpins: Int = 0,
    val missions: Int = 0,
    val biggestWin: Int = 0,
    val bestStreakTenths: Int = GameConfig.STREAK_BASE_TENTHS,
    val hitRate: Int = 0,
    val coinsEarned: Int = 0,
    val bonusPercent: Int = 0
) {
    val hasRuns: Boolean get() = totalSpins > 0

    val xpFraction: Float
        get() = if (xpPerLevel == 0) 0f else xpIntoLevel.toFloat() / xpPerLevel.toFloat()
}
