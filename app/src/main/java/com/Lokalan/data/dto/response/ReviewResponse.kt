package com.Lokalan.data.dto.response


import com.Lokalan.data.model.Review
import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Review>? = null,
    @SerializedName("rating_avg") val ratingAvg: Double? = null,
    @SerializedName("total_reviews") val totalReviews: Int? = null
)