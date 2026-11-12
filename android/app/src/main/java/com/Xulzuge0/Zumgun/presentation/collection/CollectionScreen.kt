package com.Xulzuge0.Zumgun.presentation.collection

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.GoldFaint
import com.Xulzuge0.Zumgun.core.theme.JadeTick
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinTranslucent
import com.Xulzuge0.Zumgun.core.theme.WoodSurface
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.core.ui.ScreenScaffold
import com.Xulzuge0.Zumgun.core.ui.SecondaryButton
import com.Xulzuge0.Zumgun.core.ui.WoodCard

@Composable
fun CollectionScreen(
    onBack: () -> Unit,
    onLevels: () -> Unit
) {
    val viewModel: CollectionViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectionContent(state = state, onBack = onBack, onLevels = onLevels)
}

@Composable
private fun CollectionContent(
    state: CollectionUiState,
    onBack: () -> Unit,
    onLevels: () -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.collection_title),
        onBack = onBack,
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.80f),
            PlazaBlack.copy(alpha = 0.90f),
            PlazaBlack.copy(alpha = 0.97f)
        ),
        trailing = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = TinTranslucent,
                border = BorderStroke(1.dp, GoldFaint)
            ) {
                Text(
                    text = stringResource(R.string.stat_fraction, state.unlocked, state.total),
                    style = MaterialTheme.typography.labelSmall,
                    color = CandleGold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.unlocked == 0) {
                WoodCard(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.collection_empty),
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
            } else {
                Text(
                    text = stringResource(R.string.active_bonus, state.bonusPercent),
                    style = MaterialTheme.typography.labelLarge,
                    color = JadeTick
                )
            }
            state.groups.forEach { group ->
                DecorationGroupBlock(group = group)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun DecorationGroupBlock(group: DecorationGroupUi) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (group.complete) {
                stringResource(
                    R.string.set_header_active,
                    stringResource(AssetMap.setName(group.group)),
                    group.ownedCount,
                    group.tiles.size
                )
            } else {
                stringResource(
                    R.string.set_header_idle,
                    stringResource(AssetMap.setName(group.group)),
                    group.ownedCount,
                    group.tiles.size
                )
            },
            style = MaterialTheme.typography.labelLarge,
            color = if (group.complete) JadeTick else TextSecondary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            group.tiles.forEach { tile ->
                DecorationTileCard(tile = tile, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun DecorationTileCard(tile: DecorationTile, modifier: Modifier = Modifier) {
    val label = if (tile.unlocked) {
        stringResource(R.string.state_unlocked)
    } else {
        stringResource(R.string.state_locked)
    }
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = WoodSurface,
        border = BorderStroke(1.dp, if (tile.unlocked) GoldFaint else TinTranslucent),
        modifier = modifier.semantics { stateDescription = label }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(54.dp)
            ) {
                Image(
                    painter = painterResource(AssetMap.symbolDrawable(tile.symbolId)),
                    contentDescription = stringResource(AssetMap.decorationName(tile.id)),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .size(54.dp)
                        .alpha(if (tile.unlocked) 1f else 0.18f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(AssetMap.decorationName(tile.id)),
                style = MaterialTheme.typography.labelSmall,
                color = if (tile.unlocked) CandleGold else TextMuted,
                textAlign = TextAlign.Center
            )
            if (!tile.unlocked) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(tile.hintRes, tile.hintAmount),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
