package com.Xulzuge0.Zumgun.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.theme.TextMuted

@Composable
fun DisclaimerLine(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.disclaimer_short),
        style = MaterialTheme.typography.labelSmall,
        color = TextMuted,
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}
