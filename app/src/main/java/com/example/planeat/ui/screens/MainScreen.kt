package com.example.planeat.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

enum class Screen(val title: String) {
    Diet("饮食"),
    Workout("训练"),
    Profile("我的")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf(Screen.Diet) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(currentScreen.title) })
        },
        bottomBar = {
            NavigationBar {
                Screen.values().forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        label = { Text(screen.title) },
                        icon = {
                            // 针对纯前端进行硬编码图标，绝对不会产生 unresolved reference 报错
                            val iconImage = when(screen) {
                                Screen.Diet -> Icons.Default.List
                                Screen.Workout -> Icons.Default.PlayArrow
                                Screen.Profile -> Icons.Default.AccountCircle
                            }
                            Icon(iconImage, contentDescription = screen.title)
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Diet -> DietScreen()
                Screen.Workout -> WorkoutScreen()
                Screen.Profile -> ProfileScreen()
            }
        }
    }
}