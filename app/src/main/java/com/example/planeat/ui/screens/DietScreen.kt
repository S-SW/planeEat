package com.example.planeat.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 🛠️ 纯前端假数据：直接写在文件里，彻底断绝和后端/外部data文件夹的联系，保证不报错
data class LocalDietRecord(val mealType: String, val foodName: String, val weightG: Int, val calories: Int)

val localMockDietRecords = listOf(
    LocalDietRecord("早餐", "全麦面包", 100, 250),
    LocalDietRecord("早餐", "纯牛奶", 250, 160),
    LocalDietRecord("午餐", "香煎鸡胸肉", 150, 248),
    LocalDietRecord("午餐", "水煮西兰花", 100, 34),
    LocalDietRecord("午餐", "糙米饭", 150, 165)
)

@Composable
fun DietScreen() {
    val totalCalories = localMockDietRecords.sumOf { it.calories }
    val targetTdee = 2200

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* 纯前端占位 */ }) {
                // 修复点：这里直接引用基础图标，不再需要额外扩展包
                Icon(Icons.Default.Add, contentDescription = "添加饮食")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // 顶部热量卡片
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "今日热量摄入", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "$totalCalories / $targetTdee kcal", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { totalCalories.toFloat() / targetTdee },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "饮食记录明细", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            // 饮食列表
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(localMockDietRecords) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = "[${record.mealType}] ${record.foodName}", style = MaterialTheme.typography.bodyLarge)
                                Text(text = "${record.weightG} g", style = MaterialTheme.typography.bodyMedium)
                            }
                            Text(text = "${record.calories} kcal", style = MaterialTheme.typography.titleMedium)
                        }
                    }
                }
            }
        }
    }
}