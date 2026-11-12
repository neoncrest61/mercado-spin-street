package com.Xulzuge0.Zumgun.domain.usecase

import com.Xulzuge0.Zumgun.domain.model.LineWin
import com.Xulzuge0.Zumgun.domain.model.PayLines
import com.Xulzuge0.Zumgun.domain.model.SpinEvaluation
import com.Xulzuge0.Zumgun.domain.model.Symbol

class EvaluatePaylinesUseCase {

    operator fun invoke(grid: List<Int>, symbols: List<Symbol>, bet: Int): SpinEvaluation {
        val byId = symbols.associateBy { it.id }
        val wins = mutableListOf<LineWin>()
        PayLines.LINES.forEachIndexed { lineIndex, cells ->
            val drawn = cells.map { byId.getValue(grid[it]) }
            val triple = resolveTriple(drawn)
            if (triple != null) {
                wins.add(LineWin(lineIndex, triple.id, triple.tripleMultiplier, cells))
                return@forEachIndexed
            }
            val pair = resolvePair(drawn)
            if (pair != null) {
                wins.add(LineWin(lineIndex, pair.id, pair.pairMultiplier, cells.take(2)))
            }
        }
        val baseWin = wins.sumOf { bet * it.multiplier }
        return SpinEvaluation(wins, baseWin)
    }

    private fun resolveTriple(drawn: List<Symbol>): Symbol? {
        val anchor = drawn.firstOrNull { !it.wild } ?: return drawn.first()
        val matches = drawn.all { it.wild || it.id == anchor.id }
        return if (matches) anchor else null
    }

    private fun resolvePair(drawn: List<Symbol>): Symbol? {
        val first = drawn[0]
        val second = drawn[1]
        val anchor = when {
            !first.wild -> first
            !second.wild -> second
            else -> first
        }
        if (anchor.pairMultiplier <= 0) return null
        val matches = (first.wild || first.id == anchor.id) && (second.wild || second.id == anchor.id)
        return if (matches) anchor else null
    }
}
