package com.Lokalan.data.repository

import com.Lokalan.data.dto.request.KategoriRequest
import com.Lokalan.data.dto.response.KategoriResponse
import com.Lokalan.data.service.KategoriService
import retrofit2.Response

class KategoriBudayaRepository(private val kategoriService: KategoriService) {
    suspend fun getAllKategori(): Response<KategoriResponse> {
        return kategoriService.getAllKategori()
    }

    suspend fun addKategori(token: String, request: KategoriRequest): Response<KategoriResponse> {
        return kategoriService.addKategori("Bearer $token", request)
    }

    suspend fun updateKategori(token: String, id: Int, request: KategoriRequest): Response<KategoriResponse> {
        return kategoriService.updateKategori("Bearer $token", id, request)
    }

    suspend fun deleteKategori(token: String, id: Int): Response<KategoriResponse> {
        return kategoriService.deleteKategori("Bearer $token", id)
    }
}