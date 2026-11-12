package com.Xulzuge0.Zumgun.domain.model

data class LineWin(
    val lineIndex: Int,
    val symbolId: Int,
    val multiplier: Int,
    val cells: List<Int>
)

data class SpinEvaluation(
    val lineWins: List<LineWin>,
    val baseWin: Int
) {
    val winningCells: Set<Int> get() = lineWins.flatMap { it.cells }.toSet()
}
