package com.Lokalan.data.dto.request

import com.google.gson.annotations.SerializedName

data class BudayaRequest(
    @SerializedName("kategori_id") val kategoriId: Int,
    @SerializedName("nama_budaya") val namaBudaya: String,
    @SerializedName("deskripsi") val deskripsi: String? = null,
    @SerializedName("asal_daerah") val asalDaerah: String? = null,
    @SerializedName("gambar") val gambar: String? = null
)