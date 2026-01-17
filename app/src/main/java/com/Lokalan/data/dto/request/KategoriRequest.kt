package com.Lokalan.data.dto.request

import com.google.gson.annotations.SerializedName

data class KategoriRequest(
    @SerializedName("nama_kategori") val namaKategori: String,
    @SerializedName("deskripsi") val deskripsi: String? = null
)