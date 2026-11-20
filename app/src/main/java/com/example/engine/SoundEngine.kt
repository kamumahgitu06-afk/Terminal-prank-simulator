package com.example.engine

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.PI
import kotlin.math.sin

class SoundEngine(context: Context) {

  private val appContext = context.applicationContext
  private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

  @Volatile
  var isEnabled: Boolean = true

  private var tts: TextToSpeech? = null
  @Volatile
  private var ttsReady: Boolean = false

  init {
    try {
      tts = TextToSpeech(appContext) { status ->
        if (status == TextToSpeech.SUCCESS) {
          tts?.language = Locale.US
          tts?.setPitch(0.85f)
          tts?.setSpeechRate(1.05f)
          ttsReady = true
        }
      }
    } catch (_: Throwable) {
      ttsReady = false
    }
  }

  fun playKeyClick() {
    if (!isEnabled) return
    scope.launch {
      playTonePcm(
        freqHz = 1480.0,
        durationMs = 22,
        volume = 0.25,
        decay = true
      )
    }
  }

  fun playBeep() {
    if (!isEnabled) return
    scope.launch {
      playTonePcm(
        freqHz = 960.0,
        durationMs = 75,
        volume = 0.35,
        decay = true
      )
    }
  }

  fun playAlarm() {
    if (!isEnabled) return
    scope.launch {
      playTonePcm(freqHz = 880.0, durationMs = 130, volume = 0.45, decay = false)
      playTonePcm(freqHz = 620.0, durationMs = 130, volume = 0.45, decay = false)
    }
  }

  fun playAccessGranted() {
    if (!isEnabled) return
    scope.launch {
      playTonePcm(freqHz = 523.25, durationMs = 90, volume = 0.4, decay = false)
      playTonePcm(freqHz = 659.25, durationMs = 90, volume = 0.4, decay = false)
      playTonePcm(freqHz = 1046.5, durationMs = 220, volume = 0.45, decay = true)
    }
    try {
      if (ttsReady) {
        tts?.speak("Access Granted", TextToSpeech.QUEUE_FLUSH, null, "access_granted")
      }
    } catch (_: Throwable) {
      // Safe fallback when TTS is unavailable
    }
  }

  @Suppress("DEPRECATION")
  fun vibrateSuccess() {
    try {
      val pattern = longArrayOf(0, 90, 60, 140, 60, 220)
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val manager = appContext.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
        manager?.defaultVibrator?.vibrate(
          VibrationEffect.createWaveform(pattern, -1)
        )
      } else {
        val vibrator = appContext.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          vibrator?.vibrate(VibrationEffect.createWaveform(pattern, -1))
        } else {
          vibrator?.vibrate(pattern, -1)
        }
      }
    } catch (_: Throwable) {
      // Ignore on environments without vibrator hardware (e.g. unit tests)
    }
  }

  private fun playTonePcm(
    freqHz: Double,
    durationMs: Int,
    volume: Double,
    decay: Boolean
  ) {
    try {
      val sampleRate = 22050
      val numSamples = (sampleRate * durationMs) / 1000
      if (numSamples <= 0) return

      val samples = ShortArray(numSamples)
      for (i in 0 until numSamples) {
        val envelope = if (decay) {
          1.0 - (i.toDouble() / numSamples.toDouble())
        } else {
          1.0
        }
        val angle = 2.0 * PI * i.toDouble() * freqHz / sampleRate
        val raw = sin(angle) * Short.MAX_VALUE * volume * envelope
        samples[i] = raw.toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
      }

      val bufferBytes = samples.size * 2
      val track = AudioTrack.Builder()
        .setAudioAttributes(
          AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()
        )
        .setAudioFormat(
          AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()
        )
        .setBufferSizeInBytes(bufferBytes)
        .setTransferMode(AudioTrack.MODE_STATIC)
        .build()

      track.write(samples, 0, samples.size)
      track.play()
      Thread.sleep(durationMs.toLong() + 15L)
      track.release()
    } catch (_: Throwable) {
      // Safe no-op in headless/JVM test environments
    }
  }

  fun release() {
    scope.cancel()
    try {
      tts?.stop()
      tts?.shutdown()
    } catch (_: Throwable) {
    }
  }
}
