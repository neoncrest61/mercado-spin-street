package com.Xulzuge0.Zumgun.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MercadoColorScheme = darkColorScheme(
    primary = LanternGold,
    onPrimary = PlazaBlack,
    primaryContainer = WoodSurface,
    onPrimaryContainer = CandleGold,
    secondary = ChiliRed,
    onSecondary = TextPrimary,
    secondaryContainer = TinSurface,
    onSecondaryContainer = TextPrimary,
    tertiary = JadeTick,
    onTertiary = PlazaBlack,
    background = PlazaBlack,
    onBackground = TextPrimary,
    surface = MarketSurface,
    onSurface = TextPrimary,
    surfaceVariant = TinSurface,
    onSurfaceVariant = TextSecondary,
    outline = GoldHairline,
    outlineVariant = GoldFaint,
    error = ChiliRed,
    onError = TextPrimary,
    scrim = PlazaBlack
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MercadoColorScheme,
        typography = AppTypography,
        content = content
    )
}
