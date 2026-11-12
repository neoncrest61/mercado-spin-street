package com.Xulzuge0.Zumgun.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Xulzuge0.Zumgun.core.theme.GoldFaint
import com.Xulzuge0.Zumgun.core.theme.WoodSurface

@Composable
fun WoodCard(
    modifier: Modifier = Modifier,
    borderColor: Color = GoldFaint,
    borderWidth: Dp = 1.dp,
    corner: Dp = 18.dp,
    innerPadding: Dp = 14.dp,
    elevation: Dp = 6.dp,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        shape = RoundedCornerShape(corner),
        color = WoodSurface,
        border = BorderStroke(borderWidth, borderColor),
        shadowElevation = elevation,
        modifier = modifier
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            InsetTopLine()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding),
                content = content
            )
        }
    }
}
