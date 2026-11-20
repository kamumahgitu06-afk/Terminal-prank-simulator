package com.example.model

import android.net.Uri

enum class AppLanguage(val code: String, val label: String) {
  INDONESIAN("ID", "INDONESIA"),
  ENGLISH("EN", "ENGLISH")
}

enum class HackMode {
  INSTAGRAM,
  WIFI,
  NASA,
  FRIEND_TRACKER,
  PENTEST_TOOLS
}

enum class PentestTool(
  val cliName: String,
  val badge: String,
  val descId: String,
  val descEn: String
) {
  PRANK_SCAN("prank-scan", "LUCU & FIKSI", "Simulasi pemindai jaringan pura-pura mode lucu", "Simulated funny fictional network scanner"),
  FAKE_HACK("fake-hack", "TERMINAL FILM", "Simulasi terminal ala film hacker untuk bercanda", "Movie-style pretend hacker terminal prank"),
  MEME_DECRYPT("meme-decrypt", "CARI SANDI", "Simulasi cari password palsu super kocak", "Simulated funny fake password finder"),
  ORBIT_PING("orbit-ping", "SATELIT FIKSI", "Simulasi ping stasiun satelit Orbit-X fiksi", "Simulated fictional Orbit-X satellite ping"),
  MATRIX_DUMP("matrix-dump", "LOG MATRIX", "Simulasi aliran kode hijau ala film sci-fi", "Simulated sci-fi green code stream")
}

enum class HackPhase {
  SETUP,
  HACKING,
  COMPLETED,
  PRANK_REVEALED
}

data class FakeWifiNetwork(
  val ssid: String,
  val bssid: String,
  val security: String,
  val signalStrength: Int,
  val channel: Int
)

data class NasaTarget(
  val id: String,
  val codeName: String,
  val coordinates: String
)

data class FakeDirectMessage(
  val sender: String,
  val message: String,
  val time: String
)

data class FriendTrackingDetail(
  val location: String,
  val distance: String,
  val activity: String
)

data class HackUiState(
  val language: AppLanguage = AppLanguage.INDONESIAN,
  val showInitialDisclaimer: Boolean = true,
  val showPrivacyDialog: Boolean = false,
  val selectedMode: HackMode? = null,
  val phase: HackPhase = HackPhase.SETUP,
  val isSoundEnabled: Boolean = true,
  val stealthCalculatorEnabled: Boolean = false,
  val isInCalculatorDisguise: Boolean = false,
  val progress: Int = 0,
  val statusStage: String = "",
  val terminalLines: List<String> = emptyList(),
  val keystrokesCount: Int = 0,
  val glitchTrigger: Int = 0,
  // Mode 1: Social Media Prank (fictional)
  val igUsername: String = "contoh_target_sim",
  val igPhotoUri: Uri? = null,
  val igFakeDms: List<FakeDirectMessage> = emptyList(),
  // Mode 2: Fake Wi-Fi Prank (fictional)
  val isScanningWifi: Boolean = false,
  val wifiNetworks: List<FakeWifiNetwork> = emptyList(),
  val selectedWifi: FakeWifiNetwork? = null,
  val revealedWifiPassword: String = "Prank123",
  val wifiPasswordCopied: Boolean = false,
  // Mode 3: Fictional Satellite Orbit-X Prank
  val selectedNasaTarget: NasaTarget = NasaTarget("ORBIT_X_LAB", "ORBIT-X SPACE LAB (FIKSI)", "00.0000° N, 00.0000° E"),
  val isAlarmActive: Boolean = false,
  val nasaCountdownSec: Int = 15,
  // Mode 4: Guess Friend Location Prank (nickname only, no phone numbers)
  val friendNickname: String = "Budi_Santuy",
  val friendLocationText: String = "",
  val friendDetail: FriendTrackingDetail? = null,
  val friendDotProgress: Float = 0.25f,
  // Mode 5: Pretend Terminal Simulator (fictional targets only, no real IPs)
  val selectedPentestTool: PentestTool = PentestTool.PRANK_SCAN,
  val pentestTargetHost: String = "SERVER-PURA-PURA-01"
)

object AppStrings {
  val fakeTerminalTargets: List<String> = listOf(
    "SERVER-PURA-PURA-01",
    "SATELIT-ORBIT-X",
    "WIFI-WARUNG-SEBELAH"
  )

