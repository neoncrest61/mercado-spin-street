package com.Xulzuge0.Zumgun.presentation.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
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
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinSurface
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.core.ui.Format
import com.Xulzuge0.Zumgun.core.ui.ScreenScaffold
import com.Xulzuge0.Zumgun.core.ui.SecondaryButton
import com.Xulzuge0.Zumgun.core.ui.StatItem
import com.Xulzuge0.Zumgun.core.ui.StatRow
import com.Xulzuge0.Zumgun.core.ui.WoodCard

@Composable
fun ProgressScreen(
    onBack: () -> Unit,
    onLevels: () -> Unit
) {
    val viewModel: ProgressViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    ProgressContent(state = state, onBack = onBack, onLevels = onLevels)
}

@Composable
private fun ProgressContent(
    state: ProgressUiState,
    onBack: () -> Unit,
    onLevels: () -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.progress_title),
        onBack = onBack,
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.76f),
            PlazaBlack.copy(alpha = 0.90f),
            PlazaBlack.copy(alpha = 0.96f)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            LevelCard(state = state)
            StreetStrip(state = state)
            if (state.hasRuns) {
                StatGrid(state = state)
                Text(
                    text = stringResource(R.string.active_bonus, state.bonusPercent),
                    style = MaterialTheme.typography.labelLarge,
                    color = JadeTick
                )
            } else {
                WoodCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.progress_empty),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    SecondaryButton(
                        text = stringResource(R.string.levels),
                        onClick = onLevels
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun LevelCard(state: ProgressUiState) {
    WoodCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = TinSurface,
                border = BorderStroke(2.dp, GoldHairline),
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = state.level.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        color = CandleGold
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(
                        R.string.level_xp,
                        state.level,
                        state.xpIntoLevel,
                        state.xpPerLevel
                    ),
                    style = MaterialTheme.typography.titleSmall,
                    color = CandleGold,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { state.xpFraction },
                    color = LanternGold,
                    trackColor = TinSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                )
            }
        }
    }
}

@Composable
private fun StreetStrip(state: ProgressUiState) {
    WoodCard(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.street_strip),
            style = MaterialTheme.typography.labelLarge,
            color = TextSecondary
        )
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            state.slots.forEach { slot ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = TinSurface,
                    border = BorderStroke(1.dp, if (slot.lit) GoldHairline else TinSurface),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(62.dp)
                    ) {
                        Image(
                            painter = painterResource(AssetMap.symbolDrawable(slot.symbolId)),
                            contentDescription = stringResource(AssetMap.stallName(slot.stallId)),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(40.dp)
                                .alpha(if (slot.lit) 1f else 0.20f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StatGrid(state: ProgressUiState) {
    val items = buildList {
        if (state.totalSpins > 0) {
            add(StatItem(Format.grouped(state.totalSpins), R.string.stat_spins, LanternGold))
        }
        if (state.missions > 0) {
            add(StatItem(state.missions.toString(), R.string.stat_missions, JadeTick))
        }
        if (state.biggestWin > 0) {
            add(StatItem(Format.grouped(state.biggestWin), R.string.stat_biggest_win, CandleGold))
        }
        if (state.bestStreakTenths > 10) {
            add(
                StatItem(
                    stringResource(R.string.streak_value, Format.tenths(state.bestStreakTenths)),
                    R.string.stat_best_streak,
                    ChiliRed
                )
            )
        }
        if (state.hitRate > 0) {
            add(StatItem(stringResource(R.string.percent_value, state.hitRate), R.string.stat_hit_rate, JadeTick))
        }
        if (state.coinsEarned > 0) {
            add(StatItem(Format.grouped(state.coinsEarned), R.string.stat_coins_earned, CandleGold))
        }
    }
    val rows = packRows(items)
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        rows.forEach { row ->
            StatRow(items = row)
        }
    }
}

private fun packRows(items: List<StatItem>): List<List<StatItem>> {
    val rows = mutableListOf<List<StatItem>>()
    var index = 0
    while (items.size - index >= 2) {
        val remaining = items.size - index
        val take = if (remaining == 3) 3 else 2
        rows.add(items.subList(index, index + take))
        index += take
    }
    return rows
}
