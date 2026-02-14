package com.example.collegeschedule.ui.schedule

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedule.data.dto.GroupDto
import com.example.collegeschedule.data.dto.ScheduleByDateDto
import com.example.collegeschedule.data.network.RetrofitInstance

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    selectedGroup: String,
    onGroupSelected: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    isFavorite: Boolean
) {

    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }

    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    // Загрузка списка групп
    LaunchedEffect(Unit) {
        try {
            loading = true
            groups = RetrofitInstance.api.getGroups()
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    // Загрузка расписания при смене группы
    LaunchedEffect(selectedGroup) {
        try {
            loading = true
            schedule = RetrofitInstance.api.getSchedule(
                selectedGroup,
                "2026-01-12",
                "2026-01-17"
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(selectedGroup)
                },
                actions = {
                    IconButton(
                        onClick = { onToggleFavorite(selectedGroup) }
                    ) {
                        Icon(
                            imageVector = if (isFavorite)
                                Icons.Default.Favorite
                            else
                                Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {

                OutlinedTextField(
                    value = selectedGroup,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Group") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    groups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(group.groupName) },
                            onClick = {
                                onGroupSelected(group.groupName)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when {
                loading -> CircularProgressIndicator()
                error != null -> Text("Error: $error")
                else -> ScheduleList(schedule)
            }
        }
    }
}
