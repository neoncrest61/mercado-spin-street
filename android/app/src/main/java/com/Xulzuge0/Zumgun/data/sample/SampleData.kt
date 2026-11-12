package com.Xulzuge0.Zumgun.data.sample

import com.Xulzuge0.Zumgun.domain.model.Decoration
import com.Xulzuge0.Zumgun.domain.model.DecorationSet
import com.Xulzuge0.Zumgun.domain.model.DecorationUnlock
import com.Xulzuge0.Zumgun.domain.model.Stall
import com.Xulzuge0.Zumgun.domain.model.Symbol

object SampleData {

    const val SYMBOL_CHILI_GARLAND = 1
    const val SYMBOL_PAPEL_PICADO = 2
    const val SYMBOL_FRUIT_CRATE = 3
    const val SYMBOL_TALAVERA_POT = 4
    const val SYMBOL_GOLDEN_LANTERN = 5

    val symbols: List<Symbol> = listOf(
        Symbol(SYMBOL_CHILI_GARLAND, weight = 30, tripleMultiplier = 2, pairMultiplier = 0, wild = false),
        Symbol(SYMBOL_PAPEL_PICADO, weight = 26, tripleMultiplier = 3, pairMultiplier = 0, wild = false),
        Symbol(SYMBOL_FRUIT_CRATE, weight = 20, tripleMultiplier = 5, pairMultiplier = 0, wild = false),
        Symbol(SYMBOL_TALAVERA_POT, weight = 14, tripleMultiplier = 8, pairMultiplier = 1, wild = false),
        Symbol(SYMBOL_GOLDEN_LANTERN, weight = 10, tripleMultiplier = 15, pairMultiplier = 2, wild = true)
    )

    val stalls: List<Stall> = listOf(
        Stall(1, SYMBOL_CHILI_GARLAND, targetGoods = 12, requiredLevel = 1, decorationId = 7, rewardCoins = 120),
        Stall(2, SYMBOL_PAPEL_PICADO, targetGoods = 11, requiredLevel = 2, decorationId = 4, rewardCoins = 140),
        Stall(3, SYMBOL_FRUIT_CRATE, targetGoods = 9, requiredLevel = 3, decorationId = 8, rewardCoins = 160),
        Stall(4, SYMBOL_TALAVERA_POT, targetGoods = 7, requiredLevel = 4, decorationId = 10, rewardCoins = 180),
        Stall(5, SYMBOL_GOLDEN_LANTERN, targetGoods = 5, requiredLevel = 5, decorationId = 1, rewardCoins = 200)
    )

    val decorations: List<Decoration> = listOf(
        Decoration(1, DecorationSet.LANTERNS, SYMBOL_GOLDEN_LANTERN, DecorationUnlock.StallLit(5)),
        Decoration(2, DecorationSet.LANTERNS, SYMBOL_GOLDEN_LANTERN, DecorationUnlock.BazaarLevel(3)),
        Decoration(3, DecorationSet.LANTERNS, SYMBOL_GOLDEN_LANTERN, DecorationUnlock.TotalSpins(40)),
        Decoration(4, DecorationSet.BANNERS, SYMBOL_PAPEL_PICADO, DecorationUnlock.StallLit(2)),
        Decoration(5, DecorationSet.BANNERS, SYMBOL_PAPEL_PICADO, DecorationUnlock.BazaarLevel(2)),
        Decoration(6, DecorationSet.BANNERS, SYMBOL_PAPEL_PICADO, DecorationUnlock.MissionsDone(4)),
        Decoration(7, DecorationSet.GOODS, SYMBOL_CHILI_GARLAND, DecorationUnlock.StallLit(1)),
        Decoration(8, DecorationSet.GOODS, SYMBOL_FRUIT_CRATE, DecorationUnlock.StallLit(3)),
        Decoration(9, DecorationSet.GOODS, SYMBOL_FRUIT_CRATE, DecorationUnlock.BazaarLevel(4)),
        Decoration(10, DecorationSet.PLAZA, SYMBOL_TALAVERA_POT, DecorationUnlock.StallLit(4)),
        Decoration(11, DecorationSet.PLAZA, SYMBOL_TALAVERA_POT, DecorationUnlock.BazaarLevel(5)),
        Decoration(12, DecorationSet.PLAZA, SYMBOL_TALAVERA_POT, DecorationUnlock.BiggestWin(1000))
    )

    fun stallById(id: Int): Stall = stalls.firstOrNull { it.id == id } ?: stalls.first()

    fun decorationById(id: Int): Decoration? = decorations.firstOrNull { it.id == id }
}
