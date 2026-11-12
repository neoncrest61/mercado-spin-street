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
import com.Xulzuge0.Zumgun.core.theme.ChiliRed
import com.Xulzuge0.Zumgun.core.theme.GoldHairline
import com.Xulzuge0.Zumgun.core.theme.TextPrimary
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.WoodSurface

@Composable
fun ConfirmResetDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = WoodSurface,
        tonalElevation = 0.dp,
        title = {
            Text(
                text = stringResource(R.string.reset_title),
                style = MaterialTheme.typography.headlineSmall,
                color = ChiliRed
            )
        },
        text = {
            Text(
                text = stringResource(R.string.reset_body),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ChiliRed,
                    contentColor = TextPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.reset),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, GoldHairline)
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = CandleGold
                )
            }
        }
    )
}
