package com.Xulzuge0.Zumgun.domain.model

enum class RunOutcome { STALL_LIT, MISSION_MISSED, OUT_OF_COINS }

data class RunTally(
    val stallId: Int,
    val goods: Int,
    val coinsWon: Int,
    val spins: Int,
    val winningSpins: Int,
    val biggestWin: Int,
    val bestStreakTenths: Int,
    val seed: Long,
    val outOfCoins: Boolean
)

data class RunResult(
    val stallId: Int,
    val outcome: RunOutcome,
    val goods: Int,
    val target: Int,
    val coinsWon: Int,
    val bestStreakTenths: Int,
    val seed: Long,
    val decorationId: Int,
    val xpGained: Int
)
