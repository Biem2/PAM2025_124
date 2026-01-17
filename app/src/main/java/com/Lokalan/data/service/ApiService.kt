package com.Lokalan.data.service


import com.Lokalan.data.dto.request.LoginRequest
import com.Lokalan.data.dto.request.RegisterRequest
import com.Lokalan.data.dto.response.AuthResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>

    @POST("auth/logout")
    suspend fun logout(@Header("Authorization") token: String): Response<AuthResponse>

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<AuthResponse>

    @PUT("auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Query("nama") nama: String,
        @Query("email") email: String
    ): Response<AuthResponse>
}