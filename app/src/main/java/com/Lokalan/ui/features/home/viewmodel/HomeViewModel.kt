package com.Lokalan.ui.features.home.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lokalan.data.model.Budaya
import com.Lokalan.data.repository.BudayaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val budayaList: List<Budaya> = emptyList(),
    val filteredBudayaList: List<Budaya> = emptyList(),
    val isLoading: Boolean = false,
    val showSearch: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val budayaRepository: BudayaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    private var searchJob: Job? = null

    init {
        loadBudaya()
    }

    fun loadBudaya() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = budayaRepository.getAllBudaya()
                if (response.isSuccessful && response.body()?.success == true) {
                    val budayaList = response.body()?.data ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        budayaList = budayaList,
                        filteredBudayaList = budayaList,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = response.body()?.message ?: "Gagal memuat data",
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

    fun toggleSearch() {
        _uiState.value = _uiState.value.copy(showSearch = !_uiState.value.showSearch)
        if (!_uiState.value.showSearch) {
            searchQuery = ""
            _uiState.value = _uiState.value.copy(
                filteredBudayaList = _uiState.value.budayaList
            )
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
        searchJob?.cancel()

        if (query.isEmpty()) {
            // Jika query kosong, tampilkan semua data
            _uiState.value = _uiState.value.copy(
                filteredBudayaList = _uiState.value.budayaList
            )
            return
        }

        searchJob = viewModelScope.launch {
            delay(500) // Debounce 500ms

            // Filter lokal berdasarkan query
            filterBudayaLocally(query)
        }
    }

    private fun filterBudayaLocally(query: String) {
        val lowercaseQuery = query.lowercase()

        val filteredList = _uiState.value.budayaList.filter { budaya ->
            // Cari di semua properti yang mungkin dicari user
            budaya.namaBudaya.lowercase().contains(lowercaseQuery) ||
                    budaya.deskripsi?.lowercase()?.contains(lowercaseQuery) == true ||
                    budaya.asalDaerah?.lowercase()?.contains(lowercaseQuery) == true ||
                    budaya.kategori?.namaKategori?.lowercase()?.contains(lowercaseQuery) == true
        }

        _uiState.value = _uiState.value.copy(
            filteredBudayaList = filteredList
        )
    }

    private fun searchBudayaFromApi(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val response = budayaRepository.searchBudaya(query)
                if (response.isSuccessful && response.body()?.success == true) {
                    val searchResults = response.body()?.data ?: emptyList()
                    _uiState.value = _uiState.value.copy(
                        budayaList = searchResults,
                        filteredBudayaList = searchResults,
                        isLoading = false,
                        errorMessage = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        errorMessage = response.body()?.message ?: "Pencarian gagal",
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

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun retry() {
        if (searchQuery.isNotEmpty()) {
            searchBudayaFromApi(searchQuery)
        } else {
            loadBudaya()
        }
    }

    fun refreshData() {
        loadBudaya()
    }

    // Fungsi tambahan untuk sorting/filtering jika diperlukan
    fun sortByRating() {
        val sortedList = _uiState.value.filteredBudayaList.sortedByDescending {
            it.ratingAvg ?: 0.0
        }
        _uiState.value = _uiState.value.copy(
            filteredBudayaList = sortedList
        )
    }

    fun filterByRegion(region: String) {
        val filteredList = if (region.isBlank()) {
            _uiState.value.budayaList
        } else {
            _uiState.value.budayaList.filter {
                it.asalDaerah?.equals(region, ignoreCase = true) == true
            }
        }
        _uiState.value = _uiState.value.copy(
            filteredBudayaList = filteredList
        )
    }
}