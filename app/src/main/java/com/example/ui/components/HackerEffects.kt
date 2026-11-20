package com.example.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.AppLanguage
import com.example.model.AppStrings
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.CyberObsidian
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.NeonGreenDim
import com.example.ui.theme.TerminalBorder
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun MatrixRainBackground(
  modifier: Modifier = Modifier,
  alpha: Float = 0.28f
) {
  var tick by remember { mutableIntStateOf(0) }
  val glyphs = remember { "01アイウエオカキクケコサシスセソABCDEF9876543210#@$%" }
  val columns = remember { 28 }
  val drops = remember { IntArray(columns) { Random.nextInt(0, 30) } }

  LaunchedEffect(Unit) {
    while (true) {
      delay(85)
      for (i in drops.indices) {
        drops[i] = if (drops[i] > 32 && Random.nextFloat() > 0.92f) 0 else drops[i] + 1
      }
      tick++
    }
  }

  val paint = remember {
    Paint().apply {
      typeface = Typeface.MONOSPACE
      textSize = 32f
      isAntiAlias = true
    }
  }

  Canvas(
    modifier = modifier
      .fillMaxSize()
      .alpha(alpha)
  ) {
    val frameSeed = tick
    val colWidth = size.width / columns
    val stepY = 36f

    for (col in 0 until columns) {
      val headRow = drops[col]
      val x = col * colWidth + colWidth * 0.2f
      for (trail in 0..8) {
        val row = headRow - trail
        if (row < 0) continue
        val y = row * stepY
        if (y > size.height) continue

        val ch = glyphs[(col * 7 + row + frameSeed) % glyphs.length].toString()
        paint.color = when (trail) {
          0 -> android.graphics.Color.rgb(200, 255, 200)
          1, 2 -> android.graphics.Color.argb(210, 0, 255, 0)
          else -> android.graphics.Color.argb((170 - trail * 18).coerceAtLeast(25), 0, 220, 0)
        }
        drawContext.canvas.nativeCanvas.drawText(ch, x, y, paint)
      }
    }
  }
}

@Composable
fun ScanlineOverlay(modifier: Modifier = Modifier) {
  Canvas(modifier = modifier.fillMaxSize()) {
    val lineGap = 6.dp.toPx()
    var y = 0f
    while (y < size.height) {
      drawLine(
        color = Color.Black.copy(alpha = 0.22f),
        start = Offset(0f, y),
        end = Offset(size.width, y),
        strokeWidth = 1.5f
      )
      y += lineGap
    }
  }
}

