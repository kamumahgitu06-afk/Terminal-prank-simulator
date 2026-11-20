package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WifiFind
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.HackMode
import com.example.model.HackUiState
import com.example.ui.components.GlitchText
import com.example.ui.components.MatrixRainBackground
import com.example.ui.components.ScanlineOverlay
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDim
import com.example.ui.theme.TerminalBorder

private data class ModeCardSpec(
  val mode: HackMode,
  val icon: ImageVector,
  val badge: String,
  val tag: String
)

@Composable
fun MenuScreen(
  uiState: HackUiState,
  onDismissDisclaimer: () -> Unit,
  onOpenPrivacy: () -> Unit,
  onDismissPrivacy: () -> Unit,
  onSelectLanguage: (AppLanguage) -> Unit,
  onToggleSound: () -> Unit,
  onToggleStealthCalculator: () -> Unit,
  onSelectMode: (HackMode) -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = uiState.language
  val modes = listOf(
    ModeCardSpec(HackMode.INSTAGRAM, Icons.Default.PersonSearch, "SOSMED_SIM // PRANK", "mode_card_instagram"),
    ModeCardSpec(HackMode.WIFI, Icons.Default.WifiFind, "WIFI_PALSU // FIKSI", "mode_card_wifi"),
    ModeCardSpec(HackMode.NASA, Icons.Default.Public, "ORBIT-X // PRANK", "mode_card_nasa"),
    ModeCardSpec(HackMode.FRIEND_TRACKER, Icons.Default.LocationSearching, "TEBAK_LOKASI // PRANK", "mode_card_friend"),
    ModeCardSpec(HackMode.PENTEST_TOOLS, Icons.Default.Terminal, "PRANK-SCAN • FAKE-HACK", "mode_card_pentest")
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
  ) {
    MatrixRainBackground(alpha = 0.26f)
    ScanlineOverlay()

    BoxWithConstraints(
      modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing)
    ) {
      val columns = if (maxWidth >= 600.dp) 2 else 1

      LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
          .fillMaxSize()
          .widthIn(max = 840.dp)
          .align(Alignment.TopCenter)
      ) {
        item(span = { GridItemSpan(columns) }) {
          MenuHeaderBar(
            language = lang,
            isSoundEnabled = uiState.isSoundEnabled,
            glitchTrigger = uiState.glitchTrigger,
            onOpenPrivacy = onOpenPrivacy,
            onSelectLanguage = onSelectLanguage,
            onToggleSound = onToggleSound
          )
        }

        item(span = { GridItemSpan(columns) }) {
          DisclaimerBanner(text = AppStrings.disclaimerBody(lang))
        }

        item(span = { GridItemSpan(columns) }) {
          Text(
            text = "> ${AppStrings.menuSubtitle(lang)}_",
            style = MaterialTheme.typography.titleMedium,
            color = NeonGreenBright,
            modifier = Modifier.padding(top = 4.dp)
          )
        }

        items(modes, key = { it.mode.name }) { spec ->
          HackModeCard(
            title = AppStrings.modeTitle(spec.mode, lang),
            description = AppStrings.modeDesc(spec.mode, lang),
            badge = spec.badge,
            icon = spec.icon,
            testTagName = spec.tag,
            onClick = { onSelectMode(spec.mode) }
          )
        }

        item(span = { GridItemSpan(columns) }) {
          StealthCalculatorCard(
            language = lang,
            enabled = uiState.stealthCalculatorEnabled,
            onToggle = onToggleStealthCalculator
          )
        }

        item(span = { GridItemSpan(columns) }) {
          PanicTipFooter(tip = AppStrings.panicTip(lang, uiState.stealthCalculatorEnabled))
        }
      }
    }

    if (uiState.showInitialDisclaimer) {
      InitialDisclaimerDialog(
        language = lang,
        onSelectLanguage = onSelectLanguage,
        onAccept = onDismissDisclaimer
      )
    }

    if (uiState.showPrivacyDialog) {
      PrivacyPolicyDialog(
        language = lang,
        onDismiss = onDismissPrivacy
      )
    }
  }
}

