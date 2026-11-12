package com.Xulzuge0.Zumgun.core.ui

import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.domain.model.Decoration
import com.Xulzuge0.Zumgun.domain.model.DecorationSet
import com.Xulzuge0.Zumgun.domain.model.DecorationUnlock

data class UnlockHint(val textRes: Int, val amount: Int)

object AssetMap {

    fun symbolDrawable(symbolId: Int): Int = when (symbolId) {
        1 -> R.drawable.sprite_1
        2 -> R.drawable.sprite_2
        3 -> R.drawable.sprite_3
        4 -> R.drawable.sprite_4
        else -> R.drawable.sprite_5
    }

    fun symbolName(symbolId: Int): Int = when (symbolId) {
        1 -> R.string.symbol_chili
        2 -> R.string.symbol_papel
        3 -> R.string.symbol_crate
        4 -> R.string.symbol_talavera
        else -> R.string.symbol_lantern
    }

    fun stallName(stallId: Int): Int = when (stallId) {
        1 -> R.string.stall_chili_row
        2 -> R.string.stall_papel_lane
        3 -> R.string.stall_crate_corner
        4 -> R.string.stall_talavera_plaza
        else -> R.string.stall_lantern_square
    }

    fun decorationName(decorationId: Int): Int = when (decorationId) {
        1 -> R.string.decor_grand_lantern
        2 -> R.string.decor_paper_lantern
        3 -> R.string.decor_bulb_string
        4 -> R.string.decor_papel_banner
        5 -> R.string.decor_flag_row
        6 -> R.string.decor_ribbon_arch
        7 -> R.string.decor_chili_string
        8 -> R.string.decor_fruit_awning
        9 -> R.string.decor_spice_sacks
        10 -> R.string.decor_tiled_counter
        11 -> R.string.decor_stone_fountain
        else -> R.string.decor_market_bench
    }

    fun setName(group: DecorationSet): Int = when (group) {
        DecorationSet.LANTERNS -> R.string.set_lanterns
        DecorationSet.BANNERS -> R.string.set_banners
        DecorationSet.GOODS -> R.string.set_goods
        DecorationSet.PLAZA -> R.string.set_plaza
    }

    fun unlockHint(decoration: Decoration): UnlockHint = when (val rule = decoration.unlock) {
        is DecorationUnlock.StallLit -> UnlockHint(R.string.unlock_stall, rule.stallId)
        is DecorationUnlock.BazaarLevel -> UnlockHint(R.string.unlock_level, rule.level)
        is DecorationUnlock.TotalSpins -> UnlockHint(R.string.unlock_spins, rule.spins)
        is DecorationUnlock.MissionsDone -> UnlockHint(R.string.unlock_missions, rule.missions)
        is DecorationUnlock.BiggestWin -> UnlockHint(R.string.unlock_big_win, rule.coins)
    }
}
