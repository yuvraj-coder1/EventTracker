package com.example.eventtracker.data.event

import com.example.eventtracker.dto.CreateEventRequest
import com.example.eventtracker.dto.GeneralResponse
import com.example.eventtracker.model.GetEventResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Singleton

@Singleton
class NetworkEventRepository(
    private val eventApiService: EventApiService
) {
    suspend fun getEvents(): GetEventResponse {
        return eventApiService.getEvents()
    }
    suspend fun createEvent(
         namePart: RequestBody,
         descriptionPart: RequestBody,
         datePart: RequestBody,
         timePart: RequestBody,
         locationPart: RequestBody,
         imagePart: MultipartBody.Part?,
         categoryPart: RequestBody,
         eventLinkPart: RequestBody
    ): GeneralResponse {
        return eventApiService.createEvent(
            name = namePart,
            description = descriptionPart,
            date = datePart,
            time = timePart,
            location = locationPart,
            image = imagePart,
            category = categoryPart,
            link = eventLinkPart,
        )
    }

    suspend fun bookmarkEvent(
        objectId: String
    ): GeneralResponse {
        return eventApiService.bookmarkEvent(objectId)
    }
    suspend fun unBookmarkEvent(
        objectId: String
    ): GeneralResponse {
        return eventApiService.unBookmarkEvent(objectId)
    }

    suspend fun getUserEvents(): GetEventResponse {
        return eventApiService.getUserEvents()
    }

    suspend fun getBookmarkedEvents(): GetEventResponse {
        return eventApiService.getBookmarkedEvents()
    }


}