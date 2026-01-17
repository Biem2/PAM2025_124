package com.Lokalan.data.model


import com.google.gson.annotations.SerializedName

data class Review(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("budaya_id") val budayaId: Int,
    val nilai: Int,
    val komentar: String?,
    // Tambahkan tanda tanya (?) agar tidak crash jika datanya kosong
    @SerializedName("created_at") val createdAt: String? = null,
    val user: UserReview? = null
)

data class UserReview(
    val nama: String?,
    val email: String?
)