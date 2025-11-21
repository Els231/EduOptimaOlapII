package com.example.eduoptimaolapii.ui.components.charts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PieChartComponent(
    data: Map<String, Float>,
    title: String,
    modifier: Modifier = Modifier
) {
    val chartData = if (data.isEmpty()) {
        mapOf("Sin datos" to 1f)
    } else {
        data
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                ProfessionalPieChart(
                    data = chartData,
                    modifier = Modifier.size(160.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val total = chartData.values.sum()
                val colors = getPieChartColors()

                chartData.entries.forEachIndexed { index, (label, value) ->
                    val percentage = if (total > 0) (value / total * 100).toInt() else 0
                    LegendItem(
                        color = colors[index % colors.size],
                        label = label,
                        value = value.toInt(),
                        percentage = percentage
                    )
                }
            }
        }
    }
}

@Composable
fun ProfessionalPieChart(
    data: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    val colors = getPieChartColors()
    val backgroundColor = MaterialTheme.colorScheme.background

    Canvas(modifier = modifier) {
        val total = data.values.sum()
        if (total == 0f) return@Canvas

        var startAngle = -90f

        // Draw shadow first
        data.entries.forEachIndexed { index, (_, value) ->
            val sweepAngle = (value / total) * 360f
            drawPieSegment(
                color = colors[index % colors.size].copy(alpha = 0.3f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                size = size,
                isShadow = true
            )
            startAngle += sweepAngle
        }

        // Reset for main segments
        startAngle = -90f

        // Draw main segments
        data.entries.forEachIndexed { index, (_, value) ->
            val sweepAngle = (value / total) * 360f
            drawPieSegment(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                size = size,
                isShadow = false
            )
            startAngle += sweepAngle
        }

        // Draw center circle
        drawCircle(
            color = backgroundColor,
            center = Offset(size.width / 2, size.height / 2),
            radius = size.minDimension / 4,
            style = Fill
        )
    }
}

private fun DrawScope.drawPieSegment(
    color: Color,
    startAngle: Float,
    sweepAngle: Float,
    size: Size,
    isShadow: Boolean = false
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = size.minDimension / 2 - if (isShadow) 8.dp.toPx() else 0f

    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweepAngle,
        useCenter = true,
        topLeft = Offset(center.x - radius, center.y - radius),
        size = Size(radius * 2, radius * 2),
        style = if (isShadow) Fill else Stroke(width = 4.dp.toPx())
    )

    // Add 3D effect
    if (!isShadow && sweepAngle > 5f) {
        val midAngle = startAngle + sweepAngle / 2
        val highlightRadius = radius - 20.dp.toPx()
        val radians = (midAngle * PI / 180).toFloat()
        val highlightX = center.x + highlightRadius * cos(radians)
        val highlightY = center.y + highlightRadius * sin(radians)

        drawCircle(
            color = Color.White.copy(alpha = 0.3f),
            center = Offset(highlightX, highlightY),
            radius = 4.dp.toPx()
        )
    }
}

@Composable
private fun LegendItem(
    color: Color,
    label: String,
    value: Int,
    percentage: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .padding(end = 8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(color = color)
            }
        }
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        append("$value")
                    }
                    append(" ($percentage%)")
                },
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun getPieChartColors(): List<Color> {
    return listOf(
        Color(0xFF4285F4), // Blue
        Color(0xFF34A853), // Green
        Color(0xFFFBBC05), // Yellow
        Color(0xFFEA4335), // Red
        Color(0xFF8B5CF6), // Purple
        Color(0xFF06B6D4), // Cyan
        Color(0xFFF97316), // Orange
        Color(0xFF84CC16), // Lime
    )
}