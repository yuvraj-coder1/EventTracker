package com.example.eventtracker.interceptor

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.example.eventtracker.data.login.NetworkLogInRepository
import com.example.eventtracker.ui.navigation.LogInScreen

import com.example.eventtracker.utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userLogInRepository: NetworkLogInRepository,
    private val sharedPreferences: SharedPreferences
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        Log.d("TokenAuthenticator", "Authenticating for response: $response")
        if (response.code == 403 || response.code==401) {
            Log.d("TokenAuthenticator", "Token expired")
            runBlocking {
                coroutineScope() {
                    withContext(Dispatchers.IO) {
                        val refreshToken = sharedPreferences.getString("refreshToken", "")
                        val resp = userLogInRepository.updateToken(refreshToken.toString())
                        sharedPreferences.edit { putString("jwt", resp.accessToken) }
                        sharedPreferences.edit { putString("refreshToken", resp.refreshToken) }
                        return@withContext
                    }
                }
            }
            return null
        }
        if(responseCount(response)>=3) {
            utils.logOutUser.value = true
            return null;
        }
        return TODO("Provide the return value")
    }
    fun responseCount(response:Response):Int {
        var prev = response.priorResponse
        var count=0;
        while(prev!=null){
            count++;
            prev = prev.priorResponse
            if(count>3)
                break;
        }
        return count;
    }
}
