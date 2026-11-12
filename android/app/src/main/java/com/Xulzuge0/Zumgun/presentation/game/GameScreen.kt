package com.Xulzuge0.Zumgun.presentation.game

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.config.GameConfig
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.ChiliRed
import com.Xulzuge0.Zumgun.core.theme.GoldFaint
import com.Xulzuge0.Zumgun.core.theme.GoldHairline
import com.Xulzuge0.Zumgun.core.theme.InsetHighlight
import com.Xulzuge0.Zumgun.core.theme.JadeTick
import com.Xulzuge0.Zumgun.core.theme.LanternGold
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.SheetSurface
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinSurface
import com.Xulzuge0.Zumgun.core.theme.TinTranslucent
import com.Xulzuge0.Zumgun.core.theme.WoodSurface
import com.Xulzuge0.Zumgun.core.ui.AppBackground
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.core.ui.Format
import com.Xulzuge0.Zumgun.core.ui.GoodsMeter
import com.Xulzuge0.Zumgun.core.ui.IconImageButton
import com.Xulzuge0.Zumgun.core.ui.PrimaryButton
import com.Xulzuge0.Zumgun.core.ui.ReelCell
import com.Xulzuge0.Zumgun.core.ui.StatItem
import com.Xulzuge0.Zumgun.core.ui.StatRow
import com.Xulzuge0.Zumgun.presentation.dialog.PauseDialog
import kotlin.math.floor

private const val BULBS = 9
private val CABINET_PADDING = 10.dp
private val CABINET_BORDER = 3.dp
private val CELL_GAP = 6.dp

@Composable
fun GameScreen(
    stallId: Int,
    onRunFinished: () -> Unit,
    onMenu: () -> Unit
) {
    val viewModel: GameViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(stallId) {
        viewModel.beginRun(stallId)
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                GameEvent.RunFinished -> onRunFinished()
                GameEvent.LeftToMenu -> onMenu()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.pause() }
    }

    BackHandler { viewModel.setPaused(true) }

    GameContent(
        state = state,
        onSpin = viewModel::spin,
        onPause = { viewModel.setPaused(true) },
        onResume = { viewModel.setPaused(false) },
        onLeave = viewModel::abandonRun
    )
}

@Composable
private fun GameContent(
    state: GameUiState,
    onSpin: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onLeave: () -> Unit
) {
    AppBackground(
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.78f),
            PlazaBlack.copy(alpha = 0.55f),
            PlazaBlack.copy(alpha = 0.92f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(state = state, onPause = onPause)
            GoodsMeter(
                current = state.goods,
                target = state.targetGoods,
                symbolId = state.featuredSymbolId,
                modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ReelCabinet(state = state)
                WinBanner(
                    visible = state.showBanner && state.lastWin > 0,
                    amount = state.lastWin,
                    modifier = Modifier.align(Alignment.Center)
                )
                NoDealChip(
                    visible = state.noDeal,
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }
            ControlPanel(state = state, onSpin = onSpin)
        }
        if (state.paused) {
            PauseDialog(onResume = onResume, onMenu = onLeave)
        }
    }
}

@Composable
private fun GameHeader(state: GameUiState, onPause: () -> Unit) {
    Surface(
        color = SheetSurface,
        border = BorderStroke(1.dp, GoldHairline.copy(alpha = 0.13f)),
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            IconImageButton(
                iconRes = R.drawable.icon_pause,
                description = stringResource(R.string.cd_pause),
                onClick = onPause
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(AssetMap.stallName(state.stallId)),
                    style = MaterialTheme.typography.titleMedium,
                    color = CandleGold
                )
                Text(
                    text = stringResource(
                        R.string.spin_counter,
                        state.spinIndex,
                        GameConfig.SPINS_PER_RUN
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = Format.grouped(state.balance),
                style = MaterialTheme.typography.titleSmall,
                color = CandleGold,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun ReelCabinet(state: GameUiState) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        val boardMax = minOf(maxWidth, 380.dp)
        val frame = CABINET_PADDING + CABINET_BORDER
        val rawCell = (boardMax - frame * 2 - CELL_GAP * 2) / 3
        val cell = floor(rawCell.value).dp
        val boardWidth = cell * 3 + CELL_GAP * 2 + frame * 2
        Surface(
            shape = RoundedCornerShape(22.dp),
            color = WoodSurface,
            border = BorderStroke(CABINET_BORDER, GoldHairline),
            shadowElevation = 16.dp,
            modifier = Modifier.width(boardWidth)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(CABINET_PADDING)
            ) {
                BulbString(bright = state.lastWin > 0)
                Spacer(modifier = Modifier.height(8.dp))
                ReelGrid(state = state, cell = cell)
            }
        }
    }
}

@Composable
private fun ReelGrid(state: GameUiState, cell: Dp) {
    val gridWidth = cell * 3 + CELL_GAP * 2
    val gridHeight = cell * 3 + CELL_GAP * 2
    Box(modifier = Modifier.size(width = gridWidth, height = gridHeight)) {
        Row(horizontalArrangement = Arrangement.spacedBy(CELL_GAP)) {
            repeat(3) { column ->
                val symbols = listOf(
                    state.grid[column],
                    state.grid[column + 3],
                    state.grid[column + 6]
                )
                val highlighted = buildSet {
                    repeat(3) { row ->
                        if (state.winningCells.contains(row * 3 + column)) add(row)
                    }
                }
                ReelColumn(
                    symbols = symbols,
                    spinning = state.spinningReels.contains(column),
                    cell = cell,
                    highlighted = highlighted,
                    columnIndex = column
                )
            }
        }
        PayLineOverlay(
            cells = state.winningLine,
            cell = cell,
            modifier = Modifier.size(width = gridWidth, height = gridHeight)
        )
    }
}

@Composable
private fun ReelColumn(
    symbols: List<Int>,
    spinning: Boolean,
    cell: Dp,
    highlighted: Set<Int>,
    columnIndex: Int
) {
    val height = cell * 3 + CELL_GAP * 2
    val settle by animateFloatAsState(
        targetValue = if (spinning) 0.97f else 1f,
        animationSpec = spring(dampingRatio = 0.62f, stiffness = Spring.StiffnessMedium),
        label = "reelSettle"
    )
    Box(
        modifier = Modifier
            .size(width = cell, height = height)
            .clip(RoundedCornerShape(12.dp))
            .graphicsLayer {
                scaleY = settle
            }
    ) {
        if (spinning) {
            SpinningStrip(cell = cell, columnIndex = columnIndex)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(CELL_GAP)) {
                symbols.forEachIndexed { row, symbolId ->
                    ReelCell(
                        symbolId = symbolId,
                        cellSize = cell,
                        highlighted = highlighted.contains(row)
                    )
                }
            }
        }
    }
}

