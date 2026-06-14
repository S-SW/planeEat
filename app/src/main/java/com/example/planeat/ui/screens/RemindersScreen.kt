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

data class DynamicReminderClock(
    val id: Long,
    val time: String,
    val title: String,
    val repeat: String,
    val ring: String,
    val preheat: Boolean,
    var isEnabled: Boolean
)

// 💡 修复核心：将闹钟列表同样剥离至全局单例，防止来回切换页面时重置
private val globalReminderClocks = mutableStateListOf(
    DynamicReminderClock(1, "08:00", "早餐提醒", "每天", "清晨鸟鸣", true, true),
    DynamicReminderClock(2, "12:15", "午餐提醒", "法定工作日", "默认铃声", false, true)
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RemindersScreen() {
    val reminderClocks = globalReminderClocks

    var showClockDialog by remember { mutableStateOf(false) }
    var isAddingNew by remember { mutableStateOf(false) }
    var editingClock by remember { mutableStateOf<DynamicReminderClock?>(null) }

    // 控制闹钟长按二次确认弹窗
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var clockPendingDelete by remember { mutableStateOf<DynamicReminderClock?>(null) }

    var inputTime by remember { mutableStateOf("") }
    var inputTitle by remember { mutableStateOf("") }

    var showTimePicker by remember { mutableStateOf(false) }
    val timePickerState = rememberTimePickerState(initialHour = 8, initialMinute = 0, is24Hour = true)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("卡片式闹钟设置 (点击编辑 / 长按弹出确认删除)", style = MaterialTheme.typography.titleMedium)
            if (reminderClocks.isNotEmpty()) {
                TextButton(
                    onClick = { reminderClocks.clear() },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("清空全部")
                }
            }
        }

        reminderClocks.forEach { clock ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .combinedClickable(
                        onClick = {
                            isAddingNew = false
                            editingClock = clock
                            inputTime = clock.time
                            inputTitle = clock.title
                            showClockDialog = true
                        },
                        onLongClick = {
                            // 💡 交互优化：长按闹钟也不直接删除，而是呼出二次确认弹窗
                            clockPendingDelete = clock
                            showDeleteConfirmDialog = true
                        }
                    )
            ) {
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
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Switch(
                            checked = clock.isEnabled,
                            onCheckedChange = { status ->
                                val index = reminderClocks.indexOf(clock)
                                if (index != -1) {
                                    reminderClocks[index] = clock.copy(isEnabled = status)
                                }
                            }
                        )
                    }
                }
            }
        }

        OutlinedButton(
            onClick = {
                isAddingNew = true
                editingClock = null
                inputTime = "07:00"
                inputTitle = ""
                showClockDialog = true
            },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        ) {
            Text("+ 添加自定义提醒")
        }
    }

    // 【🛠️ 新增：闹钟长按的二次确认删除弹窗】
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("删除闹钟确认") },
            text = { Text("确定要删除「${clockPendingDelete?.time} ${clockPendingDelete?.title}」这个闹钟提醒吗？") },
            confirmButton = {
                Button(
                    onClick = {
                        clockPendingDelete?.let { reminderClocks.remove(it) }
                        showDeleteConfirmDialog = false
                        clockPendingDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("确认删除")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteConfirmDialog = false
                    clockPendingDelete = null
                }) {
                    Text("取消")
                }
            }
        )
    }

    // 新增/修改闹钟弹窗
    if (showClockDialog) {
        AlertDialog(
            onDismissRequest = { showClockDialog = false },
            title = { Text(if (isAddingNew) "新建饮食闹钟" else "修改闹钟参数") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = inputTime,
                        onValueChange = {},
                        label = { Text("设置响铃时间 (点击选择)") },
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
                        value = inputTitle,
                        onValueChange = { inputTitle = it },
                        label = { Text("闹钟标签名称") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (inputTime.isNotBlank() && inputTitle.isNotBlank()) {
                        if (isAddingNew) {
                            reminderClocks.add(
                                DynamicReminderClock(
                                    id = System.currentTimeMillis(),
                                    time = inputTime,
                                    title = inputTitle,
                                    repeat = "每天",
                                    ring = "默认铃声",
                                    preheat = false,
                                    isEnabled = true
                                )
                            )
                        } else {
                            editingClock?.let { oldClock ->
                                val index = reminderClocks.indexOfFirst { it.id == oldClock.id }
                                if (index != -1) {
                                    reminderClocks[index] = oldClock.copy(
                                        time = inputTime,
                                        title = inputTitle
                                    )
                                }
                            }
                        }
                        showClockDialog = false
                    }
                }) {
                    Text("保存")
                }
            },
            dismissButton = {
                if (!isAddingNew) {
                    TextButton(
                        onClick = {
                            editingClock?.let { reminderClocks.remove(it) }
                            showClockDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("删除此闹钟")
                    }
                } else {
                    TextButton(onClick = { showClockDialog = false }) {
                        Text("取消")
                    }
                }
            }
        )
    }

    // 时间滚轮
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
            title = { Text("滑动选择时间") },
            text = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }
}