package com.example.engine

import com.example.model.AppLanguage
import com.example.model.FakeDirectMessage
import com.example.model.FakeWifiNetwork
import com.example.model.FriendTrackingDetail
import com.example.model.HackMode
import com.example.model.NasaTarget
import com.example.model.PentestTool
import kotlin.random.Random

class FakeTerminalEngine(
  private val random: Random = Random.Default
) {

  private var lineCursor = 0

  val fakeNasaTargets: List<NasaTarget> = listOf(
    NasaTarget("ORBIT_X_LAB", "ORBIT-X SPACE LAB (FIKSI)", "00.0000° N, 00.0000° E"),
    NasaTarget("GALAXY_RELAY_7", "GALAXY ORBITAL RELAY-07", "12.3456° N, 45.6789° W"),
    NasaTarget("CYBER_ARCHIVE", "CYBER ARCHIVE SANDBOX", "06.2088° S, 106.8456° E")
  )

  fun getPentestCommandPreview(tool: PentestTool, targetHost: String): String {
    val target = targetHost.trim().ifBlank { "SERVER-PURA-PURA-01" }
    return when (tool) {
      PentestTool.PRANK_SCAN -> "prank-scan --mode lucu --target $target"
      PentestTool.FAKE_HACK -> "fake-hack --target $target --theme cyber-film"
      PentestTool.MEME_DECRYPT -> "meme-decrypt --cari-password-palsu $target"
      PentestTool.ORBIT_PING -> "orbit-ping --satelit $target --fictional"
      PentestTool.MATRIX_DUMP -> "matrix-dump --stream green --sandbox $target"
    }
  }

  fun getPentestResultSummary(tool: PentestTool, targetHost: String, lang: AppLanguage): String {
    val target = targetHost.trim().ifBlank { "SERVER-PURA-PURA-01" }
    return when (tool) {
      PentestTool.PRANK_SCAN -> if (lang == AppLanguage.INDONESIAN) {
        "PRANK-SCAN SELESAI: 4 Port Lucu Terbuka (Kopi, Bakso, WiFi, Meme) pada $target [PALSU]"
      } else {
        "PRANK-SCAN DONE: 4 Funny Ports Open (Coffee, Noodles, Meme, Fun) on $target [FAKE]"
      }
      PentestTool.FAKE_HACK -> if (lang == AppLanguage.INDONESIAN) {
        "FAKE-HACK SELESAI: Akses Film Sci-Fi terbuka di $target | Sandi: CumaPrank123 [PALSU]"
      } else {
        "FAKE-HACK DONE: Sci-Fi Terminal Unlocked on $target | Pass: JustAPrank123 [FAKE]"
      }
      PentestTool.MEME_DECRYPT -> if (lang == AppLanguage.INDONESIAN) {
        "CARI PASSWORD PALSU SELESAI: Sandi ditemukan -> 'NasiGorengPedas99' di $target [PRANK]"
      } else {
        "FAKE PASSWORD FOUND: Secret key -> 'SpicyNoodles99' on $target [PRANK]"
      }
      PentestTool.ORBIT_PING -> if (lang == AppLanguage.INDONESIAN) {
        "ORBIT-PING SELESAI: Sinyal Satelit Fiksi $target tersambung (0 ms / 100% Prank)"
      } else {
        "ORBIT-PING DONE: Fictional Satellite $target linked (0 ms / 100% Prank)"
      }
      PentestTool.MATRIX_DUMP -> if (lang == AppLanguage.INDONESIAN) {
        "MATRIX-DUMP SELESAI: 999 baris kode hijau fiksi sukses dirender untuk $target"
      } else {
        "MATRIX-DUMP DONE: 999 fictional green code lines rendered for $target"
      }
    }
  }

  fun generateFakeWifiNetworks(): List<FakeWifiNetwork> {
    val candidates = listOf(
      FakeWifiNetwork("WIFI-WARUNG-SEBELAH", "A4:2B:8C:19:F0:4A", "SIM-PSK", -48, 36),
      FakeWifiNetwork("KopiKenangan_Sim_VIP", "00:1A:2B:77:41:9E", "SIM-PSK", -54, 6),
      FakeWifiNetwork("Router_PakRT_04", "D8:50:E6:3C:88:11", "SIM-PSK", -62, 11),
      FakeWifiNetwork("Mobil_Bakso_5G_VIP", "FC:FB:FB:01:02:09", "SIM-FIKSI", -69, 1),
      FakeWifiNetwork("Kos_Putri_Lt2_Fast", "9C:D2:1E:55:B2:7C", "SIM-PSK", -74, 44),
      FakeWifiNetwork("Hotspot_Prank_Only", "48:5D:60:AA:12:EF", "SIM-PSK", -81, 3)
    )
    return candidates.shuffled(random)
  }

  fun generateFakeInstagramDms(targetUsername: String, lang: AppLanguage): List<FakeDirectMessage> {
    val user = targetUsername.ifBlank { "bestie" }.removePrefix("@")
    return if (lang == AppLanguage.INDONESIAN) {
      listOf(
        FakeDirectMessage("@tukang_bakso99", "Bro @$user, kuah baksonya mau pedes apa nggak?", "2m"),
        FakeDirectMessage("@pak_rt_digital", "Jangan lupa ronda malam ini ya, bawa kopi!", "9m"),
        FakeDirectMessage("@admin_prank", "Kalau ada yg ngintip layar, bilang aja kena prank!", "14m")
      )
    } else {
      listOf(
        FakeDirectMessage("@snack_vendor", "Hey @$user, extra spicy sauce on your noodles?", "2m"),
        FakeDirectMessage("@night_watch", "Don't forget tonight's neighborhood patrol!", "9m"),
        FakeDirectMessage("@prank_admin", "Anyone peeking at your screen just got pranked!", "14m")
      )
    }
  }

  fun pickRandomFriendDetail(lang: AppLanguage): FriendTrackingDetail {
    val idPool = listOf(
      FriendTrackingDetail("WARUNG SEBELAH", "38 meter", "Lagi ngopi sambil scroll HP"),
      FriendTrackingDetail("POS RONDA RT 04", "120 meter", "Main catur sama Pak RT"),
      FriendTrackingDetail("ANGKRINGAN MAS JOKO", "85 meter", "Nambah sate usus 3 tusuk"),
      FriendTrackingDetail("KANTIN LAGI NGUTANG", "210 meter", "Nunggu kasir lengah"),
      FriendTrackingDetail("TUKANG BAKSO LEWAT", "19 meter", "Pesen bakso urat ekstra sambal"),
      FriendTrackingDetail("WARNET ABADI BILIK 09", "340 meter", "Push rank sampai subuh")
    )
    val enPool = listOf(
      FriendTrackingDetail("THE SNACK STALL NEXT DOOR", "38 meters", "Ordering iced tea & fried snacks"),
      FriendTrackingDetail("NEIGHBORHOOD GUARD POST", "120 meters", "Playing chess with locals"),
      FriendTrackingDetail("CORNER COFFEE CART", "85 meters", "Adding extra espresso shots"),
      FriendTrackingDetail("CAMPUS CAFETERIA", "210 meters", "Looking for free snacks"),
      FriendTrackingDetail("STREET NOODLE STAND", "19 meters", "Waiting for extra spicy broth"),
      FriendTrackingDetail("CYBER CAFE BOOTH 09", "340 meters", "Grinding ranked matches")
    )
    val pool = if (lang == AppLanguage.INDONESIAN) idPool else enPool
    return pool[random.nextInt(pool.size)]
  }

  fun pickRandomFriendLocation(lang: AppLanguage): String =
    pickRandomFriendDetail(lang).location

  fun getStageLabel(
    mode: HackMode,
    progress: Int,
    lang: AppLanguage,
    pentestTool: PentestTool = PentestTool.PRANK_SCAN
  ): String {
    return when (mode) {
      HackMode.INSTAGRAM -> when {
        progress < 25 -> if (lang == AppLanguage.INDONESIAN) "Menghubungkan ke server palsu..." else "Connecting to fake server..."
        progress < 50 -> if (lang == AppLanguage.INDONESIAN) "Melewati firewall fiksi..." else "Passing fictional firewall..."
        progress < 75 -> if (lang == AppLanguage.INDONESIAN) "Cari Password Palsu..." else "Finding Fake Password..."
        progress < 100 -> if (lang == AppLanguage.INDONESIAN) "Membuat DM palsu lucu..." else "Generating funny fake DMs..."
        else -> if (lang == AppLanguage.INDONESIAN) "SIMULASI PRANK SELESAI" else "PRANK SIMULATION COMPLETE"
      }
      HackMode.WIFI -> when {
        progress < 30 -> if (lang == AppLanguage.INDONESIAN) "Memindai WiFi Palsu..." else "Scanning Fake WiFi..."
        progress < 65 -> if (lang == AppLanguage.INDONESIAN) "Cari Password Palsu..." else "Finding Fake Password..."
        progress < 100 -> if (lang == AppLanguage.INDONESIAN) "Menyiapkan Kejutan Prank..." else "Preparing Prank Surprise..."
        else -> if (lang == AppLanguage.INDONESIAN) "PASSWORD PALSU DITEMUKAN" else "FAKE PASSWORD FOUND"
      }
      HackMode.NASA -> when {
        progress < 35 -> "ACCESSING ORBIT-X SATELLITE..."
        progress < 70 -> "TRACKING FICTIONAL ORBIT..."
        progress < 100 -> if (lang == AppLanguage.INDONESIAN) "MENGAMBIL ALIH TELEMETRI FIKSI..." else "OVERRIDING FICTIONAL TELEMETRY..."
        else -> "ACCESS GRANTED // ORBIT-X PRANK"
      }
      HackMode.FRIEND_TRACKER -> when {
        progress < 30 -> if (lang == AppLanguage.INDONESIAN) "MEMULAI RADAR KOCAK..." else "STARTING FUNNY RADAR..."
        progress < 65 -> if (lang == AppLanguage.INDONESIAN) "MENEBAK LOKASI FIKSI..." else "GUESSING FICTIONAL SPOT..."
        progress < 100 -> if (lang == AppLanguage.INDONESIAN) "MENGUNCI TEBAKAN LUCU..." else "LOCKING FUNNY GUESS..."
        else -> if (lang == AppLanguage.INDONESIAN) "TEBAKAN LOKASI SELESAI (PRANK)" else "LOCATION GUESS READY (PRANK)"
      }
      HackMode.PENTEST_TOOLS -> when {
        progress < 30 -> "${pentestTool.cliName}: menyiapkan mode lucu..."
        progress < 65 -> "${pentestTool.cliName}: memproses target fiksi..."
        progress < 100 -> "${pentestTool.cliName}: merender output palsu..."
        else -> "${pentestTool.cliName}: SIMULASI PRANK SELESAI"
      }
    }
  }

  fun nextTerminalBatch(
    mode: HackMode,
    targetLabel: String,
    lang: AppLanguage = AppLanguage.INDONESIAN,
    pentestTool: PentestTool = PentestTool.PRANK_SCAN,
    count: Int = 3
  ): List<String> {
    val lines = mutableListOf<String>()
    val cleanTarget = targetLabel.ifBlank { "SERVER-PURA-PURA-01" }
    val scriptPool = when (mode) {
      HackMode.INSTAGRAM -> listOf(
        "[SIM] prank-connect --sandbox @$cleanTarget --offline",
        "[AUTH] Social demo probe @$cleanTarget -> 200 OK (PALSU)",
        "[FIREWALL] Fictional barrier check seq=0x${hex(4)} -> PASS",
        "[TLS] Mock film cipher lock -> OK (SIMULASI)",
        "[PASS] Cari Password Palsu #${100 + lineCursor * 17}: ********",
        "[MEME] Candidate hash: 0x${hex(8)} -> PRANK_ONLY",
        if (lang == AppLanguage.INDONESIAN) {
          "[DUMP] Menyiapkan chat palsu lucu [bagian ${lineCursor % 12 + 1}/12]"
        } else {
          "[DUMP] Generating funny fake chat [chunk ${lineCursor % 12 + 1}/12]"
        },
        "[TOKEN] Mock token -> PRANK_SIM_TOKEN_${hex(4)}"
      )
      HackMode.WIFI -> listOf(
        "[WLAN] prank-wifi --scan-fictional --channel 6",
        "[SIM] Target SSID fiktif: '$cleanTarget' (100% Palsu)",
        "[SIGNAL] Mock beacon frame #${lineCursor + 12} -> OK",
        "[PASS] Cari Password Palsu... cek kamus lucu #${lineCursor + 1}",
        "[DECODE] Mock cipher stream 0x${hex(6)} -> SIMULASI",
        "[SPEED] Kecepatan animasi palsu [${1240 + lineCursor * 89} baris/s]",
        "[CHECK] Verifikasi hasil hiburan 0x${hex(8)} -> COCOK (PALSU)",
        "[KEY] Password WiFi Palsu ditemukan -> Prank123"
      )
      HackMode.NASA -> listOf(
        "[UPLINK] Fictional Ka-band link -> $cleanTarget",
        "[SAT] Orbit-X tracking vector: AZ=${(lineCursor * 23) % 360}.4° EL=48.2°",
        "[SHIELD] Passing fictional space relay node_${hex(2)}",
        "[HEX] 0x7F 0x45 0x4C 0x46 0x02 0x01 // SIMULATED",
        "[TELEMETRY] Mock stream locked: 14.285 GHz @ 4096-QAM",
        if (lang == AppLanguage.INDONESIAN) {
          "[ALARM] Sirine prank aktif -> menyalakan efek glitch!"
        } else {
          "[ALARM] Prank siren active -> triggering glitch effect!"
        },
        "[KERNEL] echo 'prank_mode=1' > /dev/sim_null",
        "[GRANTED] Fictional Orbit-X Prank Override COMPLETE"
      )
      HackMode.FRIEND_TRACKER -> listOf(
        "[RADAR] Memulai tebakan lokasi fiksi untuk '$cleanTarget'",
        "[SIGNAL] Mock tower ping: -61 dBm (100% Palsu)",
        "[GRID] Menyapu peta hiburan sektor SIM_${hex(2)}",
        "[MAP] Animasi titik merah bergerak ke warung terdekat...",
        "[MEME] Memeriksa stok gorengan & es teh di radar fiksi",
        "[PING] Mock echo reply: 14.2ms (CUMA PRANK)",
        if (lang == AppLanguage.INDONESIAN) {
          "[LOCK] Tebakan lokasi tongkrongan $cleanTarget siap!"
        } else {
          "[LOCK] Funny hangout guess for $cleanTarget is ready!"
        }
      )
      HackMode.PENTEST_TOOLS -> pentestScriptPool(pentestTool, cleanTarget)
    }

    repeat(count) {
      val template = scriptPool[(lineCursor + it) % scriptPool.size]
      lines.add(template)
    }
    lineCursor += count
    return lines
  }

  private fun pentestScriptPool(tool: PentestTool, target: String): List<String> = when (tool) {
    PentestTool.PRANK_SCAN -> listOf(
      "guest@prank-terminal:~# prank-scan --mode lucu --target $target",
      "[SIM] Memindai target fiksi $target (Mode Hiburan 100% Offline)",
      "[PORT] 8080/sim  -> terbuka (Layanan Kopi & Gorengan)",
      "[PORT] 1337/sim  -> terbuka (Meme Vault v2.0 - PALSU)",
      "[PORT] 4242/sim  -> terbuka (Server Warung Sebelah)",
      "[INFO] OS Target Fiksi: PrankOS 99.0 LTS (Hanya Simulasi)",
      "[DONE] Prank-scan selesai! Tidak ada jaringan asli yang dipindai."
    )
    PentestTool.FAKE_HACK -> listOf(
      "guest@prank-terminal:~# fake-hack --target $target --theme cyber-film",
      "[INIT] Menyalakan efek terminal ala film action...",
      "[BYPASS] Melewati firewall kardus di $target -> SUKSES (PALSU)",
      "[HEX] Dekode paket fiksi: 0x${hex(8)} -> 'CUMA_PRANK'",
      "[ACCESS] Membuka gerbang hiburan $target...",
      "[PASS] Cari Password Palsu... ketemu: 'CumaPrank123'",
      "[DONE] Fake-hack selesai! Bercanda berhasil dijalankan."
    )
    PentestTool.MEME_DECRYPT -> listOf(
      "guest@prank-terminal:~# meme-decrypt --cari-password-palsu $target",
      "[SIM] Memuat kamus kata lucu (bukan wordlist asli)...",
      "[TRY] Mencoba kata kunci 'BaksoUrat123' -> hampir...",
      "[TRY] Mencoba kata kunci 'WiFiGratisDong' -> hampir...",
      "[MATCH] Cari Password Palsu... KETEMU! (100% Fiksi)",
      "[KEY] $target -> 'NasiGorengPedas99' [SIMULASI PRANK]",
      "[DONE] Proses selesai dalam 0.02 detik (Simulasi Lokal)."
    )
    PentestTool.ORBIT_PING -> listOf(
      "guest@prank-terminal:~# orbit-ping --satelit $target --fictional",
      "[SAT] Mengarahkan antena imajinasi ke $target...",
      "[PING] Frame #1 -> 0x${hex(4)} ttl=99 time=0.1ms (FIKSI)",
      "[PING] Frame #2 -> 0x${hex(4)} ttl=99 time=0.1ms (FIKSI)",
      "[ORBIT] Ketinggian fiksi: 420 km di atas warung kopi",
      "[LOCK] Sinyal Orbit-X stabil — 100% untuk hiburan!"
    )
    PentestTool.MATRIX_DUMP -> listOf(
      "guest@prank-terminal:~# matrix-dump --stream green --sandbox $target",
      "[STREAM] 01001000 01000001 01000011 01001011 // PRANK",
      "[MATRIX] Rendering buffer hijau neon 0x${hex(6)} -> $target",
      "[GLITCH] Sinkronisasi efek CRT scanline... OK",
      "[MEM] Alokasi 0 KB data asli — 100% offline simulator",
      "[DONE] Matrix stream aktif. Ketik sembarangan untuk efek!"
    )
  }

  private fun hex(length: Int): String {
    val chars = "0123456789ABCDEF"
    return buildString(length) {
      repeat(length) {
        append(chars[random.nextInt(chars.length)])
      }
    }
  }
}
