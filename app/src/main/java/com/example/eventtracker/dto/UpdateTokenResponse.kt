package com.example.eventtracker.dto


data class UpdateTokenResponse(
    val refreshToken: String,
    val accessToken: String
)
