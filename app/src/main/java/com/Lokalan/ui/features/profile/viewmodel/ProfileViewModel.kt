package com.Lokalan.ui.features.profile.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lokalan.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val namaError: String? = null,
    val emailError: String? = null,
    val errorMessage: String? = null
)

class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    var editNama by mutableStateOf("")
        private set

    var editEmail by mutableStateOf("")
        private set

    // Token dummy (ganti dengan cara ambil token yang bener nanti)
    private var currentToken: String? = "dummy_token"

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Simulasi loading data
                delay(1000)

                // Data dummy user (ganti dengan API call nanti)
                val dummyUser = User(
                    id = 1,
                    nama = "John Doe",
                    email = "john@example.com",
                    role = "user",
                    createdAt = "2024-01-01"
                )

                _uiState.value = _uiState.value.copy(
                    user = dummyUser,
                    isLoading = false,
                    errorMessage = null
                )

                // Set edit values
                editNama = dummyUser.nama
                editEmail = dummyUser.email

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Terjadi kesalahan: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun updateEditNama(newNama: String) {
        editNama = newNama
        validateNama()
    }

    fun updateEditEmail(newEmail: String) {
        editEmail = newEmail
        validateEmail()
    }

    private fun validateNama(): Boolean {
        val error = when {
            editNama.isEmpty() -> "Nama tidak boleh kosong"
            editNama.length < 3 -> "Nama minimal 3 karakter"
            else -> null
        }
        _uiState.value = _uiState.value.copy(namaError = error)
        return error == null
    }

    private fun validateEmail(): Boolean {
        val error = when {
            editEmail.isEmpty() -> "Email tidak boleh kosong"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail).matches() -> "Format email tidak valid"
            else -> null
        }
        _uiState.value = _uiState.value.copy(emailError = error)
        return error == null
    }

    fun updateProfile() {
        if (!validateNama() || !validateEmail()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdating = true)

            try {
                // Simulasi update profile
                delay(1000)

                val updatedUser = User(
                    id = _uiState.value.user?.id ?: 1,
                    nama = editNama,
                    email = editEmail,
                    role = _uiState.value.user?.role ?: "user",
                    createdAt = _uiState.value.user?.createdAt ?: ""
                )

                _uiState.value = _uiState.value.copy(
                    user = updatedUser,
                    isUpdating = false,
                    errorMessage = null
                )

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Terjadi kesalahan: ${e.message}",
                    isUpdating = false
                )
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            // Simulasi logout
            currentToken = null

            // Reset state
            _uiState.value = ProfileUiState()
            editNama = ""
            editEmail = ""
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(
            namaError = null,
            emailError = null,
            errorMessage = null
        )
    }

    fun refreshProfile() {
        loadProfile()
    }

    // Tambahin ini biar delay() bisa dipake
    private suspend fun delay(timeMillis: Long) {
        kotlinx.coroutines.delay(timeMillis)
    }
}