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

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val isLoginSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    fun updateEmail(newEmail: String) {
        email = newEmail
        validateEmail()
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
        validatePassword()
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

    fun login() {
        if (!validateEmail() || !validatePassword()) {
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            try {
                val response = authRepository.login(
                    com.Lokalan.data.dto.request.LoginRequest(email, password)
                )

                if (response.isSuccessful && response.body()?.success == true) {
                    val authResponse = response.body()!!

                    // TODO: Simpan token dan user data menggunakan DataStore atau AppPreferenceManager
                    // Untuk sementara, hanya tandai login berhasil
                    _uiState.value = _uiState.value.copy(isLoginSuccess = true)
                } else {
                    val errorMsg = response.body()?.message ?: "Login gagal"
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