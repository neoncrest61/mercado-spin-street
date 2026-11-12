package com.Xulzuge0.Zumgun.domain.usecase

import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.domain.model.Symbol
import kotlin.random.Random

class SpinReelsUseCase {

    operator fun invoke(symbols: List<Symbol>, random: Random, pityActive: Boolean): List<Int> {
        val weights = symbols.map { symbol ->
            if (symbol.wild && pityActive) {
                symbol.weight * GameConfig.PITY_WILD_PERMILLE / 1000
            } else {
                symbol.weight
            }
        }
        val total = weights.sum()
        return List(GameConfig.GRID_SIZE) { draw(symbols, weights, total, random) }
    }

    private fun draw(symbols: List<Symbol>, weights: List<Int>, total: Int, random: Random): Int {
        var roll = random.nextInt(total)
        for (index in symbols.indices) {
            roll -= weights[index]
            if (roll < 0) return symbols[index].id
        }
        return symbols[symbols.lastIndex].id
    }
}
