package com.Xulzuge0.Zumgun.domain.model

enum class ReelSpeed(val permille: Int) {
    RELAXED(1350),
    NORMAL(1000),
    QUICK(700)
}

enum class StartingStall { LAST_PLAYED, ALWAYS_FIRST }

data class GameSettings(
    val reelSpeed: ReelSpeed = ReelSpeed.NORMAL,
    val payLineHighlight: Boolean = true,
    val fullCelebration: Boolean = true,
    val startingStall: StartingStall = StartingStall.LAST_PLAYED
)
