package com.Xulzuge0.Zumgun.domain.usecase

import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.BonusRules
import com.Xulzuge0.Zumgun.domain.model.Decoration
import com.Xulzuge0.Zumgun.domain.model.RunOutcome
import com.Xulzuge0.Zumgun.domain.model.RunResult
import com.Xulzuge0.Zumgun.domain.model.RunTally
import com.Xulzuge0.Zumgun.domain.model.Stall

data class RunApplication(
    val progress: BazaarProgress,
    val result: RunResult
)

class ApplyRunResultUseCase {

    operator fun invoke(
        progress: BazaarProgress,
        stall: Stall,
        decorations: List<Decoration>,
        tally: RunTally
    ): RunApplication {
        val lit = tally.goods >= stall.targetGoods
        val outcome = when {
            tally.outOfCoins && progress.balance < GameConfig.BET -> RunOutcome.OUT_OF_COINS
            lit -> RunOutcome.STALL_LIT
            else -> RunOutcome.MISSION_MISSED
        }
        val xpGained = if (lit) {
            GameConfig.MISSION_XP
        } else {
            tally.goods * GameConfig.PARTIAL_XP_PER_GOOD
        }
        val reward = if (lit) stall.rewardCoins else 0
        val staged = progress.copy(
            balance = progress.balance + reward,
            xp = progress.xp + xpGained,
            selectedStallId = stall.id,
            litStalls = if (lit) progress.litStalls + stall.id else progress.litStalls,
            totalSpins = progress.totalSpins + tally.spins,
            winningSpins = progress.winningSpins + tally.winningSpins,
            missionsDone = if (lit) progress.missionsDone + 1 else progress.missionsDone,
            biggestWin = maxOf(progress.biggestWin, tally.biggestWin),
            bestStreakTenths = maxOf(progress.bestStreakTenths, tally.bestStreakTenths),
            coinsEarned = progress.coinsEarned + tally.coinsWon + reward,
            runCount = progress.runCount + 1
        )
        val earned = decorations
            .filter { BonusRules.isUnlocked(it, staged) }
            .map { it.id }
            .toSet()
        val fresh = earned - staged.unlockedDecorations
        val updated = staged.copy(unlockedDecorations = staged.unlockedDecorations + earned)
        val awarded = fresh.firstOrNull { it == stall.decorationId } ?: fresh.minOrNull() ?: 0
        val result = RunResult(
            stallId = stall.id,
            outcome = outcome,
            goods = tally.goods,
            target = stall.targetGoods,
            coinsWon = tally.coinsWon + reward,
            bestStreakTenths = tally.bestStreakTenths,
            seed = tally.seed,
            decorationId = awarded,
            xpGained = xpGained
        )
        return RunApplication(updated, result)
    }
}
