package com.example.eventtracker.model

data class UserSIgnUpRequest(
    val username:  String,
    val password:  String,
    val email:     String,
    val collegeId: String
)