@Composable
private fun MenuHeaderBar(
  language: AppLanguage,
  isSoundEnabled: Boolean,
  glitchTrigger: Int,
  onOpenPrivacy: () -> Unit,
  onSelectLanguage: (AppLanguage) -> Unit,
  onToggleSound: () -> Unit
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian.copy(alpha = 0.92f))
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column(modifier = Modifier.weight(1f)) {
        GlitchText(
          text = "TERMINAL PRANK SIMULATOR",
          style = MaterialTheme.typography.titleLarge,
          color = NeonGreen,
          glitchTrigger = glitchTrigger
        )
        Text(
          text = "FAKE HACKER TYPER // 100% OFFLINE",
          style = MaterialTheme.typography.labelSmall,
          color = CyberCyan
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(
          onClick = onOpenPrivacy,
          modifier = Modifier
            .size(48.dp)
            .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            .testTag("privacy_info_button")
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = "Privacy Policy and Terms",
            tint = CyberCyan
          )
        }

        IconButton(
          onClick = onToggleSound,
          modifier = Modifier
            .size(48.dp)
            .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            .testTag("sound_toggle_button")
        ) {
          Icon(
            imageVector = if (isSoundEnabled) {
              Icons.AutoMirrored.Filled.VolumeUp
            } else {
              Icons.AutoMirrored.Filled.VolumeOff
            },
            contentDescription = "Toggle Sound",
            tint = if (isSoundEnabled) NeonGreen else AlertRed
          )
        }
      }
    }

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      AppLanguage.entries.forEach { entry ->
        val selected = entry == language
        Surface(
          color = if (selected) NeonGreen else CyberSurface,
          shape = RoundedCornerShape(6.dp),
          border = BorderStroke(1.dp, if (selected) NeonGreen else TerminalBorder),
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("lang_button_${entry.code.lowercase()}")
            .clickable { onSelectLanguage(entry) }
        ) {
          Box(contentAlignment = Alignment.Center) {
            Text(
              text = "[ ${entry.label} ]",
              style = MaterialTheme.typography.labelLarge,
              color = if (selected) CyberBlack else NeonGreen,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}

@Composable
private fun DisclaimerBanner(text: String) {
  Surface(
    color = CyberSurface,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.5.dp, AlertAmber),
    modifier = Modifier
      .fillMaxWidth()
      .semantics(mergeDescendants = true) {}
      .testTag("menu_disclaimer_banner")
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Warning,
        contentDescription = "Disclaimer",
        tint = AlertAmber,
        modifier = Modifier.size(22.dp)
      )
      Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = AlertAmber
      )
    }
  }
}

@Composable
private fun HackModeCard(
  title: String,
  description: String,
  badge: String,
  icon: ImageVector,
  testTagName: String,
  onClick: () -> Unit
) {
  Card(
    colors = CardDefaults.cardColors(containerColor = CyberObsidian.copy(alpha = 0.94f)),
    border = BorderStroke(1.5.dp, NeonGreenDim),
    shape = RoundedCornerShape(10.dp),
    modifier = Modifier
      .fillMaxWidth()
      .testTag(testTagName)
      .clickable(onClick = onClick)
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
      Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
          .size(54.dp)
          .clip(RoundedCornerShape(8.dp))
          .background(CyberSurfaceElevated)
          .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = NeonGreen,
          modifier = Modifier.size(28.dp)
        )
      }

      Column(
        modifier = Modifier.weight(1f),
        verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        Text(
          text = badge,
          style = MaterialTheme.typography.labelSmall,
          color = CyberCyan
        )
        Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          color = NeonGreenBright
        )
        Text(
          text = description,
          style = MaterialTheme.typography.bodySmall,
          color = NeonGreen.copy(alpha = 0.85f)
        )
      }
    }
  }
}

@Composable
private fun StealthCalculatorCard(
  language: AppLanguage,
  enabled: Boolean,
  onToggle: () -> Unit
) {
  Surface(
    color = CyberObsidian.copy(alpha = 0.92f),
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(1.dp, if (enabled) CyberCyan else TerminalBorder),
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onToggle() }
  ) {
    Row(
      modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Calculate,
        contentDescription = "Stealth Calculator",
        tint = if (enabled) CyberCyan else NeonGreenDim,
        modifier = Modifier.size(26.dp)
      )
      Column(modifier = Modifier.weight(1f)) {
        Text(
          text = AppStrings.stealthToggleTitle(language),
          style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
          color = NeonGreenBright
        )
        Text(
          text = AppStrings.stealthToggleDesc(language, enabled),
          style = MaterialTheme.typography.labelSmall,
          color = if (enabled) CyberCyan else NeonGreen.copy(alpha = 0.75f)
        )
      }
      Switch(
        checked = enabled,
        onCheckedChange = { onToggle() },
        colors = SwitchDefaults.colors(
          checkedThumbColor = CyberBlack,
          checkedTrackColor = CyberCyan,
          uncheckedThumbColor = NeonGreenDim,
          uncheckedTrackColor = CyberSurface
        ),
        modifier = Modifier.testTag("stealth_calculator_toggle")
      )
    }
  }
}

