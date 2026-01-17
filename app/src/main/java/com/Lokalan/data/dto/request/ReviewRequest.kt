package com.Lokalan.data.dto.request

import com.google.gson.annotations.SerializedName

data class ReviewRequest(
    @SerializedName("budaya_id") val budayaId: Int,
    @SerializedName("nilai") val nilai: Int,
    @SerializedName("komentar") val komentar: String? = null
)