@Composable
fun RedAlarmFlashOverlay(
  isActive: Boolean,
  modifier: Modifier = Modifier
) {
  if (!isActive) return
  val infinite = rememberInfiniteTransition(label = "alarm_flash")
  val flashAlpha by infinite.animateFloat(
    initialValue = 0.04f,
    targetValue = 0.28f,
    animationSpec = infiniteRepeatable(
      animation = tween(360, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "flash_alpha"
  )
  Box(
    modifier = modifier
      .fillMaxSize()
      .background(AlertRed.copy(alpha = flashAlpha))
      .border(4.dp, AlertRed.copy(alpha = (flashAlpha * 2.5f).coerceAtMost(0.9f)))
  )
}

@Composable
fun GlitchText(
  text: String,
  style: TextStyle,
  color: Color = NeonGreen,
  glitchTrigger: Int = 0,
  modifier: Modifier = Modifier,
  textAlign: TextAlign? = null
) {
  var jitterX by remember { mutableStateOf(0) }
  var showChromatic by remember { mutableStateOf(false) }

  LaunchedEffect(glitchTrigger) {
    if (glitchTrigger > 0) {
      showChromatic = true
      jitterX = Random.nextInt(-5, 6)
      delay(65)
      jitterX = Random.nextInt(-3, 4)
      delay(55)
      jitterX = 0
      showChromatic = false
    }
  }

  Box(modifier = modifier.semantics(mergeDescendants = true) {}) {
    if (showChromatic) {
      Text(
        text = text,
        style = style,
        color = CyberCyan.copy(alpha = 0.65f),
        textAlign = textAlign,
        modifier = Modifier
          .clearAndSetSemantics {}
          .offset { IntOffset(-4 + jitterX, 1) }
      )
      Text(
        text = text,
        style = style,
        color = AlertRed.copy(alpha = 0.65f),
        textAlign = textAlign,
        modifier = Modifier
          .clearAndSetSemantics {}
          .offset { IntOffset(4 - jitterX, -1) }
      )
    }
    Text(
      text = text,
      style = style,
      color = color,
      textAlign = textAlign,
      modifier = Modifier.offset { IntOffset(jitterX, 0) }
    )
  }
}

@Composable
fun NasaRadarHud(
  isAlarmActive: Boolean,
  coordinates: String,
  countdownSec: Int = 15,
  modifier: Modifier = Modifier
) {
  val infinite = rememberInfiniteTransition(label = "radar")
  val sweepAngle by infinite.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "sweep"
  )
  val pulseAlpha by infinite.animateFloat(
    initialValue = 0.2f,
    targetValue = 0.9f,
    animationSpec = infiniteRepeatable(
      animation = tween(480, easing = LinearEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(176.dp)
      .clip(RoundedCornerShape(10.dp))
      .border(
        width = 1.5.dp,
        color = if (isAlarmActive) AlertRed.copy(alpha = pulseAlpha) else NeonGreen,
        shape = RoundedCornerShape(10.dp)
      )
      .background(CyberBlack)
  ) {
    Image(
      painter = painterResource(id = R.drawable.img_world_map_cyber),
      contentDescription = "World satellite map",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .alpha(0.68f)
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
      val center = Offset(size.width * 0.5f, size.height * 0.5f)
      val maxR = size.minDimension * 0.44f

      listOf(0.33f, 0.66f, 1.0f).forEach { factor ->
        drawCircle(
          color = NeonGreen.copy(alpha = 0.45f),
          radius = maxR * factor,
          center = center,
          style = Stroke(width = 1.5f)
        )
      }

      drawLine(
        color = NeonGreen.copy(alpha = 0.35f),
        start = Offset(center.x - maxR, center.y),
        end = Offset(center.x + maxR, center.y),
        strokeWidth = 1f
      )
      drawLine(
        color = NeonGreen.copy(alpha = 0.35f),
        start = Offset(center.x, center.y - maxR),
        end = Offset(center.x, center.y + maxR),
        strokeWidth = 1f
      )

      drawArc(
        brush = Brush.sweepGradient(
          0f to Color.Transparent,
          0.12f to NeonGreen.copy(alpha = 0.38f),
          0.15f to Color.Transparent
        ),
        startAngle = sweepAngle,
        sweepAngle = 52f,
        useCenter = true,
        topLeft = Offset(center.x - maxR, center.y - maxR),
        size = Size(maxR * 2f, maxR * 2f)
      )

      val rad = Math.toRadians((sweepAngle + 52f).toDouble())
      val endPoint = Offset(
        x = center.x + (maxR * cos(rad)).toFloat(),
        y = center.y + (maxR * sin(rad)).toFloat()
      )
      drawLine(
        color = NeonGreenBright,
        start = center,
        end = endPoint,
        strokeWidth = 2.5f,
        cap = StrokeCap.Round
      )

      val satRad = Math.toRadians((sweepAngle * 1.4).toDouble())
      val satPos = Offset(
        x = center.x + (maxR * 0.78f * cos(satRad)).toFloat(),
        y = center.y + (maxR * 0.52f * sin(satRad)).toFloat()
      )
      drawCircle(color = CyberCyan, radius = 6f, center = satPos)
      drawCircle(
        color = if (isAlarmActive) AlertRed else NeonGreen,
        radius = 12f,
        center = satPos,
        style = Stroke(width = 2f)
      )
    }

    Row(
      modifier = Modifier
        .align(Alignment.TopStart)
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      val alarmBadge = if (isAlarmActive) {
        "[!] ALARM // T-${countdownSec.toString().padStart(2, '0')}s"
      } else {
        "[SAT-LINK ACTIVE]"
      }
      Text(
        text = alarmBadge,
        style = MaterialTheme.typography.labelSmall,
        color = if (isAlarmActive) AlertRed else NeonGreenBright,
        modifier = Modifier
          .background(CyberBlack.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      )
      Text(
        text = coordinates,
        style = MaterialTheme.typography.labelSmall,
        color = CyberCyan,
        modifier = Modifier
          .background(CyberBlack.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      )
    }
  }
}

@Composable
fun FriendTrackerMapHud(
  dotProgress: Float,
  friendNickname: String,
  isLocked: Boolean,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val infinite = rememberInfiniteTransition(label = "tracker")
  val pingRadius by infinite.animateFloat(
    initialValue = 8f,
    targetValue = 36f,
    animationSpec = infiniteRepeatable(
      animation = tween(1000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "ping"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(175.dp)
      .clip(RoundedCornerShape(10.dp))
      .border(1.5.dp, NeonGreen, RoundedCornerShape(10.dp))
      .background(CyberBlack)
  ) {
    Image(
      painter = painterResource(id = R.drawable.img_world_map_cyber),
      contentDescription = "Tracking grid map",
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxSize()
        .alpha(0.58f)
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
      val cols = 8
      val rows = 5
      for (c in 1 until cols) {
        val x = size.width * (c / cols.toFloat())
        drawLine(NeonGreenDim.copy(alpha = 0.35f), Offset(x, 0f), Offset(x, size.height), 1f)
      }
      for (r in 1 until rows) {
        val y = size.height * (r / rows.toFloat())
        drawLine(NeonGreenDim.copy(alpha = 0.35f), Offset(0f, y), Offset(size.width, y), 1f)
      }

      val startX = size.width * 0.2f
      val endX = size.width * 0.76f
      val dotX = startX + (endX - startX) * dotProgress
      val waveY = size.height * 0.5f + sin(dotProgress * Math.PI * 2.5).toFloat() * (size.height * 0.18f)
      val dotCenter = Offset(dotX, waveY)

      drawLine(
        color = AlertRed.copy(alpha = 0.65f),
        start = Offset(startX, size.height * 0.5f),
        end = dotCenter,
        strokeWidth = 2.5f
      )

      drawCircle(
        color = AlertRed.copy(alpha = (1f - pingRadius / 40f).coerceIn(0.1f, 0.9f)),
        radius = pingRadius,
        center = dotCenter,
        style = Stroke(width = 2.5f)
      )
      drawCircle(
        color = AlertRed,
        radius = 8f,
        center = dotCenter
      )
    }

    Row(
      modifier = Modifier
        .align(Alignment.BottomStart)
        .fillMaxWidth()
        .padding(8.dp),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "TARGET: ${friendNickname.ifBlank { "Budi_Santuy" }}",
        style = MaterialTheme.typography.labelSmall,
        color = NeonGreenBright,
        modifier = Modifier
          .background(CyberBlack.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      )
      Text(
        text = AppStrings.gpsStatusLabel(language, isLocked),
        style = MaterialTheme.typography.labelSmall,
        color = AlertRed,
        modifier = Modifier
          .background(CyberBlack.copy(alpha = 0.8f), RoundedCornerShape(4.dp))
          .padding(horizontal = 6.dp, vertical = 2.dp)
      )
    }
  }
}

@Composable
fun HackerAutoKeyboard(
  hintText: String,
  keystrokesCount: Int,
  language: AppLanguage,
  onKeyPress: () -> Unit,
  modifier: Modifier = Modifier
) {
  val rows = remember {
    listOf(
      listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
      listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
      listOf("Z", "X", "C", "V", "B", "N", "M"),
      listOf("0xFF", "SUDO", "BYPASS", "EXEC")
    )
  }

  var hiddenInput by remember { mutableStateOf("") }
  val focusRequester = remember { FocusRequester() }
  val keyboardController = LocalSoftwareKeyboardController.current

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(10.dp))
      .background(CyberObsidian)
      .border(1.dp, TerminalBorder, RoundedCornerShape(10.dp))
      .padding(8.dp),
    verticalArrangement = Arrangement.spacedBy(5.dp)
  ) {
    // Hidden 1dp input to allow opening real Android system keyboard on demand
    BasicTextField(
      value = hiddenInput,
      onValueChange = { next ->
        hiddenInput = next.takeLast(1)
        onKeyPress()
      },
      modifier = Modifier
        .size(1.dp)
        .alpha(0f)
        .focusRequester(focusRequester)
    )

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = hintText,
        style = MaterialTheme.typography.labelSmall,
        color = CyberCyan,
        modifier = Modifier.weight(1f)
      )
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Surface(
          color = CyberSurface,
          shape = RoundedCornerShape(4.dp),
          border = BorderStroke(1.dp, CyberCyan),
          modifier = Modifier
            .clickable {
              focusRequester.requestFocus()
              keyboardController?.show()
            }
            .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(Icons.Default.Keyboard, contentDescription = "System Keyboard", tint = CyberCyan, modifier = Modifier.size(14.dp))
            Text("SYS", style = MaterialTheme.typography.labelSmall, color = CyberCyan)
          }
        }
        Text(
          text = AppStrings.keysCounterLabel(language, keystrokesCount),
          style = MaterialTheme.typography.labelSmall,
          color = NeonGreenBright
        )
      }
    }

    rows.forEachIndexed { rowIdx, keys ->
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        keys.forEach { label ->
          Surface(
            color = CyberSurface,
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, NeonGreenDim),
            modifier = Modifier
              .weight(if (label.length > 2) 1.6f else 1f)
              .height(42.dp)
              .testTag("hacker_key_${rowIdx}_$label")
              .clickable { onKeyPress() }
          ) {
            Box(contentAlignment = Alignment.Center) {
              Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                  fontSize = if (label.length > 2) 10.sp else 12.sp,
                  fontWeight = FontWeight.Bold
                ),
                color = NeonGreen,
                maxLines = 1
              )
            }
          }
        }
      }
    }
  }
}
