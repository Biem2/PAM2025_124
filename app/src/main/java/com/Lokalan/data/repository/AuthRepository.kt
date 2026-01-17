package com.Lokalan.data.repository

import com.Lokalan.data.dto.request.LoginRequest
import com.Lokalan.data.dto.request.RegisterRequest
import com.Lokalan.data.dto.response.AuthResponse
import com.Lokalan.data.service.ApiService
import retrofit2.Response

class AuthRepository(private val apiService: ApiService) {
    suspend fun login(loginRequest: LoginRequest): Response<AuthResponse> {
        return apiService.login(loginRequest)
    }

    suspend fun register(registerRequest: RegisterRequest): Response<AuthResponse> {
        return apiService.register(registerRequest)
    }

    suspend fun logout(token: String): Response<AuthResponse> {
        return apiService.logout("Bearer $token")
    }

    suspend fun getProfile(token: String): Response<AuthResponse> {
        return apiService.getProfile("Bearer $token")
    }

    suspend fun updateProfile(token: String, nama: String, email: String): Response<AuthResponse> {
        return apiService.updateProfile("Bearer $token", nama, email)
    }
}