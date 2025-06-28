package com.example.eventtracker.interceptor

import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val prefs: SharedPreferences
) : Interceptor {
    private var KEY_JWT = "jwt"
    companion object {
        private const val HEADER_AUTH = "Authorization"
        private const val TOKEN_PREFIX = "Bearer "

    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        if(original.url.toString().contains("/auth/refresh-token"))
            KEY_JWT = "refreshToken"
        val token = prefs.getString(KEY_JWT, null)
        Log.d("AuthInterceptor", "Adding token to request: $token")
        val request = if (!token.isNullOrEmpty()) {
            Log.d("AuthInterceptor", "Adding token to request: $token")
            original.newBuilder()
                .header(HEADER_AUTH, TOKEN_PREFIX + token)
                .build()
        } else {
            original
        }
        return chain.proceed(request)
    }
}
