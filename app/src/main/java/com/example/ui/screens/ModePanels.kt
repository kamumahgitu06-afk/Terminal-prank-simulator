package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.FakeDirectMessage
import com.example.model.FakeWifiNetwork
import com.example.model.HackPhase
import com.example.model.HackUiState
import com.example.model.NasaTarget
import com.example.model.PentestTool
import com.example.ui.components.GlitchText
import com.example.ui.theme.AlertAmber
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDim
import com.example.ui.theme.TerminalBorder

@Composable
fun TargetAvatarImage(
  photoUri: Uri?,
  sizeDp: Int = 64
) {
  if (photoUri != null) {
    AsyncImage(
      model = photoUri,
      contentDescription = "Target profile avatar",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(sizeDp.dp)
        .clip(CircleShape)
        .border(2.dp, NeonGreen, CircleShape)
    )
  } else {
    Image(
      painter = painterResource(id = R.drawable.img_default_avatar),
      contentDescription = "Default target avatar",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .size(sizeDp.dp)
        .clip(CircleShape)
        .border(2.dp, NeonGreen, CircleShape)
    )
  }
}

@Composable
fun InstagramSetupPanel(
  uiState: HackUiState,
  onUsernameChange: (String) -> Unit,
  onPhotoPicked: (Uri?) -> Unit,
  onStartHack: () -> Unit
) {
  val lang = uiState.language
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      onPhotoPicked(uri)
    }
  }

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian.copy(alpha = 0.94f))
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(14.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    TargetAvatarImage(photoUri = uiState.igPhotoUri, sizeDp = 82)

    OutlinedButton(
      onClick = {
        photoPickerLauncher.launch(
          PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
      },
      border = BorderStroke(1.dp, CyberCyan),
      shape = RoundedCornerShape(8.dp),
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp)
        .testTag("pick_ig_photo_button")
    ) {
      Icon(
        imageVector = Icons.Default.PhotoLibrary,
        contentDescription = "Pick Gallery Photo",
        tint = CyberCyan,
        modifier = Modifier.size(18.dp)
      )
      Text(
        text = "  ${AppStrings.pickGalleryPhoto(lang)}",
        style = MaterialTheme.typography.labelSmall,
        color = CyberCyan
      )
    }

    OutlinedTextField(
      value = uiState.igUsername,
      onValueChange = onUsernameChange,
      label = { Text(AppStrings.targetUsernameLabel(lang), color = NeonGreen) },
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = NeonGreen,
        unfocusedBorderColor = TerminalBorder,
        focusedTextColor = NeonGreenBright,
        unfocusedTextColor = NeonGreen,
        cursorColor = NeonGreen
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("ig_username_input")
    )

    StartHackButton(
      label = AppStrings.startBreach(lang),
      onClick = onStartHack
    )
  }
}

@Composable
fun WifiSetupPanel(
  uiState: HackUiState,
  onRescan: () -> Unit,
  onSelectWifi: (FakeWifiNetwork) -> Unit
) {
  val lang = uiState.language

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian.copy(alpha = 0.94f))
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = AppStrings.availableRoutersHeader(lang, uiState.isScanningWifi),
        style = MaterialTheme.typography.labelSmall,
        color = CyberCyan,
        modifier = Modifier.weight(1f)
      )
      OutlinedButton(
        onClick = onRescan,
        border = BorderStroke(1.dp, NeonGreenDim),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier
          .height(48.dp)
          .testTag("rescan_wifi_button")
      ) {
        Icon(Icons.Default.Refresh, contentDescription = "Rescan", tint = NeonGreen, modifier = Modifier.size(16.dp))
        Text(" ${AppStrings.scanBtnLabel(lang)}", style = MaterialTheme.typography.labelSmall, color = NeonGreen)
      }
    }

    if (uiState.isScanningWifi) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(120.dp),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator(color = NeonGreen)
      }
    } else {
      uiState.wifiNetworks.forEachIndexed { index, wifi ->
        Surface(
          color = CyberSurface,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, TerminalBorder),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("wifi_item_$index")
            .clickable { onSelectWifi(wifi) }
        ) {
          Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp),
              modifier = Modifier.weight(1f)
            ) {
              Icon(Icons.Default.Wifi, contentDescription = wifi.ssid, tint = NeonGreen)
              Column {
                Text(wifi.ssid, style = MaterialTheme.typography.titleMedium, color = NeonGreenBright)
                Text("${wifi.bssid} • ${wifi.security} • CH ${wifi.channel}", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
              }
            }
            Text(
              text = "${wifi.signalStrength} dBm",
              style = MaterialTheme.typography.labelSmall,
              color = AlertAmber
            )
          }
        }
      }
    }
  }
}