  fun disclaimerHeadline(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PERINGATAN // SIMULATOR PRANK"
    AppLanguage.ENGLISH -> "WARNING // PRANK SIMULATOR"
  }

  fun disclaimerBody(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN ->
      "INI HANYA PRANK / SIMULATOR UNTUK HIBURAN. TIDAK MENG-HACK APAPUN."
    AppLanguage.ENGLISH ->
      "THIS IS ONLY A PRANK / SIMULATOR FOR ENTERTAINMENT. DOES NOT HACK ANYTHING."
  }

  fun disclaimerSubNote(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN ->
      "100% Offline & aman. Tidak mengakses internet, akun, Wi-Fi, satelit, atau lokasi asli. Tidak berafiliasi dengan pihak mana pun."
    AppLanguage.ENGLISH ->
      "100% Offline & safe. Does not access the internet, real accounts, Wi-Fi, satellites, or real locations. Not affiliated with any third party."
  }

  fun modeDisclaimerBadge(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN ->
      "PRANK SIMULATOR • BUKAN HACK ASLI • 100% PALSU & AMAN"
    AppLanguage.ENGLISH ->
      "PRANK SIMULATOR • NOT REAL HACKING • 100% FAKE & SAFE"
  }

  fun acceptAndEnter(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PAHAM & MASUK TERMINAL"
    AppLanguage.ENGLISH -> "UNDERSTOOD & ENTER TERMINAL"
  }

