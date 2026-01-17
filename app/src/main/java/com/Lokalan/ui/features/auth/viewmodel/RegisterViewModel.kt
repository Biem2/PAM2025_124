package com.Lokalan.ui.features.auth.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Lokalan.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val nama: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val namaError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val isRegisterSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    var nama by mutableStateOf("")
        private set

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var confirmPassword by mutableStateOf("")
        private set

    fun updateNama(newNama: String) {
        nama = newNama
        validateNama()
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
        validateEmail()
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        validatePassword()
    }

    fun updateConfirmPassword(newConfirmPassword: String) {
        confirmPassword = newConfirmPassword
        validateConfirmPassword()
    }

    private fun validateNama(): Boolean {
        val error = when {
            nama.isEmpty() -> "Nama tidak boleh kosong"
            nama.length < 3 -> "Nama minimal 3 karakter"
            else -> null
        }
        _uiState.value = _uiState.value.copy(namaError = error)
        return error == null
    }

    private fun validateEmail(): Boolean {
        val error = when {
            email.isEmpty() -> "Email tidak boleh kosong"
            !email.contains("@") -> "Format email tidak valid"
            else -> null
        }
        _uiState.value = _uiState.value.copy(emailError = error)
        return error == null
    }

    private fun validatePassword(): Boolean {
        val error = when {
            password.isEmpty() -> "Password tidak boleh kosong"
            password.length < 6 -> "Password minimal 6 karakter"
            else -> null
        }
        _uiState.value = _uiState.value.copy(passwordError = error)
        return error == null
    }

    private fun validateConfirmPassword(): Boolean {
        val error = when {
            confirmPassword.isEmpty() -> "Konfirmasi password tidak boleh kosong"
            confirmPassword != password -> "Password tidak cocok"
            else -> null
        }
        _uiState.value = _uiState.value.copy(confirmPasswordError = error)
        return error == null
    }

    fun register() {
        if (!validateNama() || !validateEmail() || !validatePassword() || !validateConfirmPassword()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val response = authRepository.register(
                    com.Lokalan.data.dto.request.RegisterRequest(nama, email, password)
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    // TODO: Jika perlu simpan data user, gunakan DataStore atau AppPreferenceManager
                    _uiState.value = _uiState.value.copy(isRegisterSuccess = true)
                } else {
                    val errorMsg = response.body()?.message ?: "Registrasi gagal"
                    _uiState.value = _uiState.value.copy(errorMessage = errorMsg)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Terjadi kesalahan: ${e.message}"
                )
            } finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}