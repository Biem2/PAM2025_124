package com.Lokalan.data.model

import com.google.gson.annotations.SerializedName

data class KategoriBudaya(
    @SerializedName("id") val id: Int,
    @SerializedName("nama_kategori") val namaKategori: String,
    @SerializedName("deskripsi") val deskripsi: String? = null,
    @SerializedName("created_at") val createdAt: String
)