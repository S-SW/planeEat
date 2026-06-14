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
            // 这里的 PlanEatTheme 必须对应你项目 ui.theme 下的 Theme 名字
            PlanEatTheme {
                MainScreen()
            }
        }
    }
}