package com.example.lyceum_saturday10_2025.features.auth.data

import android.content.Context
import com.example.lyceum_saturday10_2025.common.api.TokensResponse
import com.example.lyceum_saturday10_2025.features.auth.data.model.AuthPayload
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthRepository(val applicationContext: Context) {

    private val api by lazy { getRetrofit() }

    suspend fun register(username: String, password: String): TokensResponse {
        return api.register(AuthPayload(username, password))
    }

    suspend fun login(username: String, password: String): TokensResponse {
        return api.login(AuthPayload(username, password))
    }

    private fun getRetrofit(): AuthApi {
        val httpClient = Builder()
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        httpClient.addInterceptor(logging)
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        return retrofit.create(AuthApi::class.java)
    }
}