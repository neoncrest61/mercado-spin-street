package com.Xulzuge0.Zumgun.presentation.result

import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.domain.model.RunOutcome

data class ResultUiState(
    val hasResult: Boolean = false,
    val stallId: Int = 1,
    val symbolId: Int = 1,
    val outcome: RunOutcome = RunOutcome.MISSION_MISSED,
    val goods: Int = 0,
    val target: Int = 0,
    val coinsWon: Int = 0,
    val bestStreakTenths: Int = GameConfig.STREAK_BASE_TENTHS,
    val seed: Long = 0L,
    val decorationId: Int = 0,
    val xpGained: Int = 0,
    val balance: Int = 0,
    val fullCelebration: Boolean = true
) {
    val canReplay: Boolean get() = balance >= GameConfig.BET
}