@Composable
private fun SpinningStrip(cell: Dp, columnIndex: Int) {
    val strip = remember(columnIndex) {
        val base = listOf(1, 3, 5, 2, 4, 1, 3)
        List(base.size) { index -> base[(index + columnIndex * 2) % base.size] }
    }
    val transition = rememberInfiniteTransition(label = "strip")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 210, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "stripShift"
    )
    Column(
        verticalArrangement = Arrangement.spacedBy(CELL_GAP),
        modifier = Modifier.graphicsLayer {
            val step = (cell + CELL_GAP).toPx()
            translationY = shift * step - step
            alpha = 0.78f
        }
    ) {
        strip.forEach { symbolId ->
            ReelCell(symbolId = symbolId, cellSize = cell, highlighted = false)
        }
    }
}

@Composable
private fun PayLineOverlay(cells: List<Int>, cell: Dp, modifier: Modifier = Modifier) {
    if (cells.size < 2) return
    Canvas(modifier = modifier) {
        val step = (cell + CELL_GAP).toPx()
        val half = cell.toPx() / 2f
        val points = cells.map { index ->
            Offset(
                x = (index % 3) * step + half,
                y = (index / 3) * step + half
            )
        }
        for (i in 0 until points.size - 1) {
            drawLine(
                color = ChiliRed,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun BulbString(bright: Boolean) {
    val alpha by animateFloatAsState(
        targetValue = if (bright) 1f else 0.42f,
        animationSpec = tween(durationMillis = 300),
        label = "bulbs"
    )
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        repeat(BULBS) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(CandleGold.copy(alpha = alpha), CircleShape)
            )
        }
    }
}

@Composable
private fun WinBanner(visible: Boolean, amount: Int, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { full -> full / 3 } + fadeIn(tween(260)),
        exit = fadeOut(tween(220)),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(18.dp),
            color = TinSurface.copy(alpha = 0.94f),
            border = BorderStroke(2.dp, CandleGold),
            shadowElevation = 18.dp
        ) {
            Text(
                text = stringResource(R.string.win_banner, Format.grouped(amount)),
                style = MaterialTheme.typography.displaySmall,
                color = CandleGold,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(horizontal = 22.dp, vertical = 14.dp)
            )
        }
    }
}

@Composable
private fun NoDealChip(visible: Boolean, modifier: Modifier = Modifier) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(140)),
        exit = fadeOut(tween(200)),
        modifier = modifier
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = TinTranslucent,
            border = BorderStroke(1.dp, GoldFaint)
        ) {
            Text(
                text = stringResource(R.string.no_deal),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun ControlPanel(state: GameUiState, onSpin: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = SheetSurface,
        border = BorderStroke(1.dp, GoldFaint),
        shadowElevation = 20.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            StatRow(
                items = listOf(
                    StatItem(GameConfig.BET.toString(), R.string.stat_bet, LanternGold),
                    StatItem(
                        stringResource(R.string.streak_value, Format.tenths(state.streakTenths)),
                        R.string.stat_streak,
                        ChiliRed
                    ),
                    StatItem(
                        stringResource(
                            R.string.stat_fraction,
                            state.charge,
                            GameConfig.GOLDEN_CHARGE_TARGET
                        ),
                        R.string.stat_charge,
                        JadeTick
                    )
                )
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.game_hint),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = stringResource(R.string.disclaimer_mini),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            val label = when {
                state.spinning -> stringResource(R.string.spinning)
                state.goldenReady -> stringResource(R.string.golden_spin)
                else -> stringResource(R.string.spin)
            }
            PrimaryButton(
                text = label,
                onClick = onSpin,
                enabled = !state.spinning,
                height = 62.dp,
                glowing = state.goldenReady,
                stateLabel = if (state.spinning) {
                    stringResource(R.string.state_spinning)
                } else {
                    stringResource(R.string.state_ready)
                }
            )
            Spacer(modifier = Modifier.height(4.dp))
            BulbLine()
        }
    }
}

@Composable
private fun BulbLine() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(InsetHighlight)
    )
}
