package com.Xulzuge0.Zumgun.domain.model

enum class DecorationSet { LANTERNS, BANNERS, GOODS, PLAZA }

sealed interface DecorationUnlock {
    data class StallLit(val stallId: Int) : DecorationUnlock
    data class BazaarLevel(val level: Int) : DecorationUnlock
    data class TotalSpins(val spins: Int) : DecorationUnlock
    data class MissionsDone(val missions: Int) : DecorationUnlock
    data class BiggestWin(val coins: Int) : DecorationUnlock
}

data class Decoration(
    val id: Int,
    val group: DecorationSet,
    val spriteSymbolId: Int,
    val unlock: DecorationUnlock
)
