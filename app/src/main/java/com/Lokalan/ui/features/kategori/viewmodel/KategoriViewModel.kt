package com.Lokalan.ui.features.kategori.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lokalan.data.model.Budaya
import com.Lokalan.data.model.KategoriBudaya
import com.Lokalan.data.repository.BudayaRepository
import com.Lokalan.data.repository.KategoriBudayaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class KategoriUiState(
    val kategoriList: List<KategoriBudaya> = emptyList(),
    val budayaByKategori: List<Budaya> = emptyList(),
    val selectedKategori: KategoriBudaya? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class KategoriViewModel(
    private val kategoriRepository: KategoriBudayaRepository,
    private val budayaRepository: BudayaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KategoriUiState())
    val uiState: StateFlow<KategoriUiState> = _uiState.asStateFlow()

    init {
        loadKategori()
    }

    fun loadKategori() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = kategoriRepository.getAllKategori()
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.value = _uiState.value.copy(
                        kategoriList = response.body()?.data ?: emptyList(),
                        isLoading = false
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = response.body()?.message ?: "Gagal memuat kategori",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Terjadi kesalahan: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun selectKategori(kategoriId: Int) {
        val selected = _uiState.value.kategoriList.find { it.id == kategoriId }
        _uiState.value = _uiState.value.copy(selectedKategori = selected)
        loadBudayaByKategori(kategoriId)
    }

    private fun loadBudayaByKategori(kategoriId: Int) {
        viewModelScope.launch {
            try {
                val response = budayaRepository.getBudayaByKategori(kategoriId)
                if (response.isSuccessful && response.body()?.success == true) {
                    _uiState.value = _uiState.value.copy(
                        budayaByKategori = response.body()?.data ?: emptyList()
                    )
                }
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun clearSelectedKategori() {
        _uiState.value = _uiState.value.copy(
            selectedKategori = null,
            budayaByKategori = emptyList()
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}