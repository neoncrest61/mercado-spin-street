package com.Xulzuge0.Zumgun.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.domain.model.BazaarProgress
import com.Xulzuge0.Zumgun.domain.model.GameSettings
import com.Xulzuge0.Zumgun.domain.model.ReelSpeed
import com.Xulzuge0.Zumgun.domain.model.StartingStall

class MercadoPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun readProgress(): BazaarProgress = BazaarProgress(
        balance = prefs.getInt(KEY_BALANCE, GameConfig.START_BALANCE),
        xp = prefs.getInt(KEY_XP, 0),
        selectedStallId = prefs.getInt(KEY_STALL, 1),
        unlockedDecorations = readIds(KEY_DECORATIONS),
        litStalls = readIds(KEY_LIT),
        totalSpins = prefs.getInt(KEY_SPINS, 0),
        winningSpins = prefs.getInt(KEY_WINNING_SPINS, 0),
        missionsDone = prefs.getInt(KEY_MISSIONS, 0),
        biggestWin = prefs.getInt(KEY_BIGGEST_WIN, 0),
        bestStreakTenths = prefs.getInt(KEY_BEST_STREAK, GameConfig.STREAK_BASE_TENTHS),
        coinsEarned = prefs.getInt(KEY_COINS_EARNED, 0),
        runCount = prefs.getInt(KEY_RUNS, 0)
    )

    fun writeProgress(progress: BazaarProgress) {
        prefs.edit()
            .putInt(KEY_BALANCE, progress.balance)
            .putInt(KEY_XP, progress.xp)
            .putInt(KEY_STALL, progress.selectedStallId)
            .putString(KEY_DECORATIONS, progress.unlockedDecorations.joinToString(SEPARATOR))
            .putString(KEY_LIT, progress.litStalls.joinToString(SEPARATOR))
            .putInt(KEY_SPINS, progress.totalSpins)
            .putInt(KEY_WINNING_SPINS, progress.winningSpins)
            .putInt(KEY_MISSIONS, progress.missionsDone)
            .putInt(KEY_BIGGEST_WIN, progress.biggestWin)
            .putInt(KEY_BEST_STREAK, progress.bestStreakTenths)
            .putInt(KEY_COINS_EARNED, progress.coinsEarned)
            .putInt(KEY_RUNS, progress.runCount)
            .apply()
    }

    fun readSettings(): GameSettings = GameSettings(
        reelSpeed = speedOf(prefs.getInt(KEY_SPEED, ReelSpeed.NORMAL.ordinal)),
        payLineHighlight = prefs.getBoolean(KEY_PAY_LINE, true),
        fullCelebration = prefs.getBoolean(KEY_CELEBRATION, true),
        startingStall = startOf(prefs.getInt(KEY_STARTING_STALL, StartingStall.LAST_PLAYED.ordinal))
    )

    fun writeSettings(settings: GameSettings) {
        prefs.edit()
            .putInt(KEY_SPEED, settings.reelSpeed.ordinal)
            .putBoolean(KEY_PAY_LINE, settings.payLineHighlight)
            .putBoolean(KEY_CELEBRATION, settings.fullCelebration)
            .putInt(KEY_STARTING_STALL, settings.startingStall.ordinal)
            .apply()
    }

    fun clearProgress() {
        prefs.edit()
            .remove(KEY_BALANCE)
            .remove(KEY_XP)
            .remove(KEY_STALL)
            .remove(KEY_DECORATIONS)
            .remove(KEY_LIT)
            .remove(KEY_SPINS)
            .remove(KEY_WINNING_SPINS)
            .remove(KEY_MISSIONS)
            .remove(KEY_BIGGEST_WIN)
            .remove(KEY_BEST_STREAK)
            .remove(KEY_COINS_EARNED)
            .remove(KEY_RUNS)
            .apply()
    }

    private fun readIds(key: String): Set<Int> =
        prefs.getString(key, "")
            .orEmpty()
            .split(SEPARATOR)
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()

    private fun speedOf(ordinal: Int): ReelSpeed =
        ReelSpeed.entries.getOrElse(ordinal) { ReelSpeed.NORMAL }

    private fun startOf(ordinal: Int): StartingStall =
        StartingStall.entries.getOrElse(ordinal) { StartingStall.LAST_PLAYED }

    private companion object {
        const val STORE = "mercado_spin_street"
        const val SEPARATOR = ","
        const val KEY_BALANCE = "balance"
        const val KEY_XP = "xp"
        const val KEY_STALL = "selected_stall"
        const val KEY_DECORATIONS = "decorations"
        const val KEY_LIT = "lit_stalls"
        const val KEY_SPINS = "total_spins"
        const val KEY_WINNING_SPINS = "winning_spins"
        const val KEY_MISSIONS = "missions"
        const val KEY_BIGGEST_WIN = "biggest_win"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_COINS_EARNED = "coins_earned"
        const val KEY_RUNS = "runs"
        const val KEY_SPEED = "reel_speed"
        const val KEY_PAY_LINE = "pay_line_highlight"
        const val KEY_CELEBRATION = "celebration"
        const val KEY_STARTING_STALL = "starting_stall"
    }
}
