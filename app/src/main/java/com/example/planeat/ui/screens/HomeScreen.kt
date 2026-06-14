package com.example.planeat.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class DynamicTimelineItem(
    val id: Long,
    val time: String,
    val name: String,
    var status: String,
    val detail: String,
    var statusCode: Int // 1:已完成, 2:下一个, 3:未到
)

// 💡 修复核心：把数据源抽离到 Composable 外部，作为一个全局的单例
// 这样即使 HomeScreen 被销毁重构，只要 App 进程没杀掉，删除状态就会一直保持！
private val globalTimelineItems = mutableStateListOf(
    DynamicTimelineItem(1, "08:00", "早餐", "[  ✔  ] 已记录", "燕麦片、鸡蛋", 1),
    DynamicTimelineItem(2, "12:30", "午餐", "[  ⏳  ] 下一个提醒", "等待打卡...", 2),
    DynamicTimelineItem(3, "15:30", "下午茶", "[  ⚪  ] 未到时间", "", 3),
    DynamicTimelineItem(4, "18:30", "晚餐", "[  ⚪  ] 未到时间", "", 3)
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    // 这里直接引用外部的全局状态，不再每次进入页面都重新初始化
    val timelineItems = globalTimelineItems

    // 控制「编辑/添加」弹窗的状态
    var showEditDialog by remember { mutableStateOf(false) }
    var isAddingNew by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<DynamicTimelineItem?>(null) }

    // 控制「长按二次确认删除」弹窗的状态
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var itemPendingDelete by remember { mutableStateOf<DynamicTimelineItem?>(null) }

    // 表单输入绑定
    var inputTime by remember { mutableStateOf("") }
    var inputName by remember { mutableStateOf("") }
    var inputDetail by remember { mutableStateOf("") }

    // 时间选择器控制
    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 30,
        is24Hour = true
    )

    val completedCount = timelineItems.count { it.statusCode == 1 }
    val totalCount = timelineItems.size

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                isAddingNew = true
                editingItem = null
                inputTime = "12:30"
                inputName = ""
                inputDetail = ""
                showEditDialog = true
            }) {
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
                        Text("$completedCount / $totalCount 次", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary)
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("💧 饮水: 5 / 8 杯", style = MaterialTheme.typography.bodyLarge)
                        Text("🍎 水果打卡: 已满足", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }

            Text("今日饮食时间轴 (点击可编辑 / 长按弹出删除)", style = MaterialTheme.typography.titleMedium)

            // 【中部 - 动态时间轴列表】
            timelineItems.forEach { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                isAddingNew = false
                                editingItem = item
                                inputTime = item.time
                                inputName = item.name
                                inputDetail = item.detail
                                showEditDialog = true
                            },
                            onLongClick = {
                                itemPendingDelete = item
                                showDeleteConfirmDialog = true
                            }
                        ),
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
                            Text("详情: ${item.detail}", style = MaterialTheme.typography.bodyLarge)
                        }

                        if (item.statusCode == 2) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = {
                                val index = timelineItems.indexOf(item)
                                if (index != -1) {
                                    timelineItems[index] = item.copy(
                                        status = "[  ✔  ] 已记录",
                                        statusCode = 1,
                                        detail = if(item.detail == "等待打卡...") "已按时吃午餐" else item.detail
                                    )
                                }
                            }) {
                                Text("确认打卡")
                            }
                        }
                    }
                }
            }
        }

        // 【二次确认删除弹窗】
        if (showDeleteConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmDialog = false },
                title = { Text("删除提示") },
                text = { Text("确定要删除「${itemPendingDelete?.time} ${itemPendingDelete?.name}」这条饮食记录吗？") },
                confirmButton = {
                    Button(
                        onClick = {
                            itemPendingDelete?.let { timelineItems.remove(it) }
                            showDeleteConfirmDialog = false
                            itemPendingDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("确认删除")
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDeleteConfirmDialog = false
                        itemPendingDelete = null
                    }) {
                        Text("取消")
                    }
                }
            )
        }

        // 【动态修改/添加通用弹窗】
        if (showEditDialog) {
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text(if (isAddingNew) "新建饮食计划" else "修改时间轴记录") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = inputTime,
                            onValueChange = {},
                            label = { Text("提醒时间 (点击选择)") },
                            readOnly = true,
                            enabled = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor = MaterialTheme.colorScheme.outline,
                                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showTimePicker = true }
                        )

                        OutlinedTextField(
                            value = inputName,
                            onValueChange = { inputName = it },
                            label = { Text("餐食名称") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = inputDetail,
                            onValueChange = { inputDetail = it },
                            label = { Text("详情备注") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        if (isAddingNew) {
                            timelineItems.add(
                                DynamicTimelineItem(
                                    id = System.currentTimeMillis(),
                                    time = inputTime,
                                    name = inputName,
                                    status = "[  ⏳  ] 新增提醒",
                                    detail = inputDetail,
                                    statusCode = 3
                                )
                            )
                        } else {
                            editingItem?.let { oldItem ->
                                val index = timelineItems.indexOfFirst { it.id == oldItem.id }
                                if (index != -1) {
                                    timelineItems[index] = oldItem.copy(
                                        time = inputTime,
                                        name = inputName,
                                        detail = inputDetail
                                    )
                                }
                            }
                        }
                        showEditDialog = false
                    }) {
                        Text("确定")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) {
                        Text("取消")
                    }
                }
            )
        }

        // 【时间选择】
        if (showTimePicker) {
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        val hour = String.format("%02d", timePickerState.hour)
                        val minute = String.format("%02d", timePickerState.minute)
                        inputTime = "$hour:$minute"
                        showTimePicker = false
                    }) { Text("完成") }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) { Text("取消") }
                },
                title = { Text("选择就餐时间", style = MaterialTheme.typography.titleMedium) },
                text = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        TimePicker(state = timePickerState)
                    }
                }
            )
        }
    }
}