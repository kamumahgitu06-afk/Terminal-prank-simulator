package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.model.AppStrings
import com.example.model.FakeWifiNetwork
import com.example.model.HackMode
import com.example.model.HackPhase
import com.example.model.HackUiState
import com.example.model.NasaTarget
import com.example.model.PentestTool
import com.example.ui.components.FriendTrackerMapHud
import com.example.ui.components.GlitchText
import com.example.ui.components.HackerAutoKeyboard
import com.example.ui.components.MatrixRainBackground
import com.example.ui.components.NasaRadarHud
import com.example.ui.components.RedAlarmFlashOverlay
import com.example.ui.components.ScanlineOverlay
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenMuted
import com.example.ui.theme.TerminalBorder
import kotlin.math.hypot

@Composable
fun HackScreen(
  uiState: HackUiState,
  nasaTargets: List<NasaTarget>,
  onBackToMenu: () -> Unit,
  onToggleSound: () -> Unit,
  onIgUsernameChange: (String) -> Unit,
  onIgPhotoPicked: (Uri?) -> Unit,
  onRescanWifi: () -> Unit,
  onSelectWifiAndHack: (FakeWifiNetwork) -> Unit,
  onSelectNasaTarget: (NasaTarget) -> Unit,
  onFriendNicknameChange: (String) -> Unit,
  onStartSimulation: () -> Unit,
  onHackerKeyPress: () -> Unit,
  onRevealInstagramPrank: () -> Unit,
  onCopyWifiPassword: () -> Unit,
  onTriggerNasaAlarm: () -> Unit,
  onSelectPentestTool: (PentestTool) -> Unit,
  onPentestHostChange: (String) -> Unit,
  getPentestCommandPreview: (PentestTool, String) -> String,
  getPentestResultSummary: (PentestTool, String, com.example.model.AppLanguage) -> String,
  onRestartMode: () -> Unit,
  modifier: Modifier = Modifier
) {
  val mode = uiState.selectedMode ?: return
  val lang = uiState.language
  val listState = rememberLazyListState()
  val swipeThresholdPx = with(LocalDensity.current) { 72.dp.toPx() }

  BackHandler {
    onBackToMenu()
  }

  LaunchedEffect(uiState.terminalLines.size) {
    if (uiState.terminalLines.isNotEmpty()) {
      listState.animateScrollToItem(uiState.terminalLines.lastIndex)
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .onPreviewKeyEvent { keyEvent ->
        if (uiState.phase == HackPhase.HACKING && keyEvent.type == KeyEventType.KeyDown) {
          onHackerKeyPress()
          true
        } else {
          false
        }
      }
      .pointerInput(mode) {
        awaitPointerEventScope {
          var startCentroidX = 0f
          var startCentroidY = 0f
          var trackingTwoFingers = false

          while (true) {
            val event = awaitPointerEvent()
            val pressed = event.changes.filter { it.pressed }
            if (pressed.size >= 2) {
              val cx = pressed.map { it.position.x }.average().toFloat()
              val cy = pressed.map { it.position.y }.average().toFloat()
              if (!trackingTwoFingers) {
                trackingTwoFingers = true
                startCentroidX = cx
                startCentroidY = cy
              } else {
                val dist = hypot(cx - startCentroidX, cy - startCentroidY)
                if (dist > swipeThresholdPx) {
                  trackingTwoFingers = false
                  onBackToMenu()
                }
              }
            } else {
              trackingTwoFingers = false
            }
          }
        }
      }
  ) {
    MatrixRainBackground(alpha = 0.2f)
    ScanlineOverlay()

    Column(
      modifier = Modifier
        .fillMaxSize()
        .windowInsetsPadding(WindowInsets.safeDrawing)
        .widthIn(max = 840.dp)
        .align(Alignment.TopCenter)
        .padding(horizontal = 14.dp, vertical = 10.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      // Top Terminal Header with Back + Sound Toggle
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBackToMenu,
          modifier = Modifier
            .size(48.dp)
            .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            .testTag("hack_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back to Menu",
            tint = NeonGreen
          )
        }

        Column(modifier = Modifier.weight(1f)) {
          GlitchText(
            text = AppStrings.modeTitle(mode, lang),
            style = MaterialTheme.typography.titleLarge,
            color = NeonGreen,
            glitchTrigger = uiState.glitchTrigger
          )
          Text(
            text = AppStrings.panicTip(lang, uiState.stealthCalculatorEnabled),
            style = MaterialTheme.typography.labelSmall,
            color = CyberCyan.copy(alpha = 0.85f),
            maxLines = 1
          )
        }

        IconButton(
          onClick = onToggleSound,
          modifier = Modifier
            .size(48.dp)
            .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            .testTag("hack_sound_toggle")
        ) {
          Icon(
            imageVector = if (uiState.isSoundEnabled) {
              Icons.AutoMirrored.Filled.VolumeUp
            } else {
              Icons.AutoMirrored.Filled.VolumeOff
            },
            contentDescription = "Toggle Sound",
            tint = if (uiState.isSoundEnabled) NeonGreen else AlertRed
          )
        }
      }

      // Mandatory Prank Disclaimer Banner inside EVERY mode
      Surface(
        color = CyberSurface,
        shape = RoundedCornerShape(6.dp),
        border = BorderStroke(1.dp, AlertAmber),
        modifier = Modifier
          .fillMaxWidth()
          .semantics(mergeDescendants = true) {}
          .testTag("mode_disclaimer_banner")
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(Icons.Default.Warning, contentDescription = "Prank Disclaimer", tint = AlertAmber, modifier = Modifier.size(16.dp))
          Text(
            text = AppStrings.modeDisclaimerBadge(lang),
            style = MaterialTheme.typography.labelSmall,
            color = AlertAmber
          )
        }
      }

      if (uiState.phase == HackPhase.SETUP) {
        Box(modifier = Modifier.weight(1f)) {
          LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            item {
              when (mode) {
                HackMode.INSTAGRAM -> InstagramSetupPanel(
                  uiState = uiState,
                  onUsernameChange = onIgUsernameChange,
                  onPhotoPicked = onIgPhotoPicked,
                  onStartHack = onStartSimulation
                )
                HackMode.WIFI -> WifiSetupPanel(
                  uiState = uiState,
                  onRescan = onRescanWifi,
                  onSelectWifi = onSelectWifiAndHack
                )
                HackMode.NASA -> {
                  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    NasaRadarHud(
                      isAlarmActive = false,
                      coordinates = uiState.selectedNasaTarget.coordinates
                    )
                    NasaSetupPanel(
                      uiState = uiState,
                      targets = nasaTargets,
                      onSelectTarget = onSelectNasaTarget,
                      onStartHack = onStartSimulation
                    )
                  }
                }
                HackMode.FRIEND_TRACKER -> {
                  Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    FriendTrackerMapHud(
                      dotProgress = 0.25f,
                      friendNickname = uiState.friendNickname,
                      isLocked = false,
                      language = lang
                    )
                    FriendTrackerSetupPanel(
                      uiState = uiState,
                      onNicknameChange = onFriendNicknameChange,
                      onStartHack = onStartSimulation
                    )
                  }
                }
                HackMode.PENTEST_TOOLS -> PentestToolsSetupPanel(
                  uiState = uiState,
                  commandPreview = getPentestCommandPreview(uiState.selectedPentestTool, uiState.pentestTargetHost),
                  onSelectTool = onSelectPentestTool,
                  onTargetHostChange = onPentestHostChange,
                  onStartTool = onStartSimulation
                )
              }
            }
          }
        }
      } else if (uiState.phase == HackPhase.HACKING) {
        when (mode) {
          HackMode.INSTAGRAM -> {
            Surface(
              color = CyberObsidian,
              shape = RoundedCornerShape(8.dp),
              border = BorderStroke(1.dp, TerminalBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
              ) {
                TargetAvatarImage(photoUri = uiState.igPhotoUri, sizeDp = 46)
                Column {
                  Text("@${uiState.igUsername.removePrefix("@")}", style = MaterialTheme.typography.titleMedium, color = NeonGreenBright)
                  Text(uiState.statusStage, style = MaterialTheme.typography.bodySmall, color = CyberCyan)
                }
              }
            }
            if (uiState.igFakeDms.isNotEmpty()) {
              FakeDmPreviewCard(dms = uiState.igFakeDms, language = lang)
            }
          }
          HackMode.WIFI -> {
            Surface(
              color = CyberObsidian,
              shape = RoundedCornerShape(8.dp),
              border = BorderStroke(1.dp, TerminalBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(
                  text = "${AppStrings.targetSsidPrefix(lang)} ${uiState.selectedWifi?.ssid ?: "Neighbor_WiFi"}",
                  style = MaterialTheme.typography.titleMedium,
                  color = NeonGreenBright
                )
                Text(uiState.statusStage, style = MaterialTheme.typography.bodySmall, color = CyberCyan)
              }
            }
          }
          HackMode.NASA -> {
            NasaRadarHud(
              isAlarmActive = uiState.isAlarmActive,
              coordinates = uiState.selectedNasaTarget.coordinates,
              countdownSec = uiState.nasaCountdownSec
            )
          }
          HackMode.FRIEND_TRACKER -> {
            FriendTrackerMapHud(
              dotProgress = uiState.friendDotProgress,
              friendNickname = uiState.friendNickname,
              isLocked = false,
              language = lang
            )
          }
          HackMode.PENTEST_TOOLS -> {
            Surface(
              color = CyberObsidian,
              shape = RoundedCornerShape(8.dp),
              border = BorderStroke(1.dp, TerminalBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                  text = "TERMINAL // ${uiState.selectedPentestTool.cliName.uppercase()} (${uiState.selectedPentestTool.badge})",
                  style = MaterialTheme.typography.titleMedium,
                  color = NeonGreenBright
                )
                Text(
                  text = "guest@prank-terminal:~# ${getPentestCommandPreview(uiState.selectedPentestTool, uiState.pentestTargetHost)}",
                  style = MaterialTheme.typography.bodySmall,
                  color = CyberCyan
                )
              }
            }
          }
        }

        Column(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberObsidian)
            .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(
              text = uiState.statusStage,
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              color = CyberCyan,
              modifier = Modifier.testTag("hack_status_stage")
            )
            Text(
              text = "${uiState.progress}%",
              style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
              color = NeonGreenBright,
              modifier = Modifier.testTag("hack_progress_percent")
            )
          }
          LinearProgressIndicator(
            progress = { uiState.progress / 100f },
            color = if (uiState.isAlarmActive) AlertRed else NeonGreen,
            trackColor = NeonGreenMuted,
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp))
          )
        }

        LazyColumn(
          state = listState,
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CyberBlack.copy(alpha = 0.92f))
            .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
            .testTag("terminal_output_list"),
          verticalArrangement = Arrangement.spacedBy(3.dp)
        ) {
          items(uiState.terminalLines) { line ->
            Text(
              text = line,
              style = MaterialTheme.typography.bodySmall,
              color = NeonGreen
            )
          }
        }

        HackerAutoKeyboard(
          hintText = AppStrings.autoTypeHint(lang),
          keystrokesCount = uiState.keystrokesCount,
          language = lang,
          onKeyPress = onHackerKeyPress
        )
      } else {
        LazyColumn(
          modifier = Modifier
            .weight(1f)
            .fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            when (mode) {
              HackMode.NASA -> NasaRadarHud(
                isAlarmActive = false,
                coordinates = uiState.selectedNasaTarget.coordinates
              )
              HackMode.FRIEND_TRACKER -> FriendTrackerMapHud(
                dotProgress = uiState.friendDotProgress,
                friendNickname = uiState.friendNickname,
                isLocked = true,
                language = lang
              )
              else -> Unit
            }
          }

          item {
            when (mode) {
              HackMode.INSTAGRAM -> InstagramResultPanel(
                uiState = uiState,
                onRevealPrank = onRevealInstagramPrank,
                onRestart = onRestartMode
              )
              HackMode.WIFI -> WifiResultPanel(
                uiState = uiState,
                onCopyPassword = onCopyWifiPassword,
                onRestart = onRestartMode
              )
              HackMode.NASA -> GenericModeResultPanel(
                headline = AppStrings.nasaGrantedBanner(lang),
                subText = AppStrings.disclaimerBody(lang),
                isAlertStyle = true,
                restartLabel = AppStrings.restartSimulation(lang),
                resultTag = "nasa_granted_result",
                extraActionLabel = AppStrings.nasaAlarmReplayBtn(lang),
                onExtraAction = onTriggerNasaAlarm,
                onRestart = onRestartMode
              )
              HackMode.FRIEND_TRACKER -> GenericModeResultPanel(
                headline = AppStrings.friendLocationResult(lang, uiState.friendLocationText),
                subText = AppStrings.friendTelemetrySub(lang, uiState.friendDetail),
                restartLabel = AppStrings.restartSimulation(lang),
                resultTag = "friend_location_result",
                onRestart = onRestartMode
              )
              HackMode.PENTEST_TOOLS -> GenericModeResultPanel(
                headline = getPentestResultSummary(uiState.selectedPentestTool, uiState.pentestTargetHost, lang),
                subText = AppStrings.disclaimerBody(lang),
                restartLabel = AppStrings.restartSimulation(lang),
                resultTag = "pentest_result_summary",
                onRestart = onRestartMode
              )
            }
          }

          item {
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(CyberBlack.copy(alpha = 0.92f))
                .border(1.dp, TerminalBorder, RoundedCornerShape(8.dp))
                .padding(10.dp),
              verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
              Text(
                text = "LOG // ${uiState.statusStage} (${uiState.progress}%)",
                style = MaterialTheme.typography.labelSmall,
                color = CyberCyan
              )
              uiState.terminalLines.takeLast(6).forEach { line ->
                Text(
                  text = line,
                  style = MaterialTheme.typography.bodySmall,
                  color = NeonGreen
                )
              }
            }
          }
        }
      }
    }

    RedAlarmFlashOverlay(isActive = uiState.isAlarmActive)
  }
}
