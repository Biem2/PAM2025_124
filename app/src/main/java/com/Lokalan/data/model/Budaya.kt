package com.Lokalan.data.model

import com.google.gson.annotations.SerializedName

data class Budaya(
    @SerializedName("id") val id: Int,
    @SerializedName("kategori_id") val kategoriId: Int,
    @SerializedName("nama_budaya") val namaBudaya: String,
    @SerializedName("deskripsi") val deskripsi: String? = null,
    @SerializedName("asal_daerah") val asalDaerah: String? = null,
    @SerializedName("gambar") val gambar: String? = null,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("kategori") val kategori: KategoriBudaya? = null,
    @SerializedName("rating_avg") val ratingAvg: Double? = null,
    @SerializedName("total_reviews") val totalReviews: Int? = null
)