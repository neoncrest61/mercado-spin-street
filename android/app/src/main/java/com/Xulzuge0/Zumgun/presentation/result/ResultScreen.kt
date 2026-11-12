package com.Xulzuge0.Zumgun.presentation.result

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.ChiliRed
import com.Xulzuge0.Zumgun.core.theme.GoldHairline
import com.Xulzuge0.Zumgun.core.theme.JadeTick
import com.Xulzuge0.Zumgun.core.theme.LanternGold
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinSurface
import com.Xulzuge0.Zumgun.core.ui.AppBackground
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.core.ui.Format
import com.Xulzuge0.Zumgun.core.ui.PrimaryButton
import com.Xulzuge0.Zumgun.core.ui.SecondaryButton
import com.Xulzuge0.Zumgun.core.ui.StatItem
import com.Xulzuge0.Zumgun.core.ui.StatRow
import com.Xulzuge0.Zumgun.core.ui.WoodCard
import com.Xulzuge0.Zumgun.domain.model.RunOutcome
import kotlin.math.cos
import kotlin.math.sin

private const val CONFETTI_DOTS = 14
private const val TWO_PI = 6.2831855f

@Composable
fun ResultScreen(
    onSpinAgain: (Int) -> Unit,
    onMenu: () -> Unit,
    onCollection: () -> Unit
) {
    val viewModel: ResultViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResultContent(
        state = state,
        onSpinAgain = { onSpinAgain(state.stallId) },
        onMenu = onMenu,
        onCollection = onCollection,
        onGrant = viewModel::grantCoins
    )
}

@Composable
private fun ResultContent(
    state: ResultUiState,
    onSpinAgain: () -> Unit,
    onMenu: () -> Unit,
    onCollection: () -> Unit,
    onGrant: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val rise by animateDpAsState(
        targetValue = if (entered) 0.dp else 24.dp,
        animationSpec = tween(durationMillis = 360, easing = FastOutSlowInEasing),
        label = "resultRise"
    )
    val burst by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "confetti"
    )
    val accent = when (state.outcome) {
        RunOutcome.STALL_LIT -> CandleGold
        RunOutcome.MISSION_MISSED -> ChiliRed
        RunOutcome.OUT_OF_COINS -> LanternGold
    }

    AppBackground(
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.93f),
            PlazaBlack.copy(alpha = 0.88f),
            PlazaBlack.copy(alpha = 0.97f)
        )
    ) {
        if (state.outcome == RunOutcome.STALL_LIT && state.fullCelebration) {
            Confetti(progress = burst, modifier = Modifier.fillMaxSize())
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 22.dp)
                .offset(y = rise)
        ) {
            Text(
                text = stringResource(outcomeTitle(state.outcome)),
                style = MaterialTheme.typography.displaySmall,
                color = accent,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutcomeSubtitle(state = state)
            Spacer(modifier = Modifier.height(20.dp))
            Surface(
                shape = CircleShape,
                color = TinSurface.copy(alpha = 0.85f),
                border = BorderStroke(2.dp, GoldHairline),
                shadowElevation = 14.dp,
                modifier = Modifier.size(146.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(AssetMap.symbolDrawable(state.symbolId)),
                        contentDescription = stringResource(AssetMap.symbolName(state.symbolId)),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(110.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(22.dp))
            StatRow(
                items = listOf(
                    StatItem(Format.grouped(state.coinsWon), R.string.stat_won, CandleGold),
                    StatItem(
                        stringResource(R.string.stat_fraction, state.goods, state.target),
                        R.string.stat_goods,
                        JadeTick
                    ),
                    StatItem(
                        stringResource(
                            R.string.streak_value,
                            Format.tenths(state.bestStreakTenths)
                        ),
                        R.string.stat_best_streak,
                        ChiliRed
                    )
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.market_seed, state.seed),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            if (state.decorationId != 0) {
                Spacer(modifier = Modifier.height(14.dp))
                RewardRow(decorationId = state.decorationId, xp = state.xpGained)
            }
            Spacer(modifier = Modifier.height(24.dp))
            if (state.outcome == RunOutcome.OUT_OF_COINS) {
                PrimaryButton(
                    text = stringResource(R.string.market_grant),
                    onClick = onGrant,
                    height = 58.dp
                )
                Spacer(modifier = Modifier.height(10.dp))
            }
            PrimaryButton(
                text = stringResource(R.string.spin_again),
                onClick = onSpinAgain,
                enabled = state.canReplay,
                height = 58.dp
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryButton(
                    text = stringResource(R.string.menu),
                    onClick = onMenu,
                    leadingIcon = R.drawable.icon_home,
                    modifier = Modifier.weight(1f)
                )
                if (state.decorationId != 0) {
                    SecondaryButton(
                        text = stringResource(R.string.collection),
                        onClick = onCollection,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.disclaimer_short),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun outcomeTitle(outcome: RunOutcome): Int = when (outcome) {
    RunOutcome.STALL_LIT -> R.string.result_lit
    RunOutcome.MISSION_MISSED -> R.string.result_missed
    RunOutcome.OUT_OF_COINS -> R.string.result_broke
}

@Composable
private fun OutcomeSubtitle(state: ResultUiState) {
    val text = when (state.outcome) {
        RunOutcome.STALL_LIT -> stringResource(
            R.string.result_lit_sub,
            stringResource(AssetMap.stallName(state.stallId))
        )
        RunOutcome.MISSION_MISSED -> stringResource(
            R.string.result_missed_sub,
            state.target,
            state.goods
        )
        RunOutcome.OUT_OF_COINS -> stringResource(R.string.result_broke_sub)
    }
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = TextSecondary,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun RewardRow(decorationId: Int, xp: Int) {
    val pop by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMediumLow),
        label = "rewardPop"
    )
    WoodCard(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = 0.8f + 0.2f * pop
                scaleY = 0.8f + 0.2f * pop
            }
    ) {
        Text(
            text = stringResource(
                R.string.reward_decoration,
                stringResource(AssetMap.decorationName(decorationId))
            ),
            style = MaterialTheme.typography.titleSmall,
            color = CandleGold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.reward_xp, xp),
            style = MaterialTheme.typography.labelSmall,
            color = JadeTick
        )
    }
}

@Composable
private fun Confetti(progress: Float, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val radius = size.minDimension * 0.42f * progress
        repeat(CONFETTI_DOTS) { index ->
            val angle = TWO_PI * index / CONFETTI_DOTS
            val color = if (index % 2 == 0) CandleGold else ChiliRed
            drawCircle(
                color = color,
                radius = 5f,
                center = Offset(
                    x = center.x + cos(angle) * radius,
                    y = center.y + sin(angle) * radius * 0.7f
                ),
                alpha = (1f - progress).coerceIn(0f, 1f)
            )
        }
    }
}

