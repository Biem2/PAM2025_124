package com.Lokalan.ui.features.detail.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lokalan.data.model.Budaya
import com.Lokalan.data.model.Review
import com.Lokalan.data.repository.BudayaRepository  // ✅ Perbaiki import path
import com.Lokalan.data.repository.ReviewRepository  // ✅ Perbaiki import path
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val budaya: Budaya? = null,
    val reviews: List<Review> = emptyList(),
    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val userRating: Int = 0,
    val hasUserRated: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val budayaRepository: BudayaRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState(
        // Untuk sementara, anggap user belum login
        isUserLoggedIn = false  // Nanti bisa diganti jika ada token management
    ))
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    var comment by mutableStateOf("")
        private set

    private fun checkUserLoginStatus() {
        // TODO: Implementasi cek login jika ada token management
        _uiState.value = _uiState.value.copy(
            isUserLoggedIn = false  // Sementara false
        )
    }

    fun loadBudayaDetail(budayaId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = budayaRepository.getBudayaDetail(budayaId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.value = _uiState.value.copy(
                        // ERROR HILANG: Karena .data sekarang adalah Budaya tunggal
                        budaya = response.body()?.data,
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = response.body()?.message ?: "Gagal",
                        isLoading = false,
                        budaya = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = e.message)
            }
        }
    }

    // Di dalam DetailViewModel
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews

    fun loadReviews(budayaId: Int) {
        viewModelScope.launch {
            try {
                val response = reviewRepository.getReviewsByBudaya(budayaId)
                if (response.isSuccessful && response.body()?.success == true) {
                    val reviewList = response.body()?.data ?: emptyList()

                    // PENTING: Update UI State di sini
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = reviewList // Data dari logcat dimasukkan ke sini
                        )
                    }
                    Log.d("DetailVM", "Ulasan berhasil dimuat: ${reviewList.size}")
                }
            } catch (e: Exception) {
                Log.e("DetailVM", "Error: ${e.message}")
            }
        }
    }
    private fun checkIfUserHasRated() {
        // TODO: Implementasi cek rating user jika ada user management
        // Untuk sementara, selalu false
        _uiState.value = _uiState.value.copy(
            hasUserRated = false,
            userRating = 0
        )
    }

    fun setRating(rating: Int) {
        _uiState.value = _uiState.value.copy(userRating = rating)
    }

    fun updateComment(newComment: String) {
        comment = newComment
    }

    fun submitReview(budayaId: Int) {
        if (!_uiState.value.isUserLoggedIn || _uiState.value.userRating == 0) {
            return
        }

        viewModelScope.launch {
            try {
                // TODO: Tambahkan token jika ada authentication
                val response = reviewRepository.addReview(
                    "", // Token kosong untuk sementara
                    com.Lokalan.data.dto.request.ReviewRequest(
                        budayaId = budayaId,
                        nilai = _uiState.value.userRating,
                        komentar = comment.ifEmpty { null }
                    )
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    // Refresh reviews
                    loadReviews(budayaId)

                    // Reset form
                    comment = ""
                    _uiState.value = _uiState.value.copy(
                        userRating = 0,
                        hasUserRated = true
                    )
                }
            } catch (e: Exception) {
                // Handle error - bisa diabaikan untuk sekarang
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}