package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val LightColorScheme = lightColorScheme(
    primary = EmeraldDeep,
    onPrimary = PureWhite,
    primaryContainer = EmeraldSoft,
    onPrimaryContainer = EmeraldDeep,
    secondary = GoldRich,
    onSecondary = CharcoalInk,
    secondaryContainer = GoldSoft,
    onSecondaryContainer = GoldDark,
    tertiary = EmeraldMedium,
    onTertiary = PureWhite,
    tertiaryContainer = SoftMist,
    onTertiaryContainer = CharcoalInk,
    background = IvoryBackground,
    onBackground = CharcoalInk,
    surface = PureWhite,
    onSurface = CharcoalInk,
    surfaceVariant = SoftMist,
    onSurfaceVariant = CharcoalMuted,
    outline = HairlineBorder,
    outlineVariant = HairlineBorder,
    error = StatusError,
    onError = PureWhite,
    errorContainer = StatusErrorBg,
    onErrorContainer = StatusError
)

private val DarkColorScheme = darkColorScheme(
    primary = GoldBright,
    onPrimary = CharcoalInk,
    primaryContainer = EmeraldDeep,
    onPrimaryContainer = GoldSoft,
    secondary = EmeraldMedium,
    onSecondary = PureWhite,
    secondaryContainer = GoldDarkContainer,
    onSecondaryContainer = GoldBright,
    tertiary = GoldRich,
    onTertiary = CharcoalInk,
    background = DarkCharcoalBg,
    onBackground = DarkTextPrimary,
    surface = DarkEmeraldCard,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkEmeraldElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = ColorTokens.DarkBorder,
    error = ColorTokens.DarkError,
    onError = CharcoalInk
)

private object ColorTokens {
    val DarkBorder = androidx.compose.ui.graphics.Color(0xFF2B3D34)
    val DarkError = androidx.compose.ui.graphics.Color(0xFFF2B8B5)
}

val AppShapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(14.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun AlHalalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
