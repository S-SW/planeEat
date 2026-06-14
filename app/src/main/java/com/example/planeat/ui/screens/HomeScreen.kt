package com.example.planeat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

// 纯前端时间轴假数据
data class TimelineItem(val time: String, val name: String, val status: String, val detail: String, val statusCode: Int) // 1:已完成, 2:下一个, 3:未到

@Composable
fun HomeScreen() {
    val timelineList = listOf(
        TimelineItem("08:00", "早餐", "[  ✔  ] 已于 08:05 记录", "燕麦片、鸡蛋", 1),
        TimelineItem("12:30", "午餐", "[  ⏳  ] 下一个提醒", "等待打卡...", 2),
        TimelineItem("15:30", "下午茶", "[  ⚪  ] 未到时间", "", 3),
        TimelineItem("18:30", "晚餐", "[  ⚪  ] 未到时间", "", 3)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 快捷加餐 */ }) {
                Text("+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 【顶部 - 状态仪表盘】
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("今日已按时进食", style = MaterialTheme.typography.titleMedium)
                        Text("2 / 4 次", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    // 水滴和苹果打卡进度
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("💧 饮水: 5 / 8 杯", style = MaterialTheme.typography.bodyLarge)
                        Text("🍎 水果打卡: 已满足", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Text("今日饮食时间轴", style = MaterialTheme.typography.titleLarge)

            // 【中部 - 动态时间轴列表】
            timelineList.forEach { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = when(item.statusCode) {
                            1 -> MaterialTheme.colorScheme.surfaceVariant
                            2 -> MaterialTheme.colorScheme.primaryContainer
                            else -> MaterialTheme.colorScheme.surface
                        }
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(item.time, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(end = 8.dp))
                                Text(item.name, style = MaterialTheme.typography.titleMedium)
                            }
                            Text(item.status, style = MaterialTheme.typography.bodyMedium)
                        }

                        if (item.detail.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("食物: ${item.detail}", style = MaterialTheme.typography.bodyLarge)
                        }

                        // 如果是下一个提醒，展示右侧操作按钮
                        if (item.statusCode == 2) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { /* 提前打卡 */ }) {
                                    Text("提前打卡")
                                }
                                OutlinedButton(onClick = { /* 修改时间 */ }) {
                                    Text("修改时间")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}