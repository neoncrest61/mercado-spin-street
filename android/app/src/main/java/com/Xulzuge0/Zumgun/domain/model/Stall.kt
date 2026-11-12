package com.Xulzuge0.Zumgun.domain.model

data class Stall(
    val id: Int,
    val featuredSymbolId: Int,
    val targetGoods: Int,
    val requiredLevel: Int,
    val decorationId: Int,
    val rewardCoins: Int
)
