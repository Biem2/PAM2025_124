package com.Lokalan.data.repository

import com.Lokalan.data.dto.request.ReviewRequest
import com.Lokalan.data.dto.response.ReviewResponse
import com.Lokalan.data.service.ReviewService
import retrofit2.Response

class ReviewRepository(private val reviewService: ReviewService) {
    suspend fun getReviewsByBudaya(budayaId: Int): Response<ReviewResponse> {
        return reviewService.getReviewsByBudaya(budayaId)
    }

    suspend fun addReview(token: String, request: ReviewRequest): Response<ReviewResponse> {
        return reviewService.addReview("Bearer $token", request)
    }

    suspend fun updateReview(token: String, id: Int, request: ReviewRequest): Response<ReviewResponse> {
        return reviewService.updateReview("Bearer $token", id, request)
    }

    suspend fun deleteReview(token: String, id: Int): Response<ReviewResponse> {
        return reviewService.deleteReview("Bearer $token", id)
    }
}