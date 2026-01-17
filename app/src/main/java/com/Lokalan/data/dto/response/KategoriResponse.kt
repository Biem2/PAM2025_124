package com.Lokalan.data.dto.response


import com.Lokalan.data.model.KategoriBudaya
import com.google.gson.annotations.SerializedName

data class KategoriResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<KategoriBudaya>? = null,
    @SerializedName("kategori") val kategori: KategoriBudaya? = null
)