  fun privacyTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "KEBIJAKAN PRIVASI & KETENTUAN"
    AppLanguage.ENGLISH -> "PRIVACY POLICY & TERMS"
  }

  fun privacyBody(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN ->
      "Terminal Prank Simulator (by goanonim_17) adalah aplikasi hiburan 100% offline.\n\n" +
        "Kami TIDAK mengumpulkan, TIDAK menyimpan, dan TIDAK membagikan data pribadi pengguna apapun. Aplikasi tidak memerlukan login, tidak menggunakan internet, tidak menggunakan Firebase, dan tidak melacak lokasi asli pengguna.\n\n" +
        "Semua foto yang dipilih lewat galeri hanya diproses secara lokal di perangkat dan tidak diunggah ke mana pun. Semua hasil, password, dan lokasi yang ditampilkan adalah simulasi palsu untuk hiburan."
    AppLanguage.ENGLISH ->
      "Terminal Prank Simulator (by goanonim_17) is a 100% offline entertainment app.\n\n" +
        "We do NOT collect, store, or share any personal user data. The app requires no login, uses no internet permission, uses no Firebase, and never tracks real locations.\n\n" +
        "Any photo picked from your gallery is processed locally on-device only. All terminal outputs, passwords, and locations shown are fake simulations for entertainment."
  }

  fun closeButton(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "TUTUP"
    AppLanguage.ENGLISH -> "CLOSE"
  }

  fun menuSubtitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PILIH MODUL SIMULASI PRANK"
    AppLanguage.ENGLISH -> "SELECT PRANK SIMULATION MODULE"
  }

  fun panicTip(lang: AppLanguage, stealthCalculator: Boolean): String = when (lang) {
    AppLanguage.INDONESIAN -> if (stealthCalculator) {
      "TOMBOL RAHASIA: Volume Bawah 3x atau Swipe 2 Jari -> Samaran Kalkulator!"
    } else {
      "TOMBOL RAHASIA: Tekan Volume Bawah 3x atau Swipe 2 Jari untuk kabur ke Menu!"
    }
    AppLanguage.ENGLISH -> if (stealthCalculator) {
      "SECRET EXIT: Volume Down 3x or 2-Finger Swipe -> Calculator Disguise!"
    } else {
      "SECRET EXIT: Press Volume Down 3x or 2-Finger Swipe to escape to Menu!"
    }
  }

  fun stealthToggleTitle(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "Mode Samaran Kalkulator saat Kabur"
    AppLanguage.ENGLISH -> "Calculator Disguise on Panic Exit"
  }

  fun stealthToggleDesc(lang: AppLanguage, enabled: Boolean): String = when (lang) {
    AppLanguage.INDONESIAN -> if (enabled) {
      "AKTIF: Tombol rahasia ubah layar jadi Kalkulator (tahan '=' untuk balik)"
    } else {
      "NONAKTIF: Tombol rahasia langsung kembali ke Menu Utama"
    }
    AppLanguage.ENGLISH -> if (enabled) {
      "ON: Secret exit switches to Calculator (long-press '=' to return)"
    } else {
      "OFF: Secret exit returns directly to Main Menu"
    }
  }

  fun modeTitle(mode: HackMode, lang: AppLanguage): String = when (mode) {
    HackMode.INSTAGRAM -> if (lang == AppLanguage.INDONESIAN) "Prank Sosmed Simulator" else "Social Prank Simulator"
    HackMode.WIFI -> if (lang == AppLanguage.INDONESIAN) "Prank WiFi Palsu" else "Fake WiFi Prank"
    HackMode.NASA -> if (lang == AppLanguage.INDONESIAN) "Prank Satelit Orbit-X (Fiksi)" else "Orbit-X Satellite Prank (Fictional)"
    HackMode.FRIEND_TRACKER -> if (lang == AppLanguage.INDONESIAN) "Tebak Lokasi Teman (Prank)" else "Guess Friend Location (Prank)"
    HackMode.PENTEST_TOOLS -> if (lang == AppLanguage.INDONESIAN) "Terminal Pura-Pura" else "Pretend Terminal"
  }

  fun modeDesc(mode: HackMode, lang: AppLanguage): String = when (mode) {
    HackMode.INSTAGRAM -> if (lang == AppLanguage.INDONESIAN) {
      "Simulasi prank sosmed fiktif, pilih foto galeri, DM palsu & jebakan sandi"
    } else {
      "Fake social prank with gallery photo picker, fake DMs & password prank"
    }
    HackMode.WIFI -> if (lang == AppLanguage.INDONESIAN) {
      "Scan daftar Wi-Fi fiktif, cari password palsu & tampilkan sandi Prank123"
    } else {
      "Scan fictional Wi-Fi list, find fake password & reveal Prank123"
    }
    HackMode.NASA -> if (lang == AppLanguage.INDONESIAN) {
      "Peta dunia fiksi, radar berputar, HUD satelit Orbit-X, flash merah & alarm"
    } else {
      "Fictional world map, rotating radar HUD, Orbit-X uplink, red flash & siren"
    }
    HackMode.FRIEND_TRACKER -> if (lang == AppLanguage.INDONESIAN) {
      "Masukkan nama panggilan teman, lihat titik merah & tebak lokasi kocak"
    } else {
      "Enter friend nickname, watch moving red dot & funny location guess"
    }
    HackMode.PENTEST_TOOLS -> if (lang == AppLanguage.INDONESIAN) {
      "Simulasi perintah terminal lucu: prank-scan, fake-hack, meme-decrypt & lainnya"
    } else {
      "Funny pretend terminal commands: prank-scan, fake-hack, meme-decrypt & more"
    }
  }

  fun selectPentestToolHeader(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PILIH PERINTAH PURA-PURA (100% PALSU):"
    AppLanguage.ENGLISH -> "SELECT PRETEND COMMAND (100% FAKE):"
  }

  fun targetHostLabel(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PILIH TARGET PALSU:"
    AppLanguage.ENGLISH -> "SELECT FAKE TARGET:"
  }

  fun commandPreviewLabel(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PRATINJAU PERINTAH PRANK:"
    AppLanguage.ENGLISH -> "PRANK COMMAND PREVIEW:"
  }

  fun runToolButton(lang: AppLanguage, toolName: String): String = when (lang) {
    AppLanguage.INDONESIAN -> "JALANKAN $toolName (PRANK)"
    AppLanguage.ENGLISH -> "RUN $toolName (PRANK)"
  }

  fun startBreach(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "MULAI SIMULASI PRANK"
    AppLanguage.ENGLISH -> "START PRANK SIMULATION"
  }

  fun pickGalleryPhoto(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PILIH FOTO PROFIL DARI GALERI"
    AppLanguage.ENGLISH -> "PICK PROFILE PHOTO FROM GALLERY"
  }

  fun targetUsernameLabel(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "Username Contoh (@)"
    AppLanguage.ENGLISH -> "Sample Username (@)"
  }

  fun friendNicknameLabel(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "Nama Panggilan Teman"
    AppLanguage.ENGLISH -> "Friend Nickname"
  }

  fun availableRoutersHeader(lang: AppLanguage, isScanning: Boolean): String = when (lang) {
    AppLanguage.INDONESIAN -> if (isScanning) "MEMINDAI WIFI PALSU..." else "DAFTAR WIFI PALSU (FIKTIF):"
    AppLanguage.ENGLISH -> if (isScanning) "SCANNING FAKE WIFI..." else "FICTIONAL WIFI LIST:"
  }

  fun scanBtnLabel(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PINDAI"
    AppLanguage.ENGLISH -> "SCAN"
  }

  fun selectNasaNodeHeader(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PILIH NODE SATELIT FIKSI:"
    AppLanguage.ENGLISH -> "SELECT FICTIONAL ORBITAL NODE:"
  }

  fun autoTypeHint(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "KETIK SEMBARANGAN UNTUK KODE SIMULASI!"
    AppLanguage.ENGLISH -> "MASH ANY KEYS TO AUTO-TYPE CODE!"
  }

  fun keysCounterLabel(lang: AppLanguage, count: Int): String = when (lang) {
    AppLanguage.INDONESIAN -> "TOMBOL: $count"
    AppLanguage.ENGLISH -> "KEYS: $count"
  }

  fun igDmDumpHeader(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "CUPLIKAN CHAT SIMULASI (PALSU):"
    AppLanguage.ENGLISH -> "SIMULATED CHAT CACHE (FAKE):"
  }

  fun igPasswordFound(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "PASSWORD DITEMUKAN: ********"
    AppLanguage.ENGLISH -> "PASSWORD FOUND: ********"
  }

  fun viewPasswordBtn(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "LIHAT PASSWORD"
    AppLanguage.ENGLISH -> "VIEW PASSWORD"
  }

  fun igPrankRevealMessage(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "Kena Prank! Ini cuma simulator!"
    AppLanguage.ENGLISH -> "You Got Pranked! This is just a simulator!"
  }

  fun wifiPasswordResult(lang: AppLanguage, password: String): String = when (lang) {
    AppLanguage.INDONESIAN -> "PASSWORD WIFI: $password"
    AppLanguage.ENGLISH -> "WIFI PASSWORD: $password"
  }

  fun copyWifiPasswordBtn(lang: AppLanguage, copied: Boolean): String = when (lang) {
    AppLanguage.INDONESIAN -> if (copied) "TERSALIN! TAPI TETAP KENA PRANK :D" else "SALIN PASSWORD WIFI"
    AppLanguage.ENGLISH -> if (copied) "COPIED! STILL JUST A PRANK :D" else "COPY WIFI PASSWORD"
  }

  fun nasaGrantedBanner(lang: AppLanguage): String =
    "ACCESS GRANTED // ORBIT-X PRANK SIMULATOR"

  fun nasaAlarmReplayBtn(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "BUNYIKAN ALARM PRANK"
    AppLanguage.ENGLISH -> "TRIGGER PRANK SIREN"
  }

  fun friendLocationResult(lang: AppLanguage, location: String): String = when (lang) {
    AppLanguage.INDONESIAN -> "TEMANMU SEDANG DI: $location"
    AppLanguage.ENGLISH -> "YOUR FRIEND IS AT: $location"
  }

  fun friendTelemetrySub(lang: AppLanguage, detail: FriendTrackingDetail?): String {
    if (detail == null) return disclaimerBody(lang)
    return when (lang) {
      AppLanguage.INDONESIAN ->
        "Jarak: ${detail.distance} • Aktivitas: ${detail.activity} (100% Palsu)"
      AppLanguage.ENGLISH ->
        "Distance: ${detail.distance} • Activity: ${detail.activity} (100% Fake)"
    }
  }

  fun hackSuccessBanner(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "SIMULASI SELESAI // HANYA PRANK"
    AppLanguage.ENGLISH -> "SIMULATION COMPLETE // PRANK ONLY"
  }

  fun restartSimulation(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "ULANGI SIMULASI"
    AppLanguage.ENGLISH -> "RUN SIMULATION AGAIN"
  }

  fun targetSsidPrefix(lang: AppLanguage): String = when (lang) {
    AppLanguage.INDONESIAN -> "TARGET WIFI:"
    AppLanguage.ENGLISH -> "TARGET SSID:"
  }

  fun gpsStatusLabel(lang: AppLanguage, isLocked: Boolean): String = when (lang) {
    AppLanguage.INDONESIAN -> if (isLocked) "TEBAKAN DIKUNCI" else "MENEBAK..."
    AppLanguage.ENGLISH -> if (isLocked) "GUESS LOCKED" else "GUESSING..."
  }
}
