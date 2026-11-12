package com.Xulzuge0.Zumgun.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.GoldFaint
import com.Xulzuge0.Zumgun.core.theme.JadeTick
import com.Xulzuge0.Zumgun.core.theme.LanternGold
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.SheetSurface
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinTranslucent
import com.Xulzuge0.Zumgun.core.ui.AppBackground
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.core.ui.Format
import com.Xulzuge0.Zumgun.core.ui.InsetTopLine
import com.Xulzuge0.Zumgun.core.ui.PrimaryButton
import com.Xulzuge0.Zumgun.core.ui.SecondaryButton
import com.Xulzuge0.Zumgun.core.ui.StatItem
import com.Xulzuge0.Zumgun.core.ui.StatRow

@Composable
fun MenuScreen(
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val viewModel: MenuViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    MenuContent(
        state = state,
        onPlay = onPlay,
        onLevels = onLevels,
        onCollection = onCollection,
        onProgress = onProgress,
        onSettings = onSettings
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onPlay: (Int) -> Unit,
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val sheetRise by animateDpAsState(
        targetValue = if (entered) 0.dp else 40.dp,
        animationSpec = tween(durationMillis = 420, easing = FastOutSlowInEasing),
        label = "sheetRise"
    )

    AppBackground(
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.20f),
            Color.Transparent,
            PlazaBlack.copy(alpha = 0.95f)
        )
    ) {
        LanternGlows()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                BalanceBadge(
                    balance = state.balance,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            Surface(
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                color = SheetSurface,
                border = BorderStroke(1.dp, GoldFaint),
                shadowElevation = 18.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = sheetRise)
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    InsetTopLine()
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        BrandHeader()
                        Spacer(modifier = Modifier.height(14.dp))
                        StatRow(items = menuStats(state))
                        Spacer(modifier = Modifier.height(12.dp))
                        SelectedStallStrip(state = state, onClick = onLevels)
                        Spacer(modifier = Modifier.height(16.dp))
                        PrimaryButton(
                            text = stringResource(R.string.play),
                            onClick = { onPlay(state.playStallId) },
                            height = 60.dp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        SecondaryGrid(
                            onLevels = onLevels,
                            onCollection = onCollection,
                            onProgress = onProgress,
                            onSettings = onSettings
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = stringResource(R.string.disclaimer_short),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun menuStats(state: MenuUiState): List<StatItem> {
    val items = mutableListOf(
        StatItem(state.level.toString(), R.string.stat_bazaar_level, LanternGold),
        StatItem(Format.grouped(state.balance), R.string.stat_coins, CandleGold)
    )
    if (state.litStalls > 0) {
        items.add(
            StatItem(
                stringResource(R.string.stat_fraction, state.litStalls, 5),
                R.string.stat_stalls_lit,
                JadeTick
            )
        )
    }
    return items
}

@Composable
private fun BrandHeader() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = stringResource(R.string.cd_logo),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(64.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = stringResource(R.string.app_title),
                style = MaterialTheme.typography.headlineMedium,
                color = CandleGold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun SelectedStallStrip(state: MenuUiState, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = TinTranslucent,
        border = BorderStroke(1.dp, GoldFaint),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Image(
                painter = painterResource(AssetMap.symbolDrawable(state.selectedSymbolId)),
                contentDescription = stringResource(AssetMap.symbolName(state.selectedSymbolId)),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(AssetMap.stallName(state.selectedStallId)),
                style = MaterialTheme.typography.titleSmall,
                color = CandleGold,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(R.string.menu_target, state.selectedTarget),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Composable
private fun SecondaryGrid(
    onLevels: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryButton(
                text = stringResource(R.string.levels),
                onClick = onLevels,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.collection),
                onClick = onCollection,
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            SecondaryButton(
                text = stringResource(R.string.progress),
                onClick = onProgress,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.settings),
                onClick = onSettings,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BalanceBadge(balance: Int, modifier: Modifier = Modifier) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = TinTranslucent,
        border = BorderStroke(1.dp, GoldFaint),
        shadowElevation = 8.dp,
        modifier = modifier.height(40.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.coins_value, Format.grouped(balance)),
                style = MaterialTheme.typography.titleSmall,
                color = CandleGold,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}

@Composable
private fun LanternGlows() {
    val transition = rememberInfiniteTransition(label = "menuGlow")
    val first by transition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowOne"
    )
    val second by transition.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowTwo"
    )
    val third by transition.animateFloat(
        initialValue = 0.50f,
        targetValue = 0.90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowThree"
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Glow(diameter = 140.dp, alpha = first, offsetX = 36.dp, offsetY = 120.dp)
        Glow(diameter = 192.dp, alpha = second, offsetX = 210.dp, offsetY = 60.dp)
        Glow(diameter = 116.dp, alpha = third, offsetX = 150.dp, offsetY = 250.dp)
    }
}

@Composable
private fun Glow(diameter: Dp, alpha: Float, offsetX: Dp, offsetY: Dp) {
    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(diameter)
            .background(
                brush = Brush.radialGradient(
                    listOf(LanternGold.copy(alpha = 0.20f * alpha), Color.Transparent)
                ),
                shape = CircleShape
            )
    )
}

