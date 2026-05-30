package com.example.lyceum_saturday10_2025.features.auth.data

import com.example.lyceum_saturday10_2025.common.api.TokensResponse
import com.example.lyceum_saturday10_2025.features.auth.data.model.AuthPayload
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("/register")
    suspend fun register(@Body body: AuthPayload): TokensResponse

    @POST("/login")
    suspend fun login(@Body body: AuthPayload): TokensResponse
}