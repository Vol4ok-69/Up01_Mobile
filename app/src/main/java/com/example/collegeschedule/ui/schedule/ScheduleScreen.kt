package com.example.collegeschedule.ui.schedule

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
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
import retrofit2.HttpException
import java.io.IOException
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import com.example.collegeschedule.utils.getForwardWeekRange

@SuppressLint("MutableCollectionMutableState")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun ScheduleScreen(
    selectedGroup: String,
    onGroupSelected: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    isFavorite: Boolean
) {

    var groups by remember { mutableStateOf<List<GroupDto>>(emptyList()) }
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var cache by remember {
        mutableStateOf(
            mutableMapOf<String, List<ScheduleByDateDto>>()
        )
    }
    var expanded by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    fun loadSchedule() {
        error = null
        loading = true
    }
    // Загрузка списка групп
    LaunchedEffect(Unit) {
        try {
            loading = true
            error = null

            groups = RetrofitInstance.api.getGroups()

        } catch (e: IOException) {
            error = "Нет подключения к серверу"

        } catch (e: HttpException) {
            error = "Ошибка загрузки списка групп"

        } catch (e: Exception) {
            error = "Непредвиденная ошибка"
        } finally {
            loading = false
        }
    }


    // Загрузка расписания при смене группы
    suspend fun fetchSchedule() {

        val (start, end) = getForwardWeekRange()
        val cacheKey = "${selectedGroup}_${start}_${end}"

        Log.d("CACHE_DEBUG", "Key: $cacheKey")

        if (cache.containsKey(cacheKey)) {
            Log.d("CACHE_DEBUG", "Loaded from cache")
            schedule = cache[cacheKey]!!
            return
        }

        try {
            error = null

            val result = RetrofitInstance.api.getSchedule(
                selectedGroup,
                start,
                end
            )

            schedule = result
            cache[cacheKey] = result

            Log.d("CACHE_DEBUG", "Loaded from API")

        } catch (e: IOException) {
            error = "Нет подключения к серверу"

        } catch (e: HttpException) {
            error = "Ошибка сервера"

        } catch (e: Exception) {
            error = "Неизвестная ошибка"
        }
    }

    LaunchedEffect(selectedGroup) {
        loading = true
        fetchSchedule()
        loading = false
    }
    val scope = rememberCoroutineScope()

    val pullRefreshState = rememberPullRefreshState(
        refreshing = loading,
        onRefresh = {
            scope.launch {
                loading = true
                fetchSchedule()
                loading = false
            }
        }
    )


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

        Box(
            modifier = Modifier
                .padding(padding)
                .pullRefresh(pullRefreshState)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    error != null -> {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = error!!,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }

                    else -> {
                        ScheduleList(schedule)
                    }
                }
            }
            PullRefreshIndicator(
                refreshing = loading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}