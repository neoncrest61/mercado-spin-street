package com.Xulzuge0.Zumgun.presentation.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.GoldHairline
import com.Xulzuge0.Zumgun.core.theme.LanternGold
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.WoodSurface

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onMenu: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onResume,
        shape = RoundedCornerShape(20.dp),
        containerColor = WoodSurface,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = stringResource(R.string.pause_title),
                style = MaterialTheme.typography.headlineSmall,
                color = CandleGold
            )
        },
        text = {
            Text(
                text = stringResource(R.string.pause_body),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onResume,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LanternGold,
                    contentColor = PlazaBlack
                )
            ) {
                Text(
                    text = stringResource(R.string.resume),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onMenu,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GoldHairline)
            ) {
                Text(
                    text = stringResource(R.string.menu),
                    style = MaterialTheme.typography.labelLarge,
                    color = CandleGold
                )
            }
        }
    )
}
