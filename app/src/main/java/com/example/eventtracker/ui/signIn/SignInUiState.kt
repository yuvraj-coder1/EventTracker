package com.example.eventtracker.ui.signIn

data class SignInUiState(
    val username:String="",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isSignIn:Boolean = true,
    val collegeId:String="",
    )
