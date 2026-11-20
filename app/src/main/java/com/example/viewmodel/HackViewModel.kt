package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.engine.FakeTerminalEngine
import com.example.engine.SoundEngine
import com.example.model.AppLanguage
import com.example.model.FakeWifiNetwork
import com.example.model.HackMode
import com.example.model.HackPhase
import com.example.model.HackUiState
import com.example.model.NasaTarget
import com.example.model.PentestTool
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HackViewModel(
  application: Application,
  private val terminalEngine: FakeTerminalEngine = FakeTerminalEngine(),
  private val soundEngine: SoundEngine = SoundEngine(application)
) : AndroidViewModel(application) {

  constructor(application: Application) : this(
    application = application,
    terminalEngine = FakeTerminalEngine(),
    soundEngine = SoundEngine(application)
  )

  private val prefs = application.getSharedPreferences("hackprank_prefs", Context.MODE_PRIVATE)

  private val _uiState = MutableStateFlow(loadInitialState())
  val uiState: StateFlow<HackUiState> = _uiState.asStateFlow()

  private var simulationJob: Job? = null
  private var wifiScanJob: Job? = null

  private val volumeDownTimestamps = mutableListOf<Long>()

  val nasaTargets: List<NasaTarget>
    get() = terminalEngine.fakeNasaTargets

  fun getPentestCommandPreview(tool: PentestTool, host: String): String =
    terminalEngine.getPentestCommandPreview(tool, host)

  fun getPentestResultSummary(tool: PentestTool, host: String, lang: AppLanguage): String =
    terminalEngine.getPentestResultSummary(tool, host, lang)

  private fun loadInitialState(): HackUiState {
    val savedLangCode = prefs.getString("language", AppLanguage.INDONESIAN.code)
    val savedLang = AppLanguage.entries.firstOrNull { it.code == savedLangCode } ?: AppLanguage.INDONESIAN
    val savedSound = prefs.getBoolean("sound_enabled", true)
    val savedStealth = prefs.getBoolean("stealth_calculator", false)
    soundEngine.isEnabled = savedSound

    return HackUiState(
      language = savedLang,
      isSoundEnabled = savedSound,
      stealthCalculatorEnabled = savedStealth,
      wifiNetworks = terminalEngine.generateFakeWifiNetworks(),
      selectedNasaTarget = terminalEngine.fakeNasaTargets.first()
    )
  }

  fun dismissInitialDisclaimer() {
    soundEngine.playBeep()
    _uiState.update { it.copy(showInitialDisclaimer = false) }
  }

  fun openPrivacyDialog() {
    soundEngine.playBeep()
    _uiState.update { it.copy(showPrivacyDialog = true) }
  }

  fun dismissPrivacyDialog() {
    soundEngine.playBeep()
    _uiState.update { it.copy(showPrivacyDialog = false) }
  }

  fun setLanguage(language: AppLanguage) {
    soundEngine.playBeep()
    prefs.edit().putString("language", language.code).apply()
    _uiState.update { state ->
      val updatedDetail = if (state.friendDetail != null) {
        terminalEngine.pickRandomFriendDetail(language)
      } else {
        null
      }
      val updatedDms = if (state.igFakeDms.isNotEmpty()) {
        terminalEngine.generateFakeInstagramDms(state.igUsername, language)
      } else {
        emptyList()
      }
      state.copy(
        language = language,
        friendDetail = updatedDetail,
        friendLocationText = updatedDetail?.location ?: "",
        igFakeDms = updatedDms
      )
    }
  }

  fun toggleSound() {
    val next = !_uiState.value.isSoundEnabled
    soundEngine.isEnabled = next
    prefs.edit().putBoolean("sound_enabled", next).apply()
    if (next) {
      soundEngine.playBeep()
    }
    _uiState.update { it.copy(isSoundEnabled = next) }
  }

  fun toggleStealthCalculator() {
    val next = !_uiState.value.stealthCalculatorEnabled
    soundEngine.playBeep()
    prefs.edit().putBoolean("stealth_calculator", next).apply()
    _uiState.update { it.copy(stealthCalculatorEnabled = next) }
  }

  fun exitCalculatorDisguise() {
    soundEngine.playBeep()
    _uiState.update { it.copy(isInCalculatorDisguise = false) }
  }

  fun selectMode(mode: HackMode) {
    simulationJob?.cancel()
    wifiScanJob?.cancel()
    soundEngine.playBeep()

    _uiState.update { state ->
      state.copy(
        selectedMode = mode,
        isInCalculatorDisguise = false,
        phase = HackPhase.SETUP,
        progress = 0,
        statusStage = terminalEngine.getStageLabel(mode, 0, state.language, state.selectedPentestTool),
        terminalLines = emptyList(),
        keystrokesCount = 0,
        isAlarmActive = false,
        nasaCountdownSec = 15,
        selectedWifi = null,
        wifiPasswordCopied = false,
        igFakeDms = emptyList(),
        friendLocationText = "",
        friendDetail = null
      )
    }

    if (mode == HackMode.WIFI) {
      scanFakeWifi()
    }
  }

  fun selectPentestTool(tool: PentestTool) {
    soundEngine.playBeep()
    _uiState.update { it.copy(selectedPentestTool = tool) }
  }

  fun updatePentestTargetHost(host: String) {
    _uiState.update { it.copy(pentestTargetHost = host) }
  }

  fun updateIgUsername(username: String) {
    _uiState.update { it.copy(igUsername = username) }
  }

  fun updateIgPhotoUri(uri: Uri?) {
    soundEngine.playBeep()
    _uiState.update { it.copy(igPhotoUri = uri) }
  }

  fun selectNasaTarget(target: NasaTarget) {
    soundEngine.playBeep()
    _uiState.update { it.copy(selectedNasaTarget = target) }
  }

  fun updateFriendNickname(nickname: String) {
    _uiState.update { it.copy(friendNickname = nickname) }
  }

  fun scanFakeWifi() {
    wifiScanJob?.cancel()
    soundEngine.playBeep()
    _uiState.update { it.copy(isScanningWifi = true, wifiNetworks = emptyList()) }
    wifiScanJob = viewModelScope.launch {
      delay(900)
      soundEngine.playBeep()
      _uiState.update {
        it.copy(
          isScanningWifi = false,
          wifiNetworks = terminalEngine.generateFakeWifiNetworks()
        )
      }
    }
  }

  fun selectWifiAndHack(network: FakeWifiNetwork) {
    _uiState.update { it.copy(selectedWifi = network) }
    startSimulation()
  }

  fun markWifiPasswordCopied() {
    soundEngine.playBeep()
    _uiState.update { it.copy(wifiPasswordCopied = true) }
  }

  fun triggerNasaPrankAlarm() {
    soundEngine.playAlarm()
    soundEngine.vibrateSuccess()
    _uiState.update { it.copy(glitchTrigger = it.glitchTrigger + 1) }
  }

  fun startSimulation() {
    val currentMode = _uiState.value.selectedMode ?: return
    simulationJob?.cancel()
    soundEngine.playBeep()

    val stateSnapshot = _uiState.value
    val targetLabel = resolveTargetLabel(stateSnapshot)
    val initialLines = buildList {
      if (currentMode == HackMode.PENTEST_TOOLS) {
        add("guest@prank-terminal:~# ${terminalEngine.getPentestCommandPreview(stateSnapshot.selectedPentestTool, targetLabel)}")
      }
      addAll(
        terminalEngine.nextTerminalBatch(
          mode = currentMode,
          targetLabel = targetLabel,
          lang = stateSnapshot.language,
          pentestTool = stateSnapshot.selectedPentestTool,
          count = 4
        )
      )
    }

    _uiState.update { state ->
      state.copy(
        phase = HackPhase.HACKING,
        progress = 4,
        statusStage = terminalEngine.getStageLabel(currentMode, 4, state.language, state.selectedPentestTool),
        terminalLines = initialLines,
        keystrokesCount = 0,
        isAlarmActive = currentMode == HackMode.NASA,
        nasaCountdownSec = 15,
        wifiPasswordCopied = false,
        igFakeDms = emptyList(),
        friendDotProgress = 0.1f,
        glitchTrigger = state.glitchTrigger + 1
      )
    }

    simulationJob = viewModelScope.launch {
      var tick = 0
      while (_uiState.value.phase == HackPhase.HACKING && _uiState.value.progress < 100) {
        delay(280)
        tick++
        val state = _uiState.value
        val nextProgress = (state.progress + 3).coerceAtMost(100)
        val newBatch = terminalEngine.nextTerminalBatch(
          mode = currentMode,
          targetLabel = targetLabel,
          lang = state.language,
          pentestTool = state.selectedPentestTool,
          count = 2
        )
        val mergedLines = (state.terminalLines + newBatch).takeLast(90)

        if (currentMode == HackMode.NASA && tick % 4 == 0) {
          soundEngine.playAlarm()
        } else if (tick % 3 == 0) {
          soundEngine.playBeep()
        }

        val nextDot = ((tick % 20) / 20f).coerceIn(0.1f, 0.9f)
        val nextCountdown = (15 - (tick / 3)).coerceAtLeast(1)
        val dms = if (currentMode == HackMode.INSTAGRAM && nextProgress >= 75 && state.igFakeDms.isEmpty()) {
          terminalEngine.generateFakeInstagramDms(state.igUsername, state.language)
        } else {
          state.igFakeDms
        }

        _uiState.update {
          it.copy(
            progress = nextProgress,
            statusStage = terminalEngine.getStageLabel(currentMode, nextProgress, it.language, it.selectedPentestTool),
            terminalLines = mergedLines,
            igFakeDms = dms,
            nasaCountdownSec = nextCountdown,
            friendDotProgress = nextDot,
            glitchTrigger = if (currentMode == HackMode.NASA && tick % 3 == 0) it.glitchTrigger + 1 else it.glitchTrigger
          )
        }

        if (nextProgress >= 100) {
          completeSimulation()
          break
        }
      }
    }
  }

  fun onHackerKeyPress() {
    val state = _uiState.value
    val mode = state.selectedMode ?: return
    if (state.phase != HackPhase.HACKING) return

    soundEngine.playKeyClick()

    val targetLabel = resolveTargetLabel(state)
    val burst = terminalEngine.nextTerminalBatch(
      mode = mode,
      targetLabel = targetLabel,
      lang = state.language,
      pentestTool = state.selectedPentestTool,
      count = 4
    )
    val nextProgress = (state.progress + 5).coerceAtMost(100)
    val merged = (state.terminalLines + burst).takeLast(90)
    val dms = if (mode == HackMode.INSTAGRAM && nextProgress >= 75 && state.igFakeDms.isEmpty()) {
      terminalEngine.generateFakeInstagramDms(state.igUsername, state.language)
    } else {
      state.igFakeDms
    }

    _uiState.update {
      it.copy(
        progress = nextProgress,
        statusStage = terminalEngine.getStageLabel(mode, nextProgress, it.language, it.selectedPentestTool),
        terminalLines = merged,
        igFakeDms = dms,
        keystrokesCount = it.keystrokesCount + 1,
        glitchTrigger = it.glitchTrigger + 1
      )
    }

    if (nextProgress >= 100) {
      simulationJob?.cancel()
      completeSimulation()
    }
  }

  private fun completeSimulation() {
    val state = _uiState.value
    val mode = state.selectedMode ?: return
    val lang = state.language
    soundEngine.playAccessGranted()
    soundEngine.vibrateSuccess()

    val friendDetail = if (mode == HackMode.FRIEND_TRACKER) {
      terminalEngine.pickRandomFriendDetail(lang)
    } else {
      null
    }

    val finalDms = if (mode == HackMode.INSTAGRAM && state.igFakeDms.isEmpty()) {
      terminalEngine.generateFakeInstagramDms(state.igUsername, lang)
    } else {
      state.igFakeDms
    }

    _uiState.update {
      it.copy(
        phase = HackPhase.COMPLETED,
        progress = 100,
        statusStage = terminalEngine.getStageLabel(mode, 100, lang, it.selectedPentestTool),
        isAlarmActive = false,
        igFakeDms = finalDms,
        friendDetail = friendDetail,
        friendLocationText = friendDetail?.location ?: "",
        glitchTrigger = it.glitchTrigger + 1
      )
    }
  }

  fun revealInstagramPrank() {
    soundEngine.playAccessGranted()
    soundEngine.vibrateSuccess()
    _uiState.update {
      it.copy(
        phase = HackPhase.PRANK_REVEALED,
        glitchTrigger = it.glitchTrigger + 1
      )
    }
  }

  fun resetCurrentModeToSetup() {
    simulationJob?.cancel()
    wifiScanJob?.cancel()
    soundEngine.playBeep()
    val mode = _uiState.value.selectedMode ?: return
    _uiState.update {
      it.copy(
        phase = HackPhase.SETUP,
        progress = 0,
        statusStage = terminalEngine.getStageLabel(mode, 0, it.language, it.selectedPentestTool),
        terminalLines = emptyList(),
        keystrokesCount = 0,
        isAlarmActive = false,
        wifiPasswordCopied = false,
        igFakeDms = emptyList()
      )
    }
  }

  fun onVolumeDownPressed(nowMillis: Long = System.currentTimeMillis()): Boolean {
    if (_uiState.value.selectedMode == null) return false

    volumeDownTimestamps.add(nowMillis)
    volumeDownTimestamps.removeAll { nowMillis - it > 1600L }

    if (volumeDownTimestamps.size >= 3) {
      volumeDownTimestamps.clear()
      panicExitToMenu()
    }
    return true
  }

  fun panicExitToMenu() {
    simulationJob?.cancel()
    wifiScanJob?.cancel()
    volumeDownTimestamps.clear()
    _uiState.update { state ->
      state.copy(
        selectedMode = null,
        isInCalculatorDisguise = state.stealthCalculatorEnabled,
        phase = HackPhase.SETUP,
        progress = 0,
        terminalLines = emptyList(),
        keystrokesCount = 0,
        isAlarmActive = false,
        wifiPasswordCopied = false,
        igFakeDms = emptyList()
      )
    }
  }

  private fun resolveTargetLabel(state: HackUiState): String = when (state.selectedMode) {
    HackMode.INSTAGRAM -> state.igUsername.trim().removePrefix("@")
    HackMode.WIFI -> state.selectedWifi?.ssid ?: "WIFI-WARUNG-SEBELAH"
    HackMode.NASA -> state.selectedNasaTarget.id
    HackMode.FRIEND_TRACKER -> state.friendNickname.ifBlank { "Budi_Santuy" }
    HackMode.PENTEST_TOOLS -> state.pentestTargetHost
    null -> "SERVER-PURA-PURA-01"
  }

  override fun onCleared() {
    super.onCleared()
    soundEngine.release()
  }
}
