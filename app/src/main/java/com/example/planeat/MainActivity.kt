package com.example.planeat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.planeat.ui.screens.MainScreen
import com.example.planeat.ui.theme.PlanEatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlanEatTheme {
                MainScreen()
            }
        }
    }
}