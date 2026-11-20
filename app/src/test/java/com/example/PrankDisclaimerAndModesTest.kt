package com.example

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.test.core.app.ApplicationProvider
import com.example.engine.FakeTerminalEngine
import com.example.engine.SoundEngine
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.model.FakeWifiNetwork
import com.example.model.HackMode
import com.example.model.HackPhase
import com.example.model.PentestTool
import com.example.ui.screens.CalculatorDisguiseScreen
import com.example.ui.screens.HackScreen
import com.example.ui.screens.MenuScreen
import com.example.ui.theme.HackPrankTheme
import com.example.viewmodel.HackViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.random.Random

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36], qualifiers = "w411dp-h891dp-xxhdpi")
class PrankDisclaimerAndModesTest {

  @get:Rule
  val composeTestRule = createComposeRule()

  @Before
  fun clearPrefs() {
    val app = ApplicationProvider.getApplicationContext<Application>()
    app.getSharedPreferences("hackprank_prefs", Context.MODE_PRIVATE).edit().clear().commit()
  }

  private fun createTestViewModel(): HackViewModel {
    val app = ApplicationProvider.getApplicationContext<Application>()
    val soundEngine = SoundEngine(app).apply { isEnabled = false }
    return HackViewModel(
      application = app,
      terminalEngine = FakeTerminalEngine(Random(42)),
      soundEngine = soundEngine
    )
  }

