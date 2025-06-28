package com.example.eventtracker.data.login

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.eventtracker.dto.GetUserDetailRequest
import com.example.eventtracker.dto.GetUserDetailResponse
import com.example.eventtracker.dto.UpdateTokenRequest
import com.example.eventtracker.dto.UpdateTokenResponse
import com.example.eventtracker.model.SignUpResponse
import com.example.eventtracker.model.UserData
import com.example.eventtracker.model.UserLogInRequest
import com.example.eventtracker.model.UserLogInResponse
import com.example.eventtracker.model.UserSIgnUpRequest
import java.time.LocalDate
import javax.inject.Singleton

@Singleton
class NetworkLogInRepository(
    private val userLoginService: UserLoginService
) {
    suspend fun signInUser(username: String, password: String): UserLogInResponse {
        return userLoginService.signInUser(UserLogInRequest(username, password))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun signUpUser(
        username: String,
        password: String,
        email: String,
        collegeId: String
    ): SignUpResponse {
        return userLoginService.signUpUser(
            UserSIgnUpRequest(
                username, password, email, collegeId
            )
        )

    }
    suspend fun getUserById(
        userId: String
    ): GetUserDetailResponse {
        val getUserDetailRequest = GetUserDetailRequest(userId)
        return userLoginService.getUserById(getUserDetailRequest)
    }

    suspend fun updateToken(token:String): UpdateTokenResponse {
        val updateTokenRequest = UpdateTokenRequest(token)
        return userLoginService.updateToken(updateTokenRequest);
    }
}