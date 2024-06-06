package com.example.eventtracker.model

data class EventData(
    val name:String="Technical Talk on Android",
    val image:String = "https://www.google.com",
    val date:String = "20/05/2024",
    val time:String = "10:00 AM",
    val location:String = "Av Auditorium,DSCE",
    val description:String = "THis is the description of the event",
    val category:String = "Technical",
)