@Composable
fun NasaSetupPanel(
  uiState: HackUiState,
  targets: List<NasaTarget>,
  onSelectTarget: (NasaTarget) -> Unit,
  onStartHack: () -> Unit
) {
  val lang = uiState.language

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian.copy(alpha = 0.94f))
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(
      text = AppStrings.selectNasaNodeHeader(lang),
      style = MaterialTheme.typography.labelSmall,
      color = CyberCyan
    )

    targets.forEach { target ->
      val isSelected = target.id == uiState.selectedNasaTarget.id
      Surface(
        color = if (isSelected) NeonGreen.copy(alpha = 0.14f) else CyberSurface,
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.5.dp, if (isSelected) NeonGreen else TerminalBorder),
        modifier = Modifier
          .fillMaxWidth()
          .testTag("nasa_target_${target.id}")
          .clickable { onSelectTarget(target) }
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Text(target.codeName, style = MaterialTheme.typography.titleMedium, color = NeonGreenBright)
          Text(target.coordinates, style = MaterialTheme.typography.labelSmall, color = CyberCyan)
        }
      }
    }

    StartHackButton(
      label = AppStrings.startBreach(lang),
      onClick = onStartHack
    )
  }
}

@Composable
fun FriendTrackerSetupPanel(
  uiState: HackUiState,
  onNicknameChange: (String) -> Unit,
  onStartHack: () -> Unit
) {
  val lang = uiState.language

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian.copy(alpha = 0.94f))
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    OutlinedTextField(
      value = uiState.friendNickname,
      onValueChange = onNicknameChange,
      label = { Text(AppStrings.friendNicknameLabel(lang), color = NeonGreen) },
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = NeonGreen,
        unfocusedBorderColor = TerminalBorder,
        focusedTextColor = NeonGreenBright,
        unfocusedTextColor = NeonGreen,
        cursorColor = NeonGreen
      ),
      modifier = Modifier
        .fillMaxWidth()
        .testTag("friend_nickname_input")
    )

    StartHackButton(
      label = AppStrings.startBreach(lang),
      onClick = onStartHack
    )
  }
}

@Composable
fun PentestToolsSetupPanel(
  uiState: HackUiState,
  commandPreview: String,
  onSelectTool: (PentestTool) -> Unit,
  onTargetHostChange: (String) -> Unit,
  onStartTool: () -> Unit
) {
  val lang = uiState.language
  val selectedTool = uiState.selectedPentestTool
  val toolPairs = PentestTool.entries.chunked(2)

  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian.copy(alpha = 0.94f))
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(14.dp),
    verticalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    Text(
      text = AppStrings.selectPentestToolHeader(lang),
      style = MaterialTheme.typography.labelSmall,
      color = CyberCyan
    )

    toolPairs.forEach { rowTools ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        rowTools.forEach { tool ->
          val selected = tool == selectedTool
          Surface(
            color = if (selected) NeonGreen.copy(alpha = 0.16f) else CyberSurface,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.5.dp, if (selected) NeonGreen else TerminalBorder),
            modifier = Modifier
              .weight(1f)
              .height(50.dp)
              .testTag("pentest_tool_${tool.cliName}")
              .clickable { onSelectTool(tool) }
          ) {
            Column(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
              verticalArrangement = Arrangement.Center
            ) {
              Text(
                text = tool.cliName,
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) NeonGreenBright else NeonGreen,
                maxLines = 1
              )
              Text(
                text = tool.badge,
                style = MaterialTheme.typography.labelSmall,
                color = if (selected) AlertAmber else CyberCyan,
                maxLines = 1
              )
            }
          }
        }
        if (rowTools.size == 1) {
          Box(modifier = Modifier.weight(1f))
        }
      }
    }

    Text(
      text = "> ${if (lang == AppLanguage.INDONESIAN) selectedTool.descId else selectedTool.descEn}",
      style = MaterialTheme.typography.bodySmall,
      color = CyberCyan
    )

    Text(
      text = AppStrings.targetHostLabel(lang),
      style = MaterialTheme.typography.labelSmall,
      color = CyberCyan
    )

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
      AppStrings.fakeTerminalTargets.forEach { target ->
        val isSelected = target == uiState.pentestTargetHost
        Surface(
          color = if (isSelected) NeonGreen.copy(alpha = 0.16f) else CyberSurface,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.5.dp, if (isSelected) NeonGreen else TerminalBorder),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("fake_target_$target")
            .clickable { onTargetHostChange(target) }
        ) {
          Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.padding(horizontal = 12.dp)
          ) {
            val selectedBadge = if (lang == AppLanguage.INDONESIAN) "TERPILIH" else "SELECTED"
            Text(
              text = if (isSelected) "> $target [$selectedBadge]" else "  $target",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
              color = if (isSelected) NeonGreenBright else NeonGreen,
              maxLines = 1
            )
          }
        }
      }
    }

    Surface(
      color = CyberBlack,
      shape = RoundedCornerShape(6.dp),
      border = BorderStroke(1.dp, TerminalBorder),
      modifier = Modifier.fillMaxWidth()
    ) {
      Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
          text = AppStrings.commandPreviewLabel(lang),
          style = MaterialTheme.typography.labelSmall,
          color = AlertAmber
        )
        Text(
          text = "guest@prank-terminal:~# $commandPreview",
          style = MaterialTheme.typography.bodySmall,
          color = NeonGreenBright,
          modifier = Modifier.testTag("pentest_command_preview")
        )
      }
    }

    StartHackButton(
      label = AppStrings.runToolButton(lang, selectedTool.cliName.uppercase()),
      onClick = onStartTool
    )
  }
}

@Composable
fun StartHackButton(
  label: String,
  onClick: () -> Unit
) {
  Button(
    onClick = onClick,
    colors = ButtonDefaults.buttonColors(
      containerColor = NeonGreen,
      contentColor = CyberBlack
    ),
    shape = RoundedCornerShape(8.dp),
    modifier = Modifier
      .fillMaxWidth()
      .height(52.dp)
      .testTag("start_hack_button")
  ) {
    Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
    Text(
      text = " $label",
      style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
    )
  }
}

@Composable
fun FakeDmPreviewCard(
  dms: List<FakeDirectMessage>,
  language: AppLanguage
) {
  if (dms.isEmpty()) return
  Surface(
    color = CyberSurface,
    shape = RoundedCornerShape(8.dp),
    border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.7f)),
    modifier = Modifier
      .fillMaxWidth()
      .testTag("ig_fake_dm_card")
  ) {
    Column(
      modifier = Modifier.padding(10.dp),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      Text(
        text = AppStrings.igDmDumpHeader(language),
        style = MaterialTheme.typography.labelSmall,
        color = CyberCyan
      )
      dms.forEach { dm ->
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Text(
            text = "${dm.sender}: ${dm.message}",
            style = MaterialTheme.typography.bodySmall,
            color = NeonGreenBright,
            modifier = Modifier.weight(1f)
          )
          Text(
            text = dm.time,
            style = MaterialTheme.typography.labelSmall,
            color = NeonGreenDim
          )
        }
      }
    }
  }
}

