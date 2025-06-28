package com.example.eventtracker.data.login

import com.example.eventtracker.dto.GetUserDetailRequest
import com.example.eventtracker.dto.GetUserDetailResponse
import com.example.eventtracker.dto.UpdateTokenRequest
import com.example.eventtracker.dto.UpdateTokenResponse
import com.example.eventtracker.model.SignUpResponse
import com.example.eventtracker.model.UserData
import com.example.eventtracker.model.UserLogInRequest
import com.example.eventtracker.model.UserLogInResponse
import com.example.eventtracker.model.UserSIgnUpRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


interface UserLoginService {

    @retrofit2.http.POST("public/login")
    suspend fun signInUser(
        @Body request: UserLogInRequest
    ): UserLogInResponse

    @retrofit2.http.POST("public/signup")
    suspend fun signUpUser(
        @Body request: UserSIgnUpRequest
    ): SignUpResponse

    @POST("user-detail")
    suspend fun getUserById(
        @Body request: GetUserDetailRequest
    ): GetUserDetailResponse

    @POST("auth/refresh-token")
    suspend fun updateToken(
        @Body request: UpdateTokenRequest
    ): UpdateTokenResponse
}