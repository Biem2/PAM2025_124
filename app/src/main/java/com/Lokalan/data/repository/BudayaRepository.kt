package com.Lokalan.data.repository

import com.Lokalan.data.dto.request.BudayaRequest
import com.Lokalan.data.dto.response.BudayaDetailResponse
import com.Lokalan.data.dto.response.BudayaResponse
import com.Lokalan.data.model.Budaya
import com.Lokalan.data.service.BudayaService
import retrofit2.Response

class BudayaRepository(private val budayaService: BudayaService) {
    suspend fun getAllBudaya(): Response<BudayaResponse> {
        return budayaService.getAllBudaya()
    }

    suspend fun getBudayaByKategori(kategoriId: Int): Response<BudayaResponse> {
        return budayaService.getBudayaByKategori(kategoriId)
    }

    suspend fun searchBudaya(query: String): Response<BudayaResponse> {
        return budayaService.searchBudaya(query)
    }

    suspend fun getBudayaDetail(id: Int): Response<BudayaDetailResponse> {
        return budayaService.getBudayaDetail(id)
    }

    suspend fun addBudaya(token: String, request: BudayaRequest): Response<BudayaResponse> {
        return budayaService.addBudaya("Bearer $token", request)
    }

    suspend fun updateBudaya(token: String, id: Int, request: BudayaRequest): Response<BudayaResponse> {
        return budayaService.updateBudaya("Bearer $token", id, request)
    }

    suspend fun deleteBudaya(token: String, id: Int): Response<BudayaResponse> {
        return budayaService.deleteBudaya("Bearer $token", id)
    }
}