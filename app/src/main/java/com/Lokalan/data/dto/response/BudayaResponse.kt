package com.Lokalan.data.dto.response


import com.Lokalan.data.model.Budaya
import com.google.gson.annotations.SerializedName

data class BudayaResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Budaya>? = null,
    @SerializedName("budaya") val budaya: Budaya? = null
)