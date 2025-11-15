// File: app/src/main/java/com/example/eduoptimaolapii/ui/viewmodels/AuthViewModel.kt
package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.model.mongodb.UsuarioMongo
import com.example.eduoptimaolapii.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val isLoading: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val usuario: UsuarioMongo? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState(error = "Por favor complete todos los campos")
            return
        }

        _authState.value = _authState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val usuario = authRepository.login(email, password)
                _authState.value = AuthState(
                    isAuthenticated = true,
                    usuario = usuario
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    error = when {
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "Error de conexión. Verifique su internet"
                        e.message?.contains("401", ignoreCase = true) == true ->
                            "Credenciales incorrectas"
                        else -> "Error de autenticación: ${e.message}"
                    }
                )
            }
        }
    }

    fun register(name: String, email: String, password: String, confirmPassword: String) {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            _authState.value = AuthState(error = "Por favor complete todos los campos")
            return
        }

        if (password != confirmPassword) {
            _authState.value = AuthState(error = "Las contraseñas no coinciden")
            return
        }

        if (password.length < 6) {
            _authState.value = AuthState(error = "La contraseña debe tener al menos 6 caracteres")
            return
        }

        _authState.value = _authState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val usuario = UsuarioMongo(
                    nombre = name,
                    apellido = "",
                    email = email,
                    password = password,
                    rol = "estudiante"
                )
                val usuarioRegistrado = authRepository.register(usuario)
                _authState.value = AuthState(
                    isAuthenticated = true,
                    usuario = usuarioRegistrado
                )
            } catch (e: Exception) {
                _authState.value = AuthState(
                    error = when {
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "Error de conexión. Verifique su internet"
                        e.message?.contains("400", ignoreCase = true) == true ->
                            "El email ya está registrado"
                        else -> "Error en registro: ${e.message}"
                    }
                )
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }

    fun logout() {
        _authState.value = AuthState()
    }
}