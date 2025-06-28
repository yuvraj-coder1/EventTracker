package com.example.eventtracker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.eventtracker.data.event.EventApiService
import com.example.eventtracker.data.event.NetworkEventRepository
import com.example.eventtracker.data.login.NetworkLogInRepository
import com.example.eventtracker.data.login.UserLoginService
import com.example.eventtracker.interceptor.AuthInterceptor
import com.example.eventtracker.interceptor.TokenAuthenticator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.IOException
import java.security.GeneralSecurityException
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideTokenAuthenticator(sharedPreferences: SharedPreferences,userLoginRepository: NetworkLogInRepository): TokenAuthenticator {
        return TokenAuthenticator(userLoginRepository,sharedPreferences)
    }
    
    @Provides
    @Singleton
    fun provideOkHttpClient(auth: AuthInterceptor,tokenAuthenticator: TokenAuthenticator): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(auth)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .authenticator(tokenAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    fun providesLogInApi(auth: AuthInterceptor): UserLoginService {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor(auth)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        return Retrofit.Builder()
            .baseUrl("https://evenettracker-backend-my.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun providesLoginRepository(userLoginService: UserLoginService): NetworkLogInRepository =
        NetworkLogInRepository(userLoginService = userLoginService)

    @Provides
    @Singleton
    fun providesEventApi(okHttpClient: OkHttpClient): EventApiService = Retrofit.Builder()
        .baseUrl("https://evenettracker-backend-my.onrender.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create()

    @Provides
    @Singleton
    fun providesEventRepository(eventApiService: EventApiService): NetworkEventRepository =
        NetworkEventRepository(eventApiService = eventApiService)

    @Provides
    @Singleton
    fun provideMasterKey(@ApplicationContext context: Context): MasterKey {
        return MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
    }

    @Provides
    @Singleton
    fun provideEncryptedSharedPreferences(
        @ApplicationContext context: Context,
        masterKey: MasterKey
    ): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "secret_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}