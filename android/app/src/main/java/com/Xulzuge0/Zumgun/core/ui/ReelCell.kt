package com.Xulzuge0.Zumgun.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.CellSurface
import com.Xulzuge0.Zumgun.core.theme.InsetHighlight

@Composable
fun ReelCell(
    symbolId: Int,
    cellSize: Dp,
    highlighted: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = CellSurface,
        border = BorderStroke(
            width = if (highlighted) 2.dp else 1.dp,
            color = if (highlighted) CandleGold else InsetHighlight
        ),
        modifier = modifier.size(cellSize)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(AssetMap.symbolDrawable(symbolId)),
                contentDescription = stringResource(AssetMap.symbolName(symbolId)),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(cellSize * 0.7f)
            )
        }
    }
}
