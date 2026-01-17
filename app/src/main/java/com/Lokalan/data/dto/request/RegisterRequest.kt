package com.Lokalan.data.dto.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("nama") val nama: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)