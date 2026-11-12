package com.Xulzuge0.Zumgun.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.data.sample.SampleData
import com.Xulzuge0.Zumgun.domain.model.BonusRules
import com.Xulzuge0.Zumgun.domain.model.RunTally
import com.Xulzuge0.Zumgun.domain.model.Stall
import com.Xulzuge0.Zumgun.domain.repository.ProgressRepository
import com.Xulzuge0.Zumgun.domain.repository.SettingsRepository
import com.Xulzuge0.Zumgun.domain.usecase.ApplyRunResultUseCase
import com.Xulzuge0.Zumgun.domain.usecase.EvaluatePaylinesUseCase
import com.Xulzuge0.Zumgun.domain.usecase.SpinReelsUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val spinReels: SpinReelsUseCase,
    private val evaluatePaylines: EvaluatePaylinesUseCase,
    private val applyRunResult: ApplyRunResultUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<GameEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var spinJob: Job? = null
    private var random: Random = Random(DEFAULT_SEED)
    private var stall: Stall = SampleData.stalls.first()
    private var seed: Long = DEFAULT_SEED
    private var consecutiveLosses = 0
    private var bigWinCooldown = 0
    private var coinsWon = 0
    private var winningSpins = 0
    private var biggestWin = 0
    private var bestStreakTenths = GameConfig.STREAK_BASE_TENTHS
    private var finished = false

    fun beginRun(stallId: Int) {
        spinJob?.cancel()
        val progress = progressRepository.progress.value
        val settings = settingsRepository.settings.value
        stall = SampleData.stallById(stallId)
        seed = stallId * SEED_PRIME_A xor (progress.runCount + 1) * SEED_PRIME_B
        random = Random(seed)
        consecutiveLosses = 0
        bigWinCooldown = 0
        coinsWon = 0
        winningSpins = 0
        biggestWin = 0
        bestStreakTenths = GameConfig.STREAK_BASE_TENTHS
        finished = false
        if (progress.selectedStallId != stallId) {
            progressRepository.update { it.copy(selectedStallId = stallId) }
        }
        _state.value = GameUiState(
            stallId = stall.id,
            targetGoods = stall.targetGoods,
            featuredSymbolId = stall.featuredSymbolId,
            balance = progress.balance,
            grid = spinReels(SampleData.symbols, random, false),
            payLineHighlight = settings.payLineHighlight,
            fullCelebration = settings.fullCelebration,
            speedPermille = settings.reelSpeed.permille,
            setBonusPercent = BonusRules.setBonusPercent(
                progress.unlockedDecorations,
                SampleData.decorations
            )
        )
    }

    fun spin() {
        if (finished) return
        val current = _state.value
        if (current.spinning || current.paused) return
        if (current.balance < GameConfig.BET) {
            endRun(outOfCoins = true)
            return
        }
        spinJob?.cancel()
        spinJob = viewModelScope.launch { runSpin() }
    }

    fun setPaused(paused: Boolean) {
        _state.update { it.copy(paused = paused) }
    }

    fun abandonRun() {
        finished = true
        spinJob?.cancel()
        _state.update { it.copy(paused = false, spinning = false, spinningReels = emptySet()) }
        viewModelScope.launch { _events.send(GameEvent.LeftToMenu) }
    }

    fun pause() {
        spinJob?.cancel()
        _state.update { it.copy(spinning = false, spinningReels = emptySet()) }
    }

    private suspend fun runSpin() {
        val speed = _state.value.speedPermille
        val stopOne = GameConfig.REEL_STOP_ONE_MS * speed / 1000
        val stopTwo = GameConfig.REEL_STOP_TWO_MS * speed / 1000
        val stopThree = GameConfig.REEL_STOP_THREE_MS * speed / 1000
        val golden = _state.value.goldenReady
        val afterBet = _state.value.balance - GameConfig.BET
        progressRepository.update { it.copy(balance = afterBet) }
        val pity = consecutiveLosses >= GameConfig.PITY_LOSS_THRESHOLD && bigWinCooldown == 0
        val grid = spinReels(SampleData.symbols, random, pity)
        _state.update {
            it.copy(
                balance = afterBet,
                spinIndex = it.spinIndex + 1,
                spinning = true,
                spinningReels = setOf(0, 1, 2),
                winningCells = emptySet(),
                winningLine = emptyList(),
                showBanner = false,
                noDeal = false,
                lastWin = 0,
                grid = grid
            )
        }
        delay(stopOne)
        _state.update { it.copy(spinningReels = setOf(1, 2)) }
        delay(stopTwo - stopOne)
        _state.update { it.copy(spinningReels = setOf(2)) }
        delay(stopThree - stopTwo)
        _state.update { it.copy(spinningReels = emptySet()) }
        resolve(grid, golden)
    }

    private suspend fun resolve(grid: List<Int>, golden: Boolean) {
        val evaluation = evaluatePaylines(grid, SampleData.symbols, GameConfig.BET)
        val goodsLanded = grid.count { it == stall.featuredSymbolId }
        val current = _state.value
        val streak = current.streakTenths
        var total = 0
        if (evaluation.baseWin > 0) {
            var value = evaluation.baseWin.toLong() * streak / 10
            if (golden) value *= GameConfig.GOLDEN_MULTIPLIER
            value = value * (100 + current.setBonusPercent) / 100
            total = value.toInt()
        }
        val nextStreak = if (total > 0) {
            minOf(streak + GameConfig.STREAK_STEP_TENTHS, GameConfig.STREAK_MAX_TENTHS)
        } else {
            GameConfig.STREAK_BASE_TENTHS
        }
        val chargeBase = if (golden) 0 else current.charge
        val charge = minOf(chargeBase + goodsLanded, GameConfig.GOLDEN_CHARGE_TARGET)
        val newBalance = current.balance + total
        if (total > 0) {
            progressRepository.update { it.copy(balance = newBalance) }
            coinsWon += total
            winningSpins += 1
            biggestWin = maxOf(biggestWin, total)
            bestStreakTenths = maxOf(bestStreakTenths, nextStreak)
            consecutiveLosses = 0
            if (total >= GameConfig.BET * GameConfig.BIG_WIN_MULTIPLIER) {
                bigWinCooldown = GameConfig.BIG_WIN_COOLDOWN_SPINS
            }
        } else {
            consecutiveLosses += 1
        }
        if (bigWinCooldown > 0) bigWinCooldown -= 1
        _state.update {
            it.copy(
                balance = newBalance,
                goods = it.goods + goodsLanded,
                lastWin = total,
                winningCells = if (it.payLineHighlight) evaluation.winningCells else emptySet(),
                winningLine = if (it.payLineHighlight) {
                    evaluation.lineWins.firstOrNull()?.cells.orEmpty()
                } else {
                    emptyList()
                },
                showBanner = total > 0,
                noDeal = total == 0,
                streakTenths = nextStreak,
                charge = charge,
                goldenReady = charge >= GameConfig.GOLDEN_CHARGE_TARGET,
                spinning = false
            )
        }
        delay(if (total > 0) GameConfig.WIN_BANNER_MS else GameConfig.NO_DEAL_MS)
        _state.update { it.copy(showBanner = false, noDeal = false) }
        val after = _state.value
        if (after.spinIndex >= GameConfig.SPINS_PER_RUN || after.balance < GameConfig.BET) {
            delay(GameConfig.RUN_END_DELAY_MS)
            endRun(outOfCoins = after.balance < GameConfig.BET)
        }
    }

    private fun endRun(outOfCoins: Boolean) {
        if (finished) return
        finished = true
        val progress = progressRepository.progress.value
        val current = _state.value
        val tally = RunTally(
            stallId = stall.id,
            goods = current.goods,
            coinsWon = coinsWon,
            spins = current.spinIndex,
            winningSpins = winningSpins,
            biggestWin = biggestWin,
            bestStreakTenths = bestStreakTenths,
            seed = seed,
            outOfCoins = outOfCoins
        )
        val application = applyRunResult(progress, stall, SampleData.decorations, tally)
        progressRepository.update { application.progress }
        progressRepository.publishRun(application.result)
        viewModelScope.launch { _events.send(GameEvent.RunFinished) }
    }

    private companion object {
        const val DEFAULT_SEED = 48213L
        const val SEED_PRIME_A = 7919L
        const val SEED_PRIME_B = 104729L
    }
}
