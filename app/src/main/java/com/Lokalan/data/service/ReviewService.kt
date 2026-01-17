package com.Lokalan.data.service

import com.Lokalan.data.dto.request.ReviewRequest
import com.Lokalan.data.dto.response.ReviewResponse
import retrofit2.Response
import retrofit2.http.*

interface ReviewService {
    @GET("reviews/budaya/bybudaya/{budayaId}")
    suspend fun getReviewsByBudaya(@Path("budayaId") budayaId: Int): Response<ReviewResponse>

    @POST("reviews/{id}")
    suspend fun addReview(
        @Header("Authorization") token: String,
        @Body request: ReviewRequest
    ): Response<ReviewResponse>

    @PUT("reviews/{id}")
    suspend fun updateReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: ReviewRequest
    ): Response<ReviewResponse>

    @DELETE("reviews/{id}")
    suspend fun deleteReview(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<ReviewResponse>
}