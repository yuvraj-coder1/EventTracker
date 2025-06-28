package com.example.eventtracker.dto

import com.example.eventtracker.model.EventData
import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("objectId")
    val eventId: String,
    val eventName: String,
    val eventDescription: String,
    val eventCategory: String,
    val eventDate: String,
    val eventTime: String,
    val location: String,
    val eventLink: String?,
    val eventImageUrl: String?
)
fun EventDto.toEventData(): EventData {
    return EventData(
        eventId = eventId,
        name = eventName,
        description = eventDescription,
        category = eventCategory,
        date = eventDate,
        time = eventTime,
        location = location,
        eventLink = eventLink?:"",
        eventImageUrl = eventImageUrl?:""
    )
}
