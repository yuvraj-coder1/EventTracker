package com.example.eventtracker.ui.navigation

import kotlinx.serialization.Serializable
import com.example.eventtracker.model.EventData
import kotlinx.serialization.Contextual

@Serializable
data object HomeScreen

@Serializable
data class EventDetailsScreen(
    val name:String="",
    val image:String = "",
    val date:String = "",
    val time:String = "",
    val location:String = "",
    val description:String = "",
    val category:String = "",
)

@Serializable
data object ProfileScreen

@Serializable
data object PostEventScreen