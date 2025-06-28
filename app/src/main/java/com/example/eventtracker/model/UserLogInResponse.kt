package com.example.eventtracker.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializer

data class UserLogInResponse(
    val success: Boolean,
    val message: String,
    val data: LogInResponseData
)
data class LogInResponseData(
    @SerializedName("access_token")
    val jwt: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    val userId:String
)
