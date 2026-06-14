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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 【顶部 - 周历视图】
        Text("本周就餐达成率", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(12.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val weekDays = listOf("一" to true, "二" to true, "三" to false, "四" to true, "五" to true, "六" to false, "日" to false)
                weekDays.forEach { (day, isStar) ->
                    // 🛠️ 修复点：这里的 CenterAlignment 改为了 CenterHorizontally
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(day, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(if (isStar) "⭐" else "⚫", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }

        // 【中部 - 统计图表（纯静态线框抽象版）】
        Text("规律度折线图 (近两周时间偏差)", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier.fillMaxWidth().height(120.dp).padding(16.dp).background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("📈 [折线图模拟区域: 偏差值趋于稳定]", style = MaterialTheme.typography.bodyLarge)
            }
        }

        Text("餐食类型比例", style = MaterialTheme.typography.titleMedium)
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("🟢 健康饮食: 70%", style = MaterialTheme.typography.bodyLarge)
                Text("🟡 应酬聚餐: 20%", style = MaterialTheme.typography.bodyLarge)
                Text("🔴 放纵大餐: 10%", style = MaterialTheme.typography.bodyLarge)
            }
        }

        // 【下方 - 历史瀑布流】
        Text("历史饮食日记瀑布流", style = MaterialTheme.typography.titleMedium)
        var isExpanded by remember { mutableStateOf(false) }
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = { isExpanded = !isExpanded }
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("📅 2026年6月13日 (昨日)", style = MaterialTheme.typography.titleMedium)
                    Text(if (isExpanded) "收起 ▲" else "展开 ▼", color = MaterialTheme.colorScheme.primary)
                }
                if (isExpanded) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(150.dp).background(MaterialTheme.colorScheme.inverseOnSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🖼️ [用户上传的食物照片瀑布流预览]")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("备注：昨日完美执行少食多餐，身体无负担。", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}