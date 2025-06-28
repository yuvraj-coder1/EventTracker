package com.example.eventtracker.data.event

import android.app.usage.UsageEvents.Event
import com.example.eventtracker.dto.CreateEventRequest
import com.example.eventtracker.dto.GeneralResponse
import com.example.eventtracker.model.GetEventResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface EventApiService {
    @GET("all-events")
    suspend fun getEvents(): GetEventResponse

    @Multipart
    @POST("new-event")
    suspend fun createEvent(
        @Part("eventName")        name: RequestBody,
        @Part("eventDescription") description: RequestBody,
        @Part("eventCategory")    category: RequestBody,
        @Part("eventDate")        date: RequestBody,
        @Part("eventTime")        time: RequestBody,
        @Part("location")         location: RequestBody,
        @Part("eventLink")        link: RequestBody,
        @Part                     image: MultipartBody.Part?
    ): GeneralResponse

    @POST("bookmark-event")
    suspend fun bookmarkEvent(
        @Body objectId: String
    ): GeneralResponse

    @POST("unbookmark-event")
    suspend fun unBookmarkEvent(
        @Body objectId: String
    ): GeneralResponse

    @GET("bookmarked-event")
    suspend fun getBookmarkedEvents(): GetEventResponse

    @GET("hosted-event")
    suspend fun getUserEvents(): GetEventResponse

}