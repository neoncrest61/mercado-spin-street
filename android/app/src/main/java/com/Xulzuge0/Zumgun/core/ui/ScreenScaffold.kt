package com.Xulzuge0.Zumgun.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.theme.CandleGold

@Composable
fun ScreenScaffold(
    title: String,
    onBack: () -> Unit,
    overlay: List<Color>,
    modifier: Modifier = Modifier,
    trailing: @Composable (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    AppBackground(overlay = overlay, modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                IconImageButton(
                    iconRes = R.drawable.icon_home,
                    description = stringResource(R.string.cd_menu),
                    onClick = onBack
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = CandleGold,
                    modifier = Modifier.weight(1f)
                )
                if (trailing != null) {
                    trailing()
                }
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}
