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
        println("🔍 AuthViewModel - login() llamado con: $email")

        if (email.isEmpty() || password.isEmpty()) {
            println("❌ AuthViewModel - Campos vacíos")
            _authState.value = AuthState(error = "Por favor complete todos los campos")
            return
        }

        println("✅ AuthViewModel - Campos válidos, iniciando login...")
        _authState.value = _authState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                println("🔄 AuthViewModel - Llamando a AuthRepository...")
                val usuario = authRepository.login(email, password)
                println("🎉 AuthViewModel - Login EXITOSO: ${usuario.nombre}")
                _authState.value = AuthState(
                    isAuthenticated = true,
                    usuario = usuario
                )
            } catch (e: Exception) {
                println("🔥 AuthViewModel - ERROR: ${e.message}")
                e.printStackTrace()
                _authState.value = AuthState(
                    error = "Error: ${e.message ?: "Error desconocido"}"
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
                    apellido = "", // Campo requerido por el modelo
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
                        e.message?.contains("409", ignoreCase = true) == true ->
                            "El usuario ya existe"
                        else -> "Error en registro: ${e.message ?: "Error desconocido"}"
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