@Composable
private fun PanicTipFooter(tip: String) {
  Surface(
    color = CyberSurface.copy(alpha = 0.9f),
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.dp, TerminalBorder),
    modifier = Modifier.fillMaxWidth()
  ) {
    Row(
      modifier = Modifier.padding(12.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Icon(
        imageVector = Icons.Default.Security,
        contentDescription = "Panic Exit Tip",
        tint = CyberCyan,
        modifier = Modifier.size(20.dp)
      )
      Text(
        text = tip,
        style = MaterialTheme.typography.bodySmall,
        color = CyberCyan
      )
    }
  }
}

@Composable
private fun InitialDisclaimerDialog(
  language: AppLanguage,
  onSelectLanguage: (AppLanguage) -> Unit,
  onAccept: () -> Unit
) {
  Dialog(
    onDismissRequest = {},
    properties = DialogProperties(
      dismissOnBackPress = false,
      dismissOnClickOutside = false
    )
  ) {
    Surface(
      color = CyberObsidian,
      shape = RoundedCornerShape(12.dp),
      border = BorderStroke(2.dp, NeonGreen),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("initial_disclaimer_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Warning,
          contentDescription = "Prank Simulator Warning",
          tint = AlertAmber,
          modifier = Modifier.size(44.dp)
        )

        Text(
          text = AppStrings.disclaimerHeadline(language),
          style = MaterialTheme.typography.titleLarge,
          color = AlertAmber,
          textAlign = TextAlign.Center
        )

        Text(
          text = AppStrings.disclaimerBody(language),
          style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
          color = NeonGreenBright,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("initial_disclaimer_text")
        )

        Text(
          text = AppStrings.disclaimerSubNote(language),
          style = MaterialTheme.typography.bodySmall,
          color = CyberCyan,
          textAlign = TextAlign.Center
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          AppLanguage.entries.forEach { entry ->
            val selected = entry == language
            Surface(
              color = if (selected) NeonGreen else CyberSurface,
              shape = RoundedCornerShape(6.dp),
              border = BorderStroke(1.dp, TerminalBorder),
              modifier = Modifier
                .weight(1f)
                .height(48.dp)
                .clickable { onSelectLanguage(entry) }
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = entry.label,
                  style = MaterialTheme.typography.labelLarge,
                  color = if (selected) CyberBlack else NeonGreen
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Button(
          onClick = onAccept,
          colors = ButtonDefaults.buttonColors(
            containerColor = NeonGreen,
            contentColor = CyberBlack
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("accept_disclaimer_button")
        ) {
          Text(
            text = AppStrings.acceptAndEnter(language),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}

@Composable
private fun PrivacyPolicyDialog(
  language: AppLanguage,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Surface(
      color = CyberObsidian,
      shape = RoundedCornerShape(12.dp),
      border = BorderStroke(2.dp, CyberCyan),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("privacy_policy_dialog")
    ) {
      Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = CyberCyan,
            modifier = Modifier.size(24.dp)
          )
          Text(
            text = AppStrings.privacyTitle(language),
            style = MaterialTheme.typography.titleMedium,
            color = CyberCyan
          )
        }

        Text(
          text = AppStrings.privacyBody(language),
          style = MaterialTheme.typography.bodySmall,
          color = NeonGreenBright
        )

        Text(
          text = "Terminal Prank Simulator • by goanonim_17 (100% Offline)",
          style = MaterialTheme.typography.labelSmall,
          color = AlertAmber
        )

        Button(
          onClick = onDismiss,
          colors = ButtonDefaults.buttonColors(
            containerColor = CyberCyan,
            contentColor = CyberBlack
          ),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("close_privacy_dialog_button")
        ) {
          Text(
            text = AppStrings.closeButton(language),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}
