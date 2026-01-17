package com.Lokalan.data.service

import com.Lokalan.data.dto.request.BudayaRequest
import com.Lokalan.data.dto.response.BudayaDetailResponse
import com.Lokalan.data.dto.response.BudayaResponse
import retrofit2.Response
import retrofit2.http.*

interface BudayaService {
    @GET("budaya")
    suspend fun getAllBudaya(): Response<BudayaResponse>

    @GET("budaya/kategori/{kategoriId}")
    suspend fun getBudayaByKategori(@Path("kategoriId") kategoriId: Int): Response<BudayaResponse>

    @GET("budaya/search")
    suspend fun searchBudaya(@Query("q") query: String): Response<BudayaResponse>

    // Di ApiService.kt
    @GET("budaya/{id}")
    suspend fun getBudayaDetail(@Path("id") id: Int): Response<BudayaDetailResponse>

    @POST("budaya")
    suspend fun addBudaya(
        @Header("Authorization") token: String,
        @Body request: BudayaRequest
    ): Response<BudayaResponse>

    @PUT("budaya/{id}")
    suspend fun updateBudaya(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: BudayaRequest
    ): Response<BudayaResponse>

    @DELETE("budaya/{id}")
    suspend fun deleteBudaya(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<BudayaResponse>
}