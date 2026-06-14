package com.example.planeat.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

// 四个主要看板的枚举定义
enum class AppScreen(val title: String, val icon: String) {
    Home("今日首屏", "🏠"),
    Reminders("提醒设置", "⏰"),
    Analytics("历史与分析", "📊"),
    Profile("个人中心", "👤")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var currentScreen by remember { mutableStateOf(AppScreen.Home) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentScreen.title) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                AppScreen.values().forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        label = { Text(screen.title) },
                        icon = { Text(screen.icon, style = MaterialTheme.typography.titleLarge) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                AppScreen.Home -> HomeScreen()
                AppScreen.Reminders -> RemindersScreen()
                AppScreen.Analytics -> AnalyticsScreen()
                AppScreen.Profile -> ProfileScreen()
            }
        }
    }
}