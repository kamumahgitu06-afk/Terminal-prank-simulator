package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val HackerColorScheme = darkColorScheme(
  primary = NeonGreen,
  onPrimary = CyberBlack,
  primaryContainer = NeonGreenMuted,
  onPrimaryContainer = NeonGreenBright,
  secondary = CyberCyan,
  onSecondary = CyberBlack,
  secondaryContainer = CyberSurfaceElevated,
  onSecondaryContainer = CyberCyan,
  tertiary = AlertAmber,
  onTertiary = CyberBlack,
  background = CyberBlack,
  onBackground = NeonGreen,
  surface = CyberObsidian,
  onSurface = NeonGreen,
  surfaceVariant = CyberSurface,
  onSurfaceVariant = NeonGreenBright,
  outline = TerminalBorder,
  error = AlertRed,
  onError = CyberBlack
)

@Composable
fun HackPrankTheme(
  content: @Composable () -> Unit
) {
  MaterialTheme(
    colorScheme = HackerColorScheme,
    typography = HackTypography,
    content = content
  )
}
