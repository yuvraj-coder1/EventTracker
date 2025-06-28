package com.example.eventtracker.dto

data class GetUserDetailResponse(
    val username: String,
    val email: String,
    val collegeId: String,
)
