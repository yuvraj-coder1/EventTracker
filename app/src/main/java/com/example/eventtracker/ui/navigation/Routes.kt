package com.example.eventtracker.ui.navigation

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import com.example.eventtracker.model.EventData
import kotlinx.serialization.Contextual

@Serializable
data object HomeScreen

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class EventDetailsScreen(
    val name:String="",
    val image:String = "",
    val date:String = "",
    val time:String = "",
    val location:String = "",
    val description:String = "",
    val category:String = "",
    val eventLink:String = "",
    val eventId:String = "",
    val eventImageUrl:String = ""
)

@Serializable
data object ProfileScreen

@Serializable
data object PostEventScreen

@Serializable
data object LogInScreen

@Serializable
data object UserEventsScreen