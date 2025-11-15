package com.example.eduoptimaolapii.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun AdvancedChart(
    data: Map<String, Float>,
    title: String,
    chartType: ChartType = ChartType.BAR,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp)
        ) {
            when (chartType) {
                ChartType.BAR -> drawAdvancedBarChart(data)
                ChartType.LINE -> drawAdvancedLineChart(data)
                ChartType.AREA -> drawAdvancedAreaChart(data)
            }
        }
    }
}

enum class ChartType { BAR, LINE, AREA }

private fun DrawScope.drawAdvancedBarChart(data: Map<String, Float>) {
    if (data.isEmpty()) return

    val maxValue = data.values.maxOrNull() ?: 1f
    val barWidth = size.width / (data.size * 2 - 1)
    val spacing = barWidth * 0.6f
    val chartHeight = size.height - 60

    data.entries.forEachIndexed { index, (_, value) ->
        val barHeight = (value / maxValue) * chartHeight
        val left = index * (barWidth + spacing) + spacing / 2
        val top = size.height - barHeight - 40

        // Barra con gradiente
        drawRect(
            color = MaterialTheme.colorScheme.primary,
            topLeft = Offset(left, top),
            size = Size(barWidth, barHeight),
            alpha = 0.8f
        )

        // Borde
        drawRect(
            color = MaterialTheme.colorScheme.primary,
            topLeft = Offset(left, top),
            size = Size(barWidth, barHeight),
            alpha = 0.3f,
            style = androidx.compose.ui.graphics.drawscope.Stroke(2.dp.toPx())
        )
    }
}

private fun DrawScope.drawAdvancedLineChart(data: Map<String, Float>) {
    val values = data.values.toList()
    if (values.size < 2) return

    val maxValue = values.maxOrNull() ?: 1f
    val minValue = values.minOrNull() ?: 0f
    val valueRange = maxValue - minValue

    val chartHeight = size.height - 40
    val pointWidth = size.width / (values.size - 1)

    val points = values.mapIndexed { index, value ->
        val x = index * pointWidth
        val y = chartHeight - ((value - minValue) / valueRange) * chartHeight + 20
        Offset(x, y.toFloat())
    }

    // Línea suavizada
    if (points.size > 1) {
        for (i in 0 until points.size - 1) {
            drawLine(
                color = MaterialTheme.colorScheme.primary,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4.dp.toPx()
            )
        }
    }
}

private fun DrawScope.drawAdvancedAreaChart(data: Map<String, Float>) {
    val values = data.values.toList()
    if (values.size < 2) return

    val maxValue = values.maxOrNull() ?: 1f
    val minValue = values.minOrNull() ?: 0f
    val valueRange = maxValue - minValue

    val chartHeight = size.height - 40
    val pointWidth = size.width / (values.size - 1)

    val points = values.mapIndexed { index, value ->
        val x = index * pointWidth
        val y = chartHeight - ((value - minValue) / valueRange) * chartHeight + 20
        Offset(x, y.toFloat())
    }

    // Área bajo la curva
    if (points.isNotEmpty()) {
        val path = android.graphics.Path().apply {
            moveTo(points.first().x, size.height - 20)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height - 20)
            close()
        }
        drawContext.canvas.nativeCanvas.drawPath(path, android.graphics.Paint().apply {
            color = android.graphics.Color.argb(80, 33, 150, 243)
            style = android.graphics.Paint.Style.FILL
        })
    }

    // Línea
    if (points.size > 1) {
        for (i in 0 until points.size - 1) {
            drawLine(
                color = MaterialTheme.colorScheme.primary,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3.dp.toPx()
            )
        }
    }
}