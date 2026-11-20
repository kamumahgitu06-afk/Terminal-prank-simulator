package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.CalculatorDisguiseScreen
import com.example.ui.screens.HackScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.theme.HackPrankTheme
import com.example.viewmodel.HackViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: HackViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge(
      statusBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK),
      navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.BLACK)
    )
    setContent {
      HackPrankTheme {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        when {
          uiState.isInCalculatorDisguise -> {
            CalculatorDisguiseScreen(
              onExitDisguise = viewModel::exitCalculatorDisguise
            )
          }
          uiState.selectedMode == null -> {
            MenuScreen(
              uiState = uiState,
              onDismissDisclaimer = viewModel::dismissInitialDisclaimer,
              onOpenPrivacy = viewModel::openPrivacyDialog,
              onDismissPrivacy = viewModel::dismissPrivacyDialog,
              onSelectLanguage = viewModel::setLanguage,
              onToggleSound = viewModel::toggleSound,
              onToggleStealthCalculator = viewModel::toggleStealthCalculator,
              onSelectMode = viewModel::selectMode
            )
          }
          else -> {
            HackScreen(
              uiState = uiState,
              nasaTargets = viewModel.nasaTargets,
              onBackToMenu = viewModel::panicExitToMenu,
              onToggleSound = viewModel::toggleSound,
              onIgUsernameChange = viewModel::updateIgUsername,
              onIgPhotoPicked = viewModel::updateIgPhotoUri,
              onRescanWifi = viewModel::scanFakeWifi,
              onSelectWifiAndHack = viewModel::selectWifiAndHack,
              onSelectNasaTarget = viewModel::selectNasaTarget,
              onFriendNicknameChange = viewModel::updateFriendNickname,
              onStartSimulation = viewModel::startSimulation,
              onHackerKeyPress = viewModel::onHackerKeyPress,
              onRevealInstagramPrank = viewModel::revealInstagramPrank,
              onCopyWifiPassword = viewModel::markWifiPasswordCopied,
              onTriggerNasaAlarm = viewModel::triggerNasaPrankAlarm,
              onSelectPentestTool = viewModel::selectPentestTool,
              onPentestHostChange = viewModel::updatePentestTargetHost,
              getPentestCommandPreview = viewModel::getPentestCommandPreview,
              getPentestResultSummary = viewModel::getPentestResultSummary,
              onRestartMode = viewModel::resetCurrentModeToSetup
            )
          }
        }
      }
    }
  }

  override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
      if (viewModel.onVolumeDownPressed()) {
        return true
      }
    }
    return super.onKeyDown(keyCode, event)
  }
}
