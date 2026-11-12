package com.Xulzuge0.Zumgun.core.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinSurface

@Composable
fun GoodsMeter(
    current: Int,
    target: Int,
    symbolId: Int,
    modifier: Modifier = Modifier
) {
    val label = stringResource(R.string.goods_meter, current, target)
    val filled by animateIntAsState(
        targetValue = current,
        animationSpec = tween(durationMillis = 70 * target + 180),
        label = "goodsFill"
    )
    val rows = if (target > 9) 2 else 1
    val perRow = (target + rows - 1) / rows
    Column(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = label
                liveRegion = LiveRegionMode.Polite
            }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(AssetMap.symbolDrawable(symbolId)),
                contentDescription = stringResource(AssetMap.symbolName(symbolId)),
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        repeat(rows) { rowIndex ->
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(perRow) { pipIndex ->
                    val index = rowIndex * perRow + pipIndex
                    if (index < target) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    color = if (index < filled) CandleGold else TinSurface,
                                    shape = CircleShape
                                )
                        )
                    }
                }
            }
            if (rowIndex == 0 && rows > 1) {
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}
