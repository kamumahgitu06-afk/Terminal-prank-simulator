package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalculatorDisguiseScreen(
  onExitDisguise: () -> Unit,
  modifier: Modifier = Modifier
) {
  var display by remember { mutableStateOf("0") }
  var pendingOp by remember { mutableStateOf<String?>(null) }
  var storedValue by remember { mutableStateOf<Double?>(null) }
  var resetOnNextDigit by remember { mutableStateOf(false) }

  BackHandler {
    onExitDisguise()
  }

  fun onButtonTap(label: String) {
    when (label) {
      "C" -> {
        display = "0"
        pendingOp = null
        storedValue = null
        resetOnNextDigit = false
      }
      "+", "-", "×", "÷" -> {
        storedValue = display.toDoubleOrNull() ?: 0.0
        pendingOp = label
        resetOnNextDigit = true
      }
      "=" -> {
        val left = storedValue
        val right = display.toDoubleOrNull()
        val op = pendingOp
        if (left != null && right != null && op != null) {
          val result = when (op) {
            "+" -> left + right
            "-" -> left - right
            "×" -> left * right
            "÷" -> if (right == 0.0) Double.NaN else left / right
            else -> right
          }
          display = if (result.isNaN()) {
            "Error"
          } else if (result % 1.0 == 0.0 && result < 1e12) {
            result.toLong().toString()
          } else {
            String.format("%.4f", result).trimEnd('0').trimEnd('.')
          }
          storedValue = null
          pendingOp = null
          resetOnNextDigit = true
        }
      }
      "." -> {
        if (resetOnNextDigit) {
          display = "0."
          resetOnNextDigit = false
        } else if (!display.contains(".")) {
          display += "."
        }
      }
      else -> {
        display = if (display == "0" || display == "Error" || resetOnNextDigit) {
          resetOnNextDigit = false
          label
        } else {
          (display + label).take(12)
        }
      }
    }
  }

  val keyRows = listOf(
    listOf("C", "÷", "×", "-"),
    listOf("7", "8", "9", "+"),
    listOf("4", "5", "6", "."),
    listOf("1", "2", "3", "0"),
    listOf("=")
  )

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(Color(0xFF121214))
      .windowInsetsPadding(WindowInsets.safeDrawing)
      .testTag("calculator_disguise_screen")
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .widthIn(max = 500.dp)
        .align(Alignment.BottomCenter)
        .padding(16.dp),
      verticalArrangement = Arrangement.Bottom
    ) {
      Text(
        text = "Calculator",
        style = MaterialTheme.typography.titleMedium.copy(fontFamily = FontFamily.SansSerif),
        color = Color(0xFF8E8E93),
        modifier = Modifier.padding(bottom = 8.dp)
      )

      Spacer(modifier = Modifier.weight(1f))

      Text(
        text = display,
        fontSize = 54.sp,
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Light,
        color = Color.White,
        textAlign = TextAlign.End,
        maxLines = 1,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 24.dp, horizontal = 8.dp)
          .testTag("calculator_display")
      )

      keyRows.forEach { row ->
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          row.forEach { key ->
            val isOperator = key in listOf("+", "-", "×", "÷", "=")
            val isClear = key == "C"
            val bgColor = when {
              key == "=" -> Color(0xFFFF9F0A)
              isOperator -> Color(0xFF2C2C2E)
              isClear -> Color(0xFF3A3A3C)
              else -> Color(0xFF1C1C1E)
            }
            Surface(
              color = bgColor,
              shape = RoundedCornerShape(18.dp),
              modifier = Modifier
                .weight(1f)
                .height(66.dp)
                .clip(RoundedCornerShape(18.dp))
                .then(
                  if (key == "=") {
                    Modifier
                      .testTag("calculator_equals_button")
                      .combinedClickable(
                        onClick = { onButtonTap("=") },
                        onLongClick = { onExitDisguise() }
                      )
                  } else {
                    Modifier.combinedClickable(
                      onClick = { onButtonTap(key) }
                    )
                  }
                )
            ) {
              Box(contentAlignment = Alignment.Center) {
                Text(
                  text = key,
                  fontSize = 24.sp,
                  fontFamily = FontFamily.SansSerif,
                  fontWeight = FontWeight.Medium,
                  color = Color.White
                )
              }
            }
          }
        }
      }
    }
  }
}
