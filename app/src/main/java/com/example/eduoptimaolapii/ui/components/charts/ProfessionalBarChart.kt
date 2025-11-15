// File: app/src/main/java/com/example/eduoptimaolapii/ui/components/charts/ProfessionalBarChart.kt
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
import androidx.compose.ui.unit.sp
import kotlin.math.max

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
    val barWidth = size.width / (data.size * 2 - 1)
    val spacing = barWidth * 0.6f
    val chartHeight = size.height - 60

    data.entries.forEachIndexed { index, (label, value) ->
        val barHeight = (value / maxValue) * chartHeight
        val left = index * (barWidth + spacing) + spacing / 2
        val top = size.height - barHeight - 40

        // Barra con gradiente
        drawRect(
            color = barColor.copy(alpha = 0.8f),
            topLeft = Offset(left, top),
            size = Size(barWidth, barHeight),
            alpha = 0.9f
        )

        // Efecto de brillo
        drawRect(
            color = Color.White.copy(alpha = 0.3f),
            topLeft = Offset(left, top),
            size = Size(barWidth * 0.3f, barHeight),
            alpha = 0.5f
        )

        // Valor encima de la barra - CORREGIDO
        drawContext.canvas.nativeCanvas.drawText(
            value.toInt().toString(),
            left + barWidth / 2 - 15,
            top - 10,
            android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#2E7D32")
                textSize = 28f
                isFakeBoldText = true
                isAntiAlias = true
            }
        )
    }
}

private fun DrawScope.drawGrid() {
    val gridColor = Color.LightGray.copy(alpha = 0.3f)

    // Líneas horizontales
    for (i in 0..4) {
        val y = size.height - 40 - (i * (size.height - 60) / 4)
        drawLine(
            color = gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = 1.dp.toPx()
        )
    }
}

private fun DrawScope.drawAxisLabels(data: Map<String, Float>) {
    val barWidth = size.width / (data.size * 2 - 1)
    val spacing = barWidth * 0.6f

    data.keys.forEachIndexed { index, label ->
        val x = index * (barWidth + spacing) + spacing / 2 + barWidth / 2

        // Etiquetas del eje X - CORREGIDO
        drawContext.canvas.nativeCanvas.drawText(
            label,
            x - 20,
            size.height - 10,
            android.graphics.Paint().apply {
                color = android.graphics.Color.parseColor("#666666")
                textSize = 24f
                isAntiAlias = true
            }
        )
    }
}

private fun DrawScope.drawEmptyState() {
    // Texto sin datos - CORREGIDO
    drawContext.canvas.nativeCanvas.drawText(
        "No hay datos disponibles",
        size.width / 2 - 100,
        size.height / 2,
        android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#999999")
            textSize = 28f
            isAntiAlias = true
        }
    )
}