package com.example.planeat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 【基本信息】
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(60.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("👑", style = MaterialTheme.typography.headlineMedium)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("自律的干饭人", style = MaterialTheme.typography.titleLarge)
                    Text("饮食目标：少食多餐 | 规律作息", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }

        // 【提醒高级设置】
        Text("提醒高级设置", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text("勿扰模式", style = MaterialTheme.typography.bodyLarge)
                        Text("22:00 - 07:00 自动静音所有提醒", style = MaterialTheme.typography.bodySmall)
                    }
                    var dndStatus by remember { mutableStateOf(true) }
                    Switch(checked = dndStatus, onCheckedChange = { dndStatus = it })
                }

                Divider()

                Column {
                    Text("智能推迟间隔", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
                    var selectedInterval by remember { mutableStateOf(10) }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf(5, 10, 15).forEach { min ->
                            FilterChip(
                                selected = selectedInterval == min,
                                onClick = { selectedInterval = min },
                                label = { Text("$min 分钟") }
                            )
                        }
                    }
                }
            }
        }

        // 【备份与导出】
        Text("数据管理", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* 导出 Excel */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("📊 导出历史数据为 Excel")
                }
                OutlinedButton(onClick = { /* 导出 PDF */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("📄 导出报告为 PDF")
                }
            }
        }
    }
}