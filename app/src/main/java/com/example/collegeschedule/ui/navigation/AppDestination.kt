package com.example.collegeschedule.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class AppDestination(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : AppDestination(
        route = "home",
        label = "Home",
        icon = Icons.Default.Home
    )

    object Favorites : AppDestination(
        route = "favorites",
        label = "Favorites",
        icon = Icons.Default.Favorite
    )

    object Profile : AppDestination(
        route = "profile",
        label = "Profile",
        icon = Icons.Default.Person
    )
}
