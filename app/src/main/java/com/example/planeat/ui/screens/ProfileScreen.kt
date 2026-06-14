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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen() {
    // 允许用户手动修改的个人资料状态
    var nickname by remember { mutableStateOf("自律的干饭人") }
    var dietGoal by remember { mutableStateOf("少食多餐 | 规律作息") }

    var dndStatus by remember { mutableStateOf(true) }
    var selectedInterval by remember { mutableStateOf(10) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 【基本信息输入编辑卡片】
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(50.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👑", style = MaterialTheme.typography.titleLarge)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("个人偏好设置中心", style = MaterialTheme.typography.titleLarge)
                }

                // 变成用户输入框
                OutlinedTextField(
                    value = nickname,
                    onValueChange = { nickname = it },
                    label = { Text("修改用户昵称") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = dietGoal,
                    onValueChange = { dietGoal = it },
                    label = { Text("当前饮食管理目标") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // 【提醒高级设置】
        Text("提醒高级设置", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("勿扰模式", style = MaterialTheme.typography.bodyLarge)
                        Text("22:00 - 07:00 自动静音所有提醒", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(checked = dndStatus, onCheckedChange = { dndStatus = it })
                }

                HorizontalDivider()

                Column {
                    Text("智能推迟间隔", style = MaterialTheme.typography.bodyLarge)
                    Spacer(modifier = Modifier.height(8.dp))
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

        // 【备份与导出数据模拟】
        Text("数据管理", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { /* 模拟导出 */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("📊 导出「$nickname」的历史数据为 Excel")
                }
            }
        }
    }
}