package com.example.eventtracker.dto

data class CreateEventRequest(
    val eventName: String,
    val eventDescription: String,
    val eventCategory: String,
    val eventDate: String,
    val eventTime: String,
    val location: String,
    val eventLink: String
)