  private fun setRootContent(viewModel: HackViewModel) {
    composeTestRule.setContent {
      HackPrankTheme {
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        when {
          state.isInCalculatorDisguise -> {
            CalculatorDisguiseScreen(onExitDisguise = viewModel::exitCalculatorDisguise)
          }
          state.selectedMode == null -> {
            MenuScreen(
              uiState = state,
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
              uiState = state,
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

  @Test
  fun initialLaunchShowsMandatoryPrankDisclaimerInIndonesian() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("initial_disclaimer_dialog").assertIsDisplayed()
    composeTestRule.onNodeWithTag("initial_disclaimer_text").assertTextContains(
      "INI HANYA PRANK / SIMULATOR UNTUK HIBURAN. TIDAK MENG-HACK APAPUN."
    )
    composeTestRule.onNodeWithTag("menu_disclaimer_banner").assertIsDisplayed()
  }

  @Test
  fun allFiveModesDisplayPrankDisclaimerBannerInIndonesianAndEnglish() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()

    for (language in AppLanguage.entries) {
      viewModel.setLanguage(language)
      for (mode in HackMode.entries) {
        viewModel.selectMode(mode)
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("mode_disclaimer_banner").assertIsDisplayed()
        composeTestRule.onNodeWithTag("mode_disclaimer_banner").assertTextContains(
          AppStrings.modeDisclaimerBadge(language)
        )
      }
    }
  }

  @Test
  fun allModesKeepPrankDisclaimerVisibleDuringHackingAndCompletion() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()

    val fakeWifi = FakeWifiNetwork("Tetangga_WiFi_5G", "A4:2B:8C:19:F0:4A", "WPA2-PSK", -48, 36)

    for (mode in HackMode.entries) {
      viewModel.selectMode(mode)
      if (mode == HackMode.WIFI) {
        viewModel.selectWifiAndHack(fakeWifi)
      } else {
        viewModel.startSimulation()
      }
      composeTestRule.waitForIdle()

      composeTestRule.onNodeWithTag("mode_disclaimer_banner").assertIsDisplayed()

      repeat(25) {
        if (viewModel.uiState.value.phase == HackPhase.HACKING) {
          viewModel.onHackerKeyPress()
        }
      }
      composeTestRule.waitForIdle()

      assertEquals(HackPhase.COMPLETED, viewModel.uiState.value.phase)
      assertEquals(100, viewModel.uiState.value.progress)
      assertTrue(viewModel.uiState.value.terminalLines.isNotEmpty())
      composeTestRule.onNodeWithTag("mode_disclaimer_banner").assertIsDisplayed()
    }
  }

  @Test
  fun pretendTerminalToolsSimulatePrankScanAndFakeHackWithDisclaimer() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()
    viewModel.selectMode(HackMode.PENTEST_TOOLS)
    composeTestRule.waitForIdle()

    // Select prank-scan and preset fake target
    viewModel.selectPentestTool(PentestTool.PRANK_SCAN)
    viewModel.updatePentestTargetHost("SATELIT-ORBIT-X")
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("pentest_command_preview").assertTextContains(
      "prank-scan --mode lucu --target SATELIT-ORBIT-X",
      substring = true
    )

    viewModel.startSimulation()
    assertTrue(viewModel.uiState.value.terminalLines.any { it.contains("prank-scan") })
    repeat(25) {
      if (viewModel.uiState.value.phase == HackPhase.HACKING) viewModel.onHackerKeyPress()
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("mode_disclaimer_banner").assertIsDisplayed()
    composeTestRule.onNodeWithTag("pentest_result_summary").assertTextContains(
      "PRANK-SCAN SELESAI",
      substring = true
    )

    // Switch to fake-hack
    viewModel.resetCurrentModeToSetup()
    viewModel.selectPentestTool(PentestTool.FAKE_HACK)
    viewModel.updatePentestTargetHost("WIFI-WARUNG-SEBELAH")
    viewModel.startSimulation()
    assertTrue(viewModel.uiState.value.terminalLines.any { it.contains("fake-hack") })
  }

  @Test
  fun instagramShowsFakeDmsAndRevealButtonShowsPrankMessage() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()
    viewModel.selectMode(HackMode.INSTAGRAM)
    viewModel.startSimulation()

    repeat(25) {
      if (viewModel.uiState.value.phase == HackPhase.HACKING) {
        viewModel.onHackerKeyPress()
      }
    }
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("ig_fake_dm_card").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ig_password_masked").assertIsDisplayed()
    composeTestRule.onNodeWithTag("reveal_password_button").performClick()
    composeTestRule.waitForIdle()

    assertEquals(HackPhase.PRANK_REVEALED, viewModel.uiState.value.phase)
    composeTestRule.onNodeWithTag("ig_prank_revealed_text").assertIsDisplayed()
    composeTestRule.onNodeWithTag("ig_prank_revealed_text").assertTextContains(
      "Kena Prank! Ini cuma simulator!"
    )
  }

  @Test
  fun wifiCopyAndFriendModeShowExpectedPrankResults() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()

    viewModel.selectMode(HackMode.WIFI)
    viewModel.selectWifiAndHack(
      FakeWifiNetwork("Tetangga_WiFi_5G", "A4:2B:8C:19:F0:4A", "WPA2-PSK", -48, 36)
    )
    repeat(25) {
      if (viewModel.uiState.value.phase == HackPhase.HACKING) viewModel.onHackerKeyPress()
    }
    composeTestRule.waitForIdle()
    composeTestRule.onNodeWithTag("wifi_password_result").assertTextContains("PASSWORD WIFI: Prank123")
    composeTestRule.onNodeWithTag("copy_wifi_password_button").performClick()
    composeTestRule.waitForIdle()
    assertTrue(viewModel.uiState.value.wifiPasswordCopied)

    viewModel.selectMode(HackMode.FRIEND_TRACKER)
    viewModel.startSimulation()
    repeat(25) {
      if (viewModel.uiState.value.phase == HackPhase.HACKING) viewModel.onHackerKeyPress()
    }
    composeTestRule.waitForIdle()
    assertTrue(viewModel.uiState.value.friendLocationText.isNotBlank())
    composeTestRule.onNodeWithTag("friend_location_result").assertTextContains(
      "TEMANMU SEDANG DI: ${viewModel.uiState.value.friendLocationText}"
    )
  }

  @Test
  fun volumeDownThreeTimesTriggersSecretPanicExitAndOptionalCalculatorDisguise() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()

    viewModel.selectMode(HackMode.NASA)
    assertFalse(viewModel.uiState.value.selectedMode == null)

    assertTrue(viewModel.onVolumeDownPressed(nowMillis = 1000L))
    assertTrue(viewModel.onVolumeDownPressed(nowMillis = 1300L))
    assertTrue(viewModel.onVolumeDownPressed(nowMillis = 1600L))
    assertNull(viewModel.uiState.value.selectedMode)
    assertFalse(viewModel.uiState.value.isInCalculatorDisguise)

    viewModel.toggleStealthCalculator()
    viewModel.selectMode(HackMode.NASA)
    assertTrue(viewModel.onVolumeDownPressed(nowMillis = 2000L))
    assertTrue(viewModel.onVolumeDownPressed(nowMillis = 2200L))
    assertTrue(viewModel.onVolumeDownPressed(nowMillis = 2400L))
    composeTestRule.waitForIdle()

    assertTrue(viewModel.uiState.value.isInCalculatorDisguise)
    composeTestRule.onNodeWithTag("calculator_disguise_screen").assertIsDisplayed()
  }

  @Test
  fun languageAndSoundPreferencesPersistAcrossViewModelCreation() {
    val vm1 = createTestViewModel()
    vm1.setLanguage(AppLanguage.ENGLISH)
    vm1.toggleSound()

    val vm2 = createTestViewModel()
    assertEquals(AppLanguage.ENGLISH, vm2.uiState.value.language)
    assertFalse(vm2.uiState.value.isSoundEnabled)
  }

  @Test
  fun privacyPolicyButtonOpensOfflinePrivacyAndTermsDialog() {
    val viewModel = createTestViewModel()
    setRootContent(viewModel)
    viewModel.dismissInitialDisclaimer()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("privacy_info_button").performClick()
    composeTestRule.waitForIdle()

    composeTestRule.onNodeWithTag("privacy_policy_dialog").assertIsDisplayed()
    composeTestRule.onNodeWithTag("close_privacy_dialog_button").assertIsDisplayed()
    assertTrue(viewModel.uiState.value.showPrivacyDialog)

    composeTestRule.onNodeWithTag("close_privacy_dialog_button").performClick()
    composeTestRule.waitForIdle()

    assertFalse(viewModel.uiState.value.showPrivacyDialog)
    composeTestRule.onNodeWithTag("privacy_policy_dialog").assertDoesNotExist()
  }
}
