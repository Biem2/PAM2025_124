package com.Lokalan.data.service

import com.Lokalan.data.dto.request.KategoriRequest
import com.Lokalan.data.dto.response.KategoriResponse
import retrofit2.Response
import retrofit2.http.*

interface KategoriService {
    @GET("kategori")
    suspend fun getAllKategori(): Response<KategoriResponse>

    @POST("kategori")
    suspend fun addKategori(
        @Header("Authorization") token: String,
        @Body request: KategoriRequest
    ): Response<KategoriResponse>

    @PUT("kategori/{id}")
    suspend fun updateKategori(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: KategoriRequest
    ): Response<KategoriResponse>

    @DELETE("kategori/{id}")
    suspend fun deleteKategori(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<KategoriResponse>
}