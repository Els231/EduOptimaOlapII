package com.example.eduoptimaolapii.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Colores para Light Theme (SIMPLIFICADO)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF006A65),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF73F8EE),
    onPrimaryContainer = Color(0xFF00201E),
    secondary = Color(0xFF4A6360),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCE8E3),
    onSecondaryContainer = Color(0xFF05201D),
    tertiary = Color(0xFF456179),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFCCE5FF),
    onTertiaryContainer = Color(0xFF001D31),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    background = Color(0xFFFAFDFB),
    onBackground = Color(0xFF191C1C),
    surface = Color(0xFFFAFDFB),
    onSurface = Color(0xFF191C1C),
    surfaceVariant = Color(0xFFDAE5E2),
    onSurfaceVariant = Color(0xFF3F4947),
    outline = Color(0xFF6F7977),
    outlineVariant = Color(0xFFBEC9C6)
)

// Colores para Dark Theme (SIMPLIFICADO)
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF52DBD0),
    onPrimary = Color(0xFF003734),
    primaryContainer = Color(0xFF00504B),
    onPrimaryContainer = Color(0xFF73F8EE),
    secondary = Color(0xFFB0CCC7),
    onSecondary = Color(0xFF1B3532),
    secondaryContainer = Color(0xFF324B48),
    onSecondaryContainer = Color(0xFFCCE8E3),
    tertiary = Color(0xFFAEC9E6),
    onTertiary = Color(0xFF153349),
    tertiaryContainer = Color(0xFF2D4961),
    onTertiaryContainer = Color(0xFFCCE5FF),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF191C1C),
    onBackground = Color(0xFFE0E3E1),
    surface = Color(0xFF191C1C),
    onSurface = Color(0xFFE0E3E1),
    surfaceVariant = Color(0xFF3F4947),
    onSurfaceVariant = Color(0xFFBEC9C6),
    outline = Color(0xFF899390),
    outlineVariant = Color(0xFF3F4947)
)

// Colores personalizados para la app educativa
val EduPrimary = Color(0xFF006A65)
val EduSecondary = Color(0xFF4A6360)
val EduAccent = Color(0xFF456179)
val EduSuccess = Color(0xFF2E7D32)
val EduWarning = Color(0xFFF57C00)
val EduError = Color(0xFFD32F2F)
val EduInfo = Color(0xFF0288D1)

// Gradientes personalizados
val EduGradientPrimary = listOf(Color(0xFF006A65), Color(0xFF004A47))
val EduGradientSecondary = listOf(Color(0xFF4A6360), Color(0xFF2D413F))
val EduGradientAccent = listOf(Color(0xFF456179), Color(0xFF2D4159))

@Composable
fun EduOptimaOLAPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EduOptimaTypography,
        content = content
    )
}