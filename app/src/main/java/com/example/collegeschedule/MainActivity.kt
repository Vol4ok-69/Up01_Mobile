package com.example.collegeschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.example.collegeschedule.data.local.FavoritesManager
import com.example.collegeschedule.data.theme.CollegeScheduleTheme
import com.example.collegeschedule.ui.favorites.FavoritesScreen
import com.example.collegeschedule.ui.navigation.AppDestination
import com.example.collegeschedule.ui.schedule.ScheduleScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CollegeScheduleTheme {

                val context = LocalContext.current
                val favoritesManager = remember { FavoritesManager(context) }

                var selectedGroup by rememberSaveable {
                    mutableStateOf("ИС-22")
                }

                var favorites by remember {
                    mutableStateOf(favoritesManager.getFavorites().toList())
                }

                var currentScreen by remember {
                    mutableStateOf<AppDestination>(AppDestination.Home)
                }

                Scaffold(
                    bottomBar = {
                        NavigationBar {

                            val items = listOf(
                                AppDestination.Home,
                                AppDestination.Favorites,
                                AppDestination.Profile
                            )

                            items.forEach { destination ->
                                NavigationBarItem(
                                    selected = currentScreen == destination,
                                    onClick = { currentScreen = destination },
                                    icon = {
                                        Icon(destination.icon, contentDescription = null)
                                    },
                                    label = {
                                        Text(destination.label)
                                    }
                                )
                            }
                        }
                    }
                ) { padding ->

                    Box(
                        modifier = androidx.compose.ui.Modifier
                            .fillMaxSize()
                            .padding(padding)
                    ) {

                        when (currentScreen) {


                            AppDestination.Home -> {
                                ScheduleScreen(
                                    selectedGroup = selectedGroup,
                                    onGroupSelected = { selectedGroup = it },
                                    isFavorite = favorites.contains(selectedGroup),
                                    onToggleFavorite = { group ->
                                        if (favorites.contains(group)) {
                                            favoritesManager.removeFavorite(group)
                                        } else {
                                            favoritesManager.addFavorite(group)
                                        }
                                        favorites =
                                            favoritesManager.getFavorites().toList()
                                    }
                                )
                            }

                            AppDestination.Favorites -> {
                                FavoritesScreen(
                                    favorites = favorites,
                                    onGroupClick = { group ->
                                        selectedGroup = group
                                        currentScreen = AppDestination.Home
                                    }
                                )
                            }

                            AppDestination.Profile -> {
                                Box(
                                    modifier = androidx.compose.ui.Modifier.fillMaxSize(),
                                    contentAlignment = androidx.compose.ui.Alignment.Center
                                ) {
                                    Text("Профиль студента")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}