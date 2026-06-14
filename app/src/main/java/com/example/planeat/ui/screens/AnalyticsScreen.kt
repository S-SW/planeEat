package com.example.planeat.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AnalyticsScreen() {
    // 动态控制昨日历史备注
    var notesText by remember { mutableStateOf("昨日完美执行少食多餐，身体无负担。") }
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("本周就餐达成率", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val weekDays = listOf("一" to true, "二" to true, "三" to false, "四" to true, "五" to true, "六" to false, "日" to false)
                weekDays.forEach { (day, isStar) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(day, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (isStar) "⭐" else "⚫", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        Text("规律度折线图 (近两周时间偏差)", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).padding(16.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📈 [偏差值数据动态拟合中]", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // 【下方 - 可输入历史瀑布流】
        Text("历史饮食日记瀑布流", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📅 2026年6月13日 (昨日)", style = MaterialTheme.typography.titleMedium)
                    Button(onClick = { isExpanded = !isExpanded }) {
                        Text(if (isExpanded) "隐藏详情" else "展开历史")
                    }
                }

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(120.dp).background(MaterialTheme.colorScheme.inverseOnSurface, RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🖼️ [此处模拟展示昨日餐食瀑布流照片]")
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    // 允许用户直接在文本框里输入和修改历史文字
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("昨日饮食总结与备注点评") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}