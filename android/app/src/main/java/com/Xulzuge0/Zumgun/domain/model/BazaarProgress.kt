package com.Xulzuge0.Zumgun.domain.model

import com.Xulzuge0.Zumgun.core.config.GameConfig

data class BazaarProgress(
    val balance: Int = GameConfig.START_BALANCE,
    val xp: Int = 0,
    val selectedStallId: Int = 1,
    val unlockedDecorations: Set<Int> = emptySet(),
    val litStalls: Set<Int> = emptySet(),
    val totalSpins: Int = 0,
    val winningSpins: Int = 0,
    val missionsDone: Int = 0,
    val biggestWin: Int = 0,
    val bestStreakTenths: Int = GameConfig.STREAK_BASE_TENTHS,
    val coinsEarned: Int = 0,
    val runCount: Int = 0
) {
    val level: Int get() = xp / GameConfig.XP_PER_LEVEL + 1

    val xpIntoLevel: Int get() = xp % GameConfig.XP_PER_LEVEL

    val hitRatePercent: Int
        get() = if (totalSpins == 0) 0 else winningSpins * 100 / totalSpins
}
