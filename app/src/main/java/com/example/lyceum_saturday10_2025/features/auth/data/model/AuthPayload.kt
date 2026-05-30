package com.example.lyceum_saturday10_2025.features.auth.data.model

import com.google.gson.annotations.SerializedName

data class AuthPayload(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
