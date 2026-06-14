package com.example.planeat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ReminderClock(val time: String, val title: String, val repeat: String, val ring: String, val preheat: Boolean, val initialChecked: Boolean)

@Composable
fun RemindersScreen() {
    val clocks = listOf(
        ReminderClock("08:00", "早餐提醒", "每天", "清晨鸟鸣", true, true),
        ReminderClock("12:15", "午餐提醒", "法定工作日", "默认铃声", false, true),
        ReminderClock("15:30", "下午茶提醒", "每天", "轻柔提示", false, false),
        ReminderClock("18:30", "晚餐提醒", "每天", "默认铃声", true, true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("卡片式闹钟列表", style = MaterialTheme.typography.titleLarge)

        // 【卡片式闹钟列表】
        clocks.forEach { clock ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(clock.time, style = MaterialTheme.typography.headlineLarge)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(clock.title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("重复: ${clock.repeat}  |  铃声: ${clock.ring}", style = MaterialTheme.typography.bodyMedium)
                        if (clock.preheat) {
                            Text("💡 已开启提前15分钟预热提醒", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        }
                    }

                    // Android标准开关切换
                    var isChecked by remember { mutableStateOf(clock.initialChecked) }
                    Switch(
                        checked = isChecked,
                        onCheckedChange = { isChecked = it }
                    )
                }
            }
        }

        // 底部的添加自定义提醒按钮
        OutlinedButton(
            onClick = { /* 添加自定义 */ },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("+ 添加自定义提醒")
        }
    }
}