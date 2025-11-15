package com.example.eduoptimaolapii

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.eduoptimaolapii.navigation.AppNavigation
import com.example.eduoptimaolapii.ui.theme.EduOptimaOLAPTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduOptimaApp()
        }
    }
}

@Composable
fun EduOptimaApp() {
    EduOptimaOLAPTheme {
        Surface(
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavigation()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EduOptimaApp()
}