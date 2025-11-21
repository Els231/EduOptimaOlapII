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
import androidx.compose.ui.graphics.drawscope.Stroke
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
    // Obtener los colores del tema aquí en el ámbito composable
    val primaryColor = MaterialTheme.colorScheme.primary
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = onSurfaceColor
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp)
        ) {
            when (chartType) {
                ChartType.BAR -> drawAdvancedBarChart(data, primaryColor)
                ChartType.LINE -> drawAdvancedLineChart(data, primaryColor)
                ChartType.AREA -> drawAdvancedAreaChart(data, primaryColor)
            }
        }
    }
}

enum class ChartType {
    BAR,
    LINE,
    AREA
}

private fun DrawScope.drawAdvancedBarChart(
    data: Map<String, Float>,
    primaryColor: Color
) {
    if (data.isEmpty()) {
        drawEmptyState()
        return
    }

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
            color = primaryColor,
            topLeft = Offset(left, top),
            size = Size(barWidth, barHeight),
            alpha = 0.8f
        )

        // Borde de la barra
        drawRect(
            color = primaryColor,
            topLeft = Offset(left, top),
            size = Size(barWidth, barHeight),
            alpha = 0.3f,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}

private fun DrawScope.drawAdvancedLineChart(
    data: Map<String, Float>,
    primaryColor: Color
) {
    val values = data.values.toList()
    if (values.size < 2) {
        drawEmptyState()
        return
    }

    val maxValue = values.maxOrNull() ?: 1f
    val minValue = values.minOrNull() ?: 0f
    val valueRange = maxValue - minValue

    val chartHeight = size.height - 40f
    val pointWidth = size.width / (values.size - 1)
    val verticalPadding = 20f

    val points = values.mapIndexed { index, value ->
        val x = index * pointWidth
        val y = chartHeight - ((value - minValue) / valueRange) * (chartHeight - verticalPadding) + 10f
        Offset(x, y)
    }

    // Dibujar línea conectando los puntos
    if (points.size > 1) {
        for (i in 0 until points.size - 1) {
            drawLine(
                color = primaryColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4.dp.toPx()
            )
        }
    }

    // Dibujar puntos en las coordenadas
    points.forEach { point ->
        drawCircle(
            color = primaryColor,
            center = point,
            radius = 6.dp.toPx()
        )

        // Punto interior blanco
        drawCircle(
            color = Color.White,
            center = point,
            radius = 3.dp.toPx()
        )
    }
}

private fun DrawScope.drawAdvancedAreaChart(
    data: Map<String, Float>,
    primaryColor: Color
) {
    val values = data.values.toList()
    if (values.size < 2) {
        drawEmptyState()
        return
    }

    val maxValue = values.maxOrNull() ?: 1f
    val minValue = values.minOrNull() ?: 0f
    val valueRange = maxValue - minValue

    val chartHeight = size.height - 40f
    val pointWidth = size.width / (values.size - 1)
    val verticalPadding = 20f

    val points = values.mapIndexed { index, value ->
        val x = index * pointWidth
        val y = chartHeight - ((value - minValue) / valueRange) * (chartHeight - verticalPadding) + 10f
        Offset(x, y)
    }

    // Dibujar área bajo la curva
    if (points.isNotEmpty()) {
        drawContext.canvas.nativeCanvas.drawPath(
            android.graphics.Path().apply {
                moveTo(points.first().x, size.height - 20f)
                points.forEach { lineTo(it.x, it.y) }
                lineTo(points.last().x, size.height - 20f)
                close()
            },
            android.graphics.Paint().apply {
                color = android.graphics.Color.argb(80,
                    (primaryColor.red * 255).toInt(),
                    (primaryColor.green * 255).toInt(),
                    (primaryColor.blue * 255).toInt()
                )
                style = android.graphics.Paint.Style.FILL
            }
        )
    }

    // Dibujar línea sobre el área
    if (points.size > 1) {
        for (i in 0 until points.size - 1) {
            drawLine(
                color = primaryColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 3.dp.toPx()
            )
        }
    }

    // Dibujar puntos en la línea
    points.forEach { point ->
        drawCircle(
            color = primaryColor,
            center = point,
            radius = 4.dp.toPx()
        )

        drawCircle(
            color = Color.White,
            center = point,
            radius = 2.dp.toPx()
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