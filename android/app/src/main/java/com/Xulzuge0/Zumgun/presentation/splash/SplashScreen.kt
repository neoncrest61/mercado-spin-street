package com.Xulzuge0.Zumgun.presentation.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Xulzuge0.Zumgun.R
import com.Xulzuge0.Zumgun.core.di.AppViewModelFactory
import com.Xulzuge0.Zumgun.core.theme.CandleGold
import com.Xulzuge0.Zumgun.core.theme.ChiliRed
import com.Xulzuge0.Zumgun.core.theme.GoldHairline
import com.Xulzuge0.Zumgun.core.theme.PlazaBlack
import com.Xulzuge0.Zumgun.core.theme.TextMuted
import com.Xulzuge0.Zumgun.core.theme.TextSecondary
import com.Xulzuge0.Zumgun.core.theme.TinSurface
import com.Xulzuge0.Zumgun.core.theme.WoodSurface
import com.Xulzuge0.Zumgun.core.ui.AppBackground
import kotlin.math.cos
import kotlin.math.sin

private const val LANTERN_DOTS = 7
private const val TWO_PI = 6.2831855f

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val viewModel: SplashViewModel = viewModel(factory = AppViewModelFactory)
    val state by viewModel.state.collectAsStateWithLifecycle()
    var handled by remember { mutableStateOf(false) }

    LaunchedEffect(state.finished) {
        if (state.finished && !handled) {
            handled = true
            onFinished()
        }
    }

    SplashContent()
}

@Composable
private fun SplashContent() {
    val transition = rememberInfiniteTransition(label = "splashLoop")
    val halo by transition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo"
    )
    val ring by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ring"
    )
    val banner by transition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "banner"
    )
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        entered = true
    }
    val logoScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.78f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow),
        label = "logoScale"
    )
    val titleAlpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        animationSpec = tween(durationMillis = 700, delayMillis = 180),
        label = "titleAlpha"
    )

    AppBackground(
        overlay = listOf(
            PlazaBlack.copy(alpha = 0.90f),
            Color(0xE61A1208),
            PlazaBlack.copy(alpha = 0.97f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PapelRow(drift = banner)
            Spacer(modifier = Modifier.height(28.dp))
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(260.dp)
                        .graphicsLayer {
                            scaleX = halo
                            scaleY = halo
                        }
                        .background(
                            brush = Brush.radialGradient(
                                listOf(CandleGold.copy(alpha = 0.22f), Color.Transparent)
                            ),
                            shape = CircleShape
                        )
                )
                Canvas(
                    modifier = Modifier
                        .size(248.dp)
                        .graphicsLayer { rotationZ = ring }
                ) {
                    val radius = size.minDimension / 2f * 0.94f
                    repeat(LANTERN_DOTS) { index ->
                        val angle = TWO_PI * index / LANTERN_DOTS
                        drawCircle(
                            color = CandleGold,
                            radius = 4.dp.toPx(),
                            center = Offset(
                                x = center.x + cos(angle) * radius,
                                y = center.y + sin(angle) * radius
                            ),
                            alpha = 0.65f
                        )
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = TinSurface.copy(alpha = 0.80f),
                    border = BorderStroke(2.dp, GoldHairline),
                    shadowElevation = 24.dp,
                    modifier = Modifier.size(220.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = stringResource(R.string.cd_logo),
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(180.dp)
                                .graphicsLayer {
                                    scaleX = logoScale
                                    scaleY = logoScale
                                    alpha = titleAlpha
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(34.dp))
            Box(modifier = Modifier.alpha(titleAlpha)) {
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = ChiliRed,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(x = 2.dp, y = 2.dp)
                )
                Text(
                    text = stringResource(R.string.app_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = CandleGold,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.labelLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(36.dp))
            LinearProgressIndicator(
                color = CandleGold,
                trackColor = TinSurface,
                modifier = Modifier
                    .width(190.dp)
                    .height(5.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = stringResource(R.string.disclaimer_short),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun PapelRow(drift: Float) {
    val colors = listOf(
        ChiliRed,
        CandleGold,
        WoodSurface,
        ChiliRed,
        CandleGold,
        WoodSurface,
        ChiliRed
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(22.dp)
            .graphicsLayer { translationX = drift * 2f }
    ) {
        colors.forEachIndexed { index, color ->
            Box(
                modifier = Modifier
                    .offset(x = (index * 44).dp)
                    .size(width = 30.dp, height = 20.dp)
                    .background(
                        color = color.copy(alpha = 0.65f),
                        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
                    )
            )
        }
    }
}
