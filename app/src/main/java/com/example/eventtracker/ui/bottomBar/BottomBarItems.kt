package com.example.eventtracker.ui.bottomBar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.eventtracker.ui.navigation.HomeScreen
import com.example.eventtracker.ui.navigation.PostEventScreen
import com.example.eventtracker.ui.navigation.ProfileScreen

sealed class BottomBarItem (
    val label: String,
    val icon: ImageVector,
    val route:Any,
) {
    data object HomeScreen: BottomBarItem(
        label = "Home",
        icon = Icons.Default.Home,
        route = com.example.eventtracker.ui.navigation.HomeScreen
    )
    data object PostEventScreen: BottomBarItem(
        label = "Post Event",
        icon = Icons.Default.Add,
        route = com.example.eventtracker.ui.navigation.PostEventScreen
    )
    data object ProfileScreen: BottomBarItem(
        label = "Profile",
        icon = Icons.Default.Person,
        route = com.example.eventtracker.ui.navigation.ProfileScreen
    )
}