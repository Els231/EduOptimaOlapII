package com.example.eduoptimaolapii.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ProfessionalBarChart(
    data: Map<String, Float>,
    title: String,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    showGrid: Boolean = true
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        ) {
            if (data.isNotEmpty()) {
                if (showGrid) drawGrid()
                drawProfessionalBars(data, barColor)
                drawAxisLabels(data)
            } else {
                drawEmptyState()
            }
        }
    }
}

private fun DrawScope.drawProfessionalBars(data: Map<String, Float>, barColor: Color) {
    val maxValue = data.values.maxOrNull() ?: 1f
    val barWidth = size.width / (data.size * 2 - 1).coerceAtLeast(1)
    val spacing = barWidth * 0.6f
    val chartHeight = size.height - 60f

    data.entries.forEachIndexed { index, (_, value) ->
        val barHeight = (value / maxValue) * chartHeight
        val left = index * (barWidth + spacing) + spacing / 2
        val top = size.height - barHeight - 40f

        // Barra principal
        drawRect(
            color = barColor.copy(alpha = 0.8f),
            topLeft = Offset(left, top),
            size = Size(barWidth, barHeight)
        )

        // Efecto de brillo
        drawRect(
            color = Color.White.copy(alpha = 0.3f),
            topLeft = Offset(left, top),
            size = Size(barWidth * 0.3f, barHeight)
        )

        // Valor encima de la barra
        if (barHeight > 20f) {
            drawContext.canvas.nativeCanvas.drawText(
                value.toInt().toString(),
                left + barWidth / 2 - 10,
                top - 8,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.parseColor("#2E7D32")
                    textSize = 24f
                    isFakeBoldText = true
                    isAntiAlias = true
                    textAlign = android.graphics.Paint.Align.CENTER
                }
            )
        }
    }
}

private fun DrawScope.drawGrid() {
    val gridColor = Color.LightGray.copy(alpha = 0.3f)

    // Líneas horizontales
    for (i in 0..4) {
        val y = size.height - 40f - (i * (size.height - 60f) / 4)
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun DrawScope.drawAxisLabels(data: Map<String, Float>) {
    val barWidth = size.width / (data.size * 2 - 1).coerceAtLeast(1)
    val spacing = barWidth * 0.6f
    val labels = data.keys.toList()

    // Etiquetas del eje X
    labels.forEachIndexed { index, label ->
        val x = index * (barWidth + spacing) + spacing / 2 + barWidth / 2

        drawContext.canvas.nativeCanvas.drawText(
            label,
            x,
            size.height - 15,
            android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#666666")
                textSize = 20f
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.CENTER
            }
        )
    }

    // Etiquetas del eje Y
    val maxValue = data.values.maxOrNull() ?: 1f
    for (i in 0..4) {
        val value = (maxValue * i / 4).toInt()
        val y = size.height - 40f - (i * (size.height - 60f) / 4)

        drawContext.canvas.nativeCanvas.drawText(
            value.toString(),
            10f,
            y + 5,
            android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#666666")
                textSize = 18f
                isAntiAlias = true
            }
        )
    }
}

private fun DrawScope.drawEmptyState() {
    drawContext.canvas.nativeCanvas.drawText(
        "No hay datos disponibles",
        size.width / 2 - 80,
        size.height / 2,
        android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#999999")
            textSize = 24f
            isAntiAlias = true
            textAlign = android.graphics.Paint.Align.CENTER
        }
    )
}