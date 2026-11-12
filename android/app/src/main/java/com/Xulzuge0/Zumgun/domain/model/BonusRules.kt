package com.Xulzuge0.Zumgun.domain.model

import com.Xulzuge0.Zumgun.core.config.GameConfig

object BonusRules {

    fun completedSets(unlocked: Set<Int>, decorations: List<Decoration>): List<DecorationSet> =
        DecorationSet.entries.filter { group ->
            val ids = decorations.filter { it.group == group }.map { it.id }
            ids.isNotEmpty() && ids.all { unlocked.contains(it) }
        }

    fun setBonusPercent(unlocked: Set<Int>, decorations: List<Decoration>): Int =
        minOf(
            completedSets(unlocked, decorations).size * GameConfig.SET_BONUS_PERCENT,
            GameConfig.SET_BONUS_MAX_PERCENT
        )

    fun isUnlocked(decoration: Decoration, progress: BazaarProgress): Boolean =
        when (val rule = decoration.unlock) {
            is DecorationUnlock.StallLit -> progress.litStalls.contains(rule.stallId)
            is DecorationUnlock.BazaarLevel -> progress.level >= rule.level
            is DecorationUnlock.TotalSpins -> progress.totalSpins >= rule.spins
            is DecorationUnlock.MissionsDone -> progress.missionsDone >= rule.missions
            is DecorationUnlock.BiggestWin -> progress.biggestWin >= rule.coins
        }
}
