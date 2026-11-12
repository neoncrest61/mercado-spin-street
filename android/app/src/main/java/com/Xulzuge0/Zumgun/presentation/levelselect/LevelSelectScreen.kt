package com.Xulzuge0.Zumgun.presentation.levelselect

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.ChiliRed
import com.Xulzuge0.Zumgun.core.theme.GoldFaint
import com.Xulzuge0.Zumgun.core.theme.JadeTick
import com.Xulzuge0.Zumgun.core.theme.LanternGold
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinTranslucent
import com.Xulzuge0.Zumgun.core.theme.WoodSurface
import com.Xulzuge0.Zumgun.core.ui.AssetMap
import com.Xulzuge0.Zumgun.core.ui.PrimaryButton
import com.Xulzuge0.Zumgun.core.ui.ScreenScaffold

@Composable
fun LevelSelectScreen(
    onBack: () -> Unit,
    onStart: (Int) -> Unit
) {
    val viewModel: LevelSelectViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    LevelSelectContent(
        state = state,
        onBack = onBack,
        onSelect = viewModel::select,
        onStart = onStart
    )
}

@Composable
private fun LevelSelectContent(
    state: LevelSelectUiState,
    onBack: () -> Unit,
    onSelect: (Int) -> Unit,
    onStart: (Int) -> Unit
) {
    ScreenScaffold(
        title = stringResource(R.string.level_select_title),
        onBack = onBack,
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.72f),
            PlazaBlack.copy(alpha = 0.90f),
            PlazaBlack.copy(alpha = 0.96f)
        ),
        trailing = {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = TinTranslucent,
                border = BorderStroke(1.dp, GoldFaint)
            ) {
                Text(
                    text = stringResource(R.string.bazaar_level, state.level),
                    style = MaterialTheme.typography.labelSmall,
                    color = CandleGold,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            state.stalls.forEach { stall ->
                StallCard(
                    stall = stall,
                    allLit = state.allLit,
                    onSelect = { onSelect(stall.id) },
                    onStart = { onStart(stall.id) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        Text(
            text = stringResource(R.string.disclaimer_short),
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        )
    }
}

@Composable
private fun StallCard(
    stall: StallRow,
    allLit: Boolean,
    onSelect: () -> Unit,
    onStart: () -> Unit
) {
    val accent = if (stall.lit) JadeTick else LanternGold
    val borderColor = if (stall.selected) CandleGold else GoldFaint
    val stateLabel = when {
        !stall.unlocked -> stringResource(R.string.state_locked)
        stall.selected -> stringResource(R.string.state_selected)
        else -> stringResource(R.string.state_available)
    }
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = WoodSurface,
        border = BorderStroke(if (stall.selected) 2.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (stall.unlocked) 1f else 0.45f)
            .shadow(
                elevation = if (stall.selected) 10.dp else 4.dp,
                shape = RoundedCornerShape(18.dp),
                ambientColor = LanternGold,
                spotColor = CandleGold
            )
            .then(
                if (stall.unlocked) Modifier.clickable(onClick = onSelect) else Modifier
            )
            .semantics { stateDescription = stateLabel }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accent)
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(AssetMap.symbolDrawable(stall.symbolId)),
                        contentDescription = stringResource(AssetMap.symbolName(stall.symbolId)),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(56.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(AssetMap.stallName(stall.id)),
                            style = MaterialTheme.typography.titleMedium,
                            color = if (stall.lit) JadeTick else CandleGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.stall_brief, stall.target),
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        StallHint(stall = stall, allLit = allLit)
                    }
                }
                if (stall.selected && stall.unlocked) {
                    Spacer(modifier = Modifier.height(10.dp))
                    PrimaryButton(
                        text = stringResource(R.string.start_run),
                        onClick = onStart,
                        height = 48.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun StallHint(stall: StallRow, allLit: Boolean) {
    val text: String
    val color: Color
    when {
        !stall.unlocked -> {
            text = stringResource(R.string.stall_locked_hint, stall.requiredLevel)
            color = ChiliRed
        }
        allLit -> {
            text = stringResource(R.string.stall_all_lit_hint)
            color = JadeTick
        }
        stall.lit -> {
            text = stringResource(R.string.stall_lit_hint)
            color = JadeTick
        }
        else -> {
            text = stringResource(
                R.string.stall_reward_hint,
                stringResource(AssetMap.decorationName(stall.decorationId))
            )
            color = TextMuted
        }
    }
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color
    )
}
