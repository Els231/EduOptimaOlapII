// File: app/src/main/java/com/example/eduoptimaolapii/ui/components/charts/ProfessionalLineChart.kt
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.nativeCanvas


@Composable
fun ProfessionalLineChart(
    data: Map<String, Float>,
    title: String,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary,
    fillColor: Color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 16.dp)
        ) {
            if (data.isNotEmpty()) {
                drawLineChart(data, lineColor, fillColor)
            }
        }
    }
}

private fun DrawScope.drawLineChart(data: Map<String, Float>, lineColor: Color, fillColor: Color) {
    val values = data.values.toList()
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

    // Dibujar área bajo la línea
    if (points.size > 1) {
        val path = android.graphics.Path().apply {
            moveTo(points.first().x, size.height - 20)
            points.forEach { lineTo(it.x, it.y) }
            lineTo(points.last().x, size.height - 20)
            close()
        }
        drawContext.canvas.nativeCanvas.drawPath(path, android.graphics.Paint().apply {
            color = android.graphics.Color.argb(51, 33, 150, 243)
            style = android.graphics.Paint.Style.FILL
        })
    }

    // Dibujar línea
    if (points.size > 1) {
        for (i in 0 until points.size - 1) {
            drawLine(
                color = lineColor,
                start = points[i],
                end = points[i + 1],
                strokeWidth = 4.dp.toPx()
            )
        }
    }

    // Dibujar puntos
    points.forEach { point ->
        drawCircle(
            color = lineColor,
            center = point,
            radius = 6.dp.toPx()
        )
        drawCircle(
            color = Color.White,
            center = point,
            radius = 3.dp.toPx()
        )
    }

    // Dibujar etiquetas
    data.keys.forEachIndexed { index, label ->
        val x = index * pointWidth
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                label,
                x - 10,
                size.height - 5,
                android.graphics.Paint().apply {
                    color = android.graphics.Color.parseColor("#666666")
                    textSize = 24f
                }
            )
        }
    }
}