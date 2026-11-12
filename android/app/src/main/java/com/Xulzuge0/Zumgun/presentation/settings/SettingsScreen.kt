package com.Xulzuge0.Zumgun.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.Xulzuge0.Zumgun.core.theme.ChiliRed
import com.Xulzuge0.Zumgun.core.theme.GoldFaint
import com.Xulzuge0.Zumgun.core.theme.LanternGold
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextPrimary
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinSurface
import com.Xulzuge0.Zumgun.core.theme.TinTranslucent
import com.Xulzuge0.Zumgun.core.ui.ScreenScaffold
import com.Xulzuge0.Zumgun.core.ui.WoodCard
import com.Xulzuge0.Zumgun.domain.model.ReelSpeed
import com.Xulzuge0.Zumgun.domain.model.StartingStall
import com.Xulzuge0.Zumgun.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val viewModel: SettingsViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()

    SettingsContent(
        state = state,
        onBack = onBack,
        onSpeed = viewModel::setReelSpeed,
        onPayLine = viewModel::setPayLineHighlight,
        onCelebration = viewModel::setFullCelebration,
        onStartingStall = viewModel::setStartingStall,
        onReset = viewModel::resetBazaar
    )
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBack: () -> Unit,
    onSpeed: (ReelSpeed) -> Unit,
    onPayLine: (Boolean) -> Unit,
    onCelebration: (Boolean) -> Unit,
    onStartingStall: (StartingStall) -> Unit,
    onReset: () -> Unit
) {
    var confirming by rememberSaveable { mutableStateOf(false) }

    ScreenScaffold(
        title = stringResource(R.string.settings_title),
        onBack = onBack,
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.82f),
            PlazaBlack.copy(alpha = 0.92f),
            PlazaBlack.copy(alpha = 0.97f)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            WoodCard(modifier = Modifier.fillMaxWidth()) {
                SettingLabel(titleRes = R.string.setting_speed, hintRes = R.string.setting_speed_hint)
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedRow(
                    labels = listOf(R.string.speed_relaxed, R.string.speed_normal, R.string.speed_quick),
                    selectedIndex = state.reelSpeed.ordinal,
                    onSelect = { index -> onSpeed(ReelSpeed.entries[index]) }
                )
            }
            WoodCard(modifier = Modifier.fillMaxWidth()) {
                ToggleRow(
                    titleRes = R.string.setting_payline,
                    hintRes = R.string.setting_payline_hint,
                    checked = state.payLineHighlight,
                    onChange = onPayLine
                )
            }
            WoodCard(modifier = Modifier.fillMaxWidth()) {
                ToggleRow(
                    titleRes = R.string.setting_celebration,
                    hintRes = R.string.setting_celebration_hint,
                    checked = state.fullCelebration,
                    onChange = onCelebration
                )
            }
            WoodCard(modifier = Modifier.fillMaxWidth()) {
                SettingLabel(
                    titleRes = R.string.setting_starting_stall,
                    hintRes = R.string.setting_starting_stall_hint
                )
                Spacer(modifier = Modifier.height(10.dp))
                SegmentedRow(
                    labels = listOf(R.string.start_last_played, R.string.start_always_first),
                    selectedIndex = state.startingStall.ordinal,
                    onSelect = { index -> onStartingStall(StartingStall.entries[index]) }
                )
            }
            WoodCard(modifier = Modifier.fillMaxWidth(), borderColor = ChiliRed) {
                SettingLabel(titleRes = R.string.setting_reset, hintRes = R.string.setting_reset_hint)
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { confirming = true },
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, ChiliRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text(
                        text = stringResource(R.string.reset_bazaar),
                        style = MaterialTheme.typography.labelLarge,
                        color = ChiliRed
                    )
                }
            }
            Text(
                text = stringResource(R.string.disclaimer_long),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }

    if (confirming) {
        ConfirmResetDialog(
            onConfirm = {
                confirming = false
                onReset()
            },
            onDismiss = { confirming = false }
        )
    }
}

@Composable
private fun SettingLabel(titleRes: Int, hintRes: Int) {
    Text(
        text = stringResource(titleRes),
        style = MaterialTheme.typography.titleSmall,
        color = CandleGold
    )
    Spacer(modifier = Modifier.height(4.dp))
    Text(
        text = stringResource(hintRes),
        style = MaterialTheme.typography.bodySmall,
        color = TextSecondary
    )
}

@Composable
private fun ToggleRow(
    titleRes: Int,
    hintRes: Int,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f)) {
            SettingLabel(titleRes = titleRes, hintRes = hintRes)
        }
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = PlazaBlack,
                checkedTrackColor = LanternGold,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = TinSurface
            )
        )
    }
}

@Composable
private fun SegmentedRow(
    labels: List<Int>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val selectedLabel = stringResource(R.string.state_selected)
    val idleLabel = stringResource(R.string.state_available)
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        labels.forEachIndexed { index, labelRes ->
            val selected = index == selectedIndex
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (selected) LanternGold else TinTranslucent,
                border = BorderStroke(1.dp, GoldFaint),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .clickable { onSelect(index) }
                    .semantics { stateDescription = if (selected) selectedLabel else idleLabel }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = stringResource(labelRes),
                        style = MaterialTheme.typography.labelLarge,
                        color = if (selected) PlazaBlack else TextPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
        }
    }
}