@Composable
fun InstagramResultPanel(
  uiState: HackUiState,
  onRevealPrank: () -> Unit,
  onRestart: () -> Unit
) {
  val lang = uiState.language
  val isRevealed = uiState.phase == HackPhase.PRANK_REVEALED

  Surface(
    color = CyberObsidian,
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(2.dp, if (isRevealed) AlertAmber else NeonGreen),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      TargetAvatarImage(photoUri = uiState.igPhotoUri, sizeDp = 70)
      Text("@${uiState.igUsername.removePrefix("@")}", style = MaterialTheme.typography.titleMedium, color = CyberCyan)

      FakeDmPreviewCard(dms = uiState.igFakeDms, language = lang)

      if (!isRevealed) {
        Text(
          text = AppStrings.igPasswordFound(lang),
          style = MaterialTheme.typography.titleMedium,
          color = NeonGreenBright,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("ig_password_masked")
        )
        Button(
          onClick = onRevealPrank,
          colors = ButtonDefaults.buttonColors(containerColor = NeonGreen, contentColor = CyberBlack),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("reveal_password_button")
        ) {
          Icon(Icons.Default.Visibility, contentDescription = null)
          Text("  ${AppStrings.viewPasswordBtn(lang)}", style = MaterialTheme.typography.labelLarge)
        }
      } else {
        GlitchText(
          text = AppStrings.igPrankRevealMessage(lang),
          style = MaterialTheme.typography.titleLarge,
          color = AlertAmber,
          glitchTrigger = uiState.glitchTrigger,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("ig_prank_revealed_text")
        )
      }

      OutlinedButton(
        onClick = onRestart,
        border = BorderStroke(1.dp, TerminalBorder),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text(AppStrings.restartSimulation(lang), color = NeonGreen)
      }
    }
  }
}

@Composable
fun WifiResultPanel(
  uiState: HackUiState,
  onCopyPassword: () -> Unit,
  onRestart: () -> Unit
) {
  val lang = uiState.language
  val clipboardManager = LocalClipboardManager.current

  Surface(
    color = CyberObsidian,
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(2.dp, NeonGreen),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = AppStrings.wifiPasswordResult(lang, uiState.revealedWifiPassword),
        style = MaterialTheme.typography.titleLarge,
        color = NeonGreenBright,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag("wifi_password_result")
      )
      Text(
        text = AppStrings.hackSuccessBanner(lang),
        style = MaterialTheme.typography.bodySmall,
        color = CyberCyan,
        textAlign = TextAlign.Center
      )
      Button(
        onClick = {
          clipboardManager.setText(AnnotatedString(uiState.revealedWifiPassword))
          onCopyPassword()
        },
        colors = ButtonDefaults.buttonColors(
          containerColor = if (uiState.wifiPasswordCopied) AlertAmber else NeonGreen,
          contentColor = CyberBlack
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
          .testTag("copy_wifi_password_button")
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(18.dp))
        Text(
          text = "  ${AppStrings.copyWifiPasswordBtn(lang, uiState.wifiPasswordCopied)}",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
        )
      }
      OutlinedButton(
        onClick = onRestart,
        border = BorderStroke(1.dp, NeonGreenDim),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text(AppStrings.restartSimulation(lang), color = NeonGreen)
      }
    }
  }
}

@Composable
fun GenericModeResultPanel(
  headline: String,
  subText: String,
  isAlertStyle: Boolean = false,
  restartLabel: String,
  resultTag: String,
  extraActionLabel: String? = null,
  onExtraAction: (() -> Unit)? = null,
  onRestart: () -> Unit
) {
  Surface(
    color = CyberObsidian,
    shape = RoundedCornerShape(10.dp),
    border = BorderStroke(2.dp, if (isAlertStyle) AlertRed else NeonGreen),
    modifier = Modifier.fillMaxWidth()
  ) {
    Column(
      modifier = Modifier.padding(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Text(
        text = headline,
        style = MaterialTheme.typography.titleLarge,
        color = if (isAlertStyle) AlertAmber else NeonGreenBright,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag(resultTag)
      )
      Text(
        text = subText,
        style = MaterialTheme.typography.bodySmall,
        color = CyberCyan,
        textAlign = TextAlign.Center
      )
      if (extraActionLabel != null && onExtraAction != null) {
        Button(
          onClick = onExtraAction,
          colors = ButtonDefaults.buttonColors(containerColor = AlertRed, contentColor = CyberBlack),
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
        ) {
          Icon(Icons.Default.NotificationsActive, contentDescription = null, modifier = Modifier.size(18.dp))
          Text("  $extraActionLabel", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
        }
      }
      OutlinedButton(
        onClick = onRestart,
        border = BorderStroke(1.dp, NeonGreenDim),
        modifier = Modifier
          .fillMaxWidth()
          .height(48.dp)
      ) {
        Text(restartLabel, color = NeonGreen)
      }
    }
  }
}
