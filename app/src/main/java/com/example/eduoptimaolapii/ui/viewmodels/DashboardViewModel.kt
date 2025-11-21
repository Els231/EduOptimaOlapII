package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.model.olap.DashboardResumen
import com.example.eduoptimaolapii.data.repository.DashboardRepository
import com.example.eduoptimaolapii.data.remote.api.olap.EventoInminente
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import javax.inject.Inject

data class DashboardState(
    val isLoading: Boolean = false,
    val dashboardResumen: DashboardResumen? = null,
    val promedioPorTrimestre: Map<String, Float> = emptyMap(),
    val promedioPorMunicipio: Map<String, Float> = emptyMap(),
    val promedioPorGrado: Map<String, Float> = emptyMap(),
    val eventosProximosList: List<EventoInminente> = emptyList(), // Cambiado el nombre
    val error: String? = null,
    val isUsingDemoData: Boolean = false
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {
    companion object {
        private const val REFRESH_COOLDOWN_MS = 10000L
        private const val RESUMEN_TIMEOUT_MS = 15000L
        private const val CHARTS_TIMEOUT_MS = 10000L
        private const val EVENTOS_TIMEOUT_MS = 8000L
    }

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private var lastRefreshTime: Long = 0

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastRefreshTime < REFRESH_COOLDOWN_MS && _dashboardState.value.dashboardResumen != null) {
            return
        }

        _dashboardState.value = _dashboardState.value.copy(isLoading = true, error = null)
        lastRefreshTime = currentTime

        viewModelScope.launch {
            try {
                val resumenDeferred = async {
                    withTimeoutOrDefault(RESUMEN_TIMEOUT_MS, null) {
                        dashboardRepository.getDashboardResumen()
                    }
                }

                val trimestreDeferred = async {
                    withTimeoutOrDefault(CHARTS_TIMEOUT_MS, emptyMap<String, Float>()) {
                        dashboardRepository.getPromedioPorTrimestre()
                    }
                }

                val municipioDeferred = async {
                    withTimeoutOrDefault(CHARTS_TIMEOUT_MS, emptyMap<String, Float>()) {
                        dashboardRepository.getPromedioPorMunicipio()
                    }
                }

                val gradoDeferred = async {
                    withTimeoutOrDefault(CHARTS_TIMEOUT_MS, emptyMap<String, Float>()) {
                        dashboardRepository.getPromedioPorGrado()
                    }
                }

                val eventosDeferred = async {
                    withTimeoutOrDefault(EVENTOS_TIMEOUT_MS, emptyList<EventoInminente>()) {
                        dashboardRepository.getEventosProximos()
                    }
                }

                val resumen = resumenDeferred.await()
                val promedioTrimestre = trimestreDeferred.await()
                val promedioMunicipio = municipioDeferred.await()
                val promedioGrado = gradoDeferred.await()
                val eventosProximos = eventosDeferred.await()

                if (resumen == null && promedioTrimestre.isEmpty() && promedioMunicipio.isEmpty() &&
                    promedioGrado.isEmpty() && eventosProximos.isEmpty()) {
                    loadDemoData()
                } else {
                    _dashboardState.value = DashboardState(
                        isLoading = false,
                        dashboardResumen = resumen ?: createDemoResumen(),
                        promedioPorTrimestre = if (promedioTrimestre.isEmpty()) createDemoTrimestreData() else promedioTrimestre,
                        promedioPorMunicipio = if (promedioMunicipio.isEmpty()) createDemoMunicipioData() else promedioMunicipio,
                        promedioPorGrado = if (promedioGrado.isEmpty()) createDemoGradoData() else promedioGrado,
                        eventosProximosList = if (eventosProximos.isEmpty()) createDemoEventos() else eventosProximos,
                        isUsingDemoData = resumen == null
                    )
                }

                _isRefreshing.value = false

            } catch (e: Exception) {
                loadDemoData()
                _isRefreshing.value = false
            }
        }
    }

    fun refreshData() {
        if (_isRefreshing.value) return
        _isRefreshing.value = true
        loadDashboardData()
    }

    fun loadChartsData() {
        viewModelScope.launch {
            try {
                val promedioTrimestre = withTimeout(CHARTS_TIMEOUT_MS) {
                    dashboardRepository.getPromedioPorTrimestre()
                }
                val promedioMunicipio = withTimeout(CHARTS_TIMEOUT_MS) {
                    dashboardRepository.getPromedioPorMunicipio()
                }
                val promedioGrado = withTimeout(CHARTS_TIMEOUT_MS) {
                    dashboardRepository.getPromedioPorGrado()
                }

                _dashboardState.value = _dashboardState.value.copy(
                    promedioPorTrimestre = promedioTrimestre,
                    promedioPorMunicipio = promedioMunicipio,
                    promedioPorGrado = promedioGrado,
                    isUsingDemoData = false
                )
            } catch (e: Exception) {
                if (_dashboardState.value.promedioPorTrimestre.isEmpty()) {
                    _dashboardState.value = _dashboardState.value.copy(
                        promedioPorTrimestre = createDemoTrimestreData(),
                        promedioPorMunicipio = createDemoMunicipioData(),
                        promedioPorGrado = createDemoGradoData(),
                        isUsingDemoData = true
                    )
                }
            }
        }
    }

    fun loadEventosData() {
        viewModelScope.launch {
            try {
                val eventos = withTimeout(EVENTOS_TIMEOUT_MS) {
                    dashboardRepository.getEventosProximos()
                }

                _dashboardState.value = _dashboardState.value.copy(
                    eventosProximosList = eventos,
                    isUsingDemoData = false
                )
            } catch (e: Exception) {
                if (_dashboardState.value.eventosProximosList.isEmpty()) {
                    _dashboardState.value = _dashboardState.value.copy(
                        eventosProximosList = createDemoEventos(),
                        isUsingDemoData = true
                    )
                }
            }
        }
    }

    fun clearError() {
        _dashboardState.value = _dashboardState.value.copy(error = null)
    }

    private fun loadDemoData() {
        _dashboardState.value = DashboardState(
            isLoading = false,
            dashboardResumen = createDemoResumen(),
            promedioPorTrimestre = createDemoTrimestreData(),
            promedioPorMunicipio = createDemoMunicipioData(),
            promedioPorGrado = createDemoGradoData(),
            eventosProximosList = createDemoEventos(),
            isUsingDemoData = true
        )
    }

    private fun createDemoResumen(): DashboardResumen {
        return DashboardResumen(
            totalEstudiantes = 1250,
            totalMatriculas = 980,
            totalProfesores = 45,
            promedioGeneral = 85.5f,
            tasaAprobacion = 92.3f,
            totalGrados = 6,
            ingresosTotales = 125000.0,
            eventosProximos = 3,
            alertasRendimiento = 12,
            ultimaActualizacion = "2024-01-15"
        )
    }
    private fun createDemoTrimestreData(): Map<String, Float> {
        return mapOf(
            "Trimestre 1" to 82.3f,
            "Trimestre 2" to 85.7f,
            "Trimestre 3" to 88.1f,
            "Trimestre 4" to 86.2f
        )
    }

    private fun createDemoMunicipioData(): Map<String, Float> {
        return mapOf(
            "Municipio A" to 89.2f,
            "Municipio B" to 84.5f,
            "Municipio C" to 87.8f,
            "Municipio D" to 82.1f,
            "Municipio E" to 85.9f
        )
    }

    private fun createDemoGradoData(): Map<String, Float> {
        return mapOf(
            "Primero" to 80.5f,
            "Segundo" to 83.2f,
            "Tercero" to 86.7f,
            "Cuarto" to 88.9f,
            "Quinto" to 87.3f,
            "Sexto" to 85.1f
        )
    }

    private fun createDemoEventos(): List<EventoInminente> {
        return listOf(
            EventoInminente(
                id = 1,
                titulo = "Reunión de Padres",
                descripcion = "Reunión general de padres de familia para informar sobre el rendimiento académico",
                fecha = "2024-11-15",
                fechaInicio = "2024-11-15 09:00",
                fechaFinal = "2024-11-15 12:00",
                grado = "Todos los grados"
            ),
            EventoInminente(
                id = 2,
                titulo = "Feria Científica",
                descripcion = "Exposición de proyectos científicos estudiantiles",
                fecha = "2024-11-22",
                fechaInicio = "2024-11-22 08:00",
                fechaFinal = "2024-11-22 16:00",
                grado = "4to - 6to grado"
            ),
            EventoInminente(
                id = 3,
                titulo = "Festival Deportivo",
                descripcion = "Competencias deportivas intergrados",
                fecha = "2024-11-30",
                fechaInicio = "2024-11-30 07:30",
                fechaFinal = "2024-11-30 14:00",
                grado = "1ro - 6to grado"
            )
        )
    }

    private suspend fun <T> withTimeoutOrDefault(
        timeoutMs: Long = 10000,
        default: T,
        block: suspend () -> T
    ): T {
        return try {
            withTimeout(timeoutMs) {
                block()
            }
        } catch (e: TimeoutCancellationException) {
            default
        } catch (e: Exception) {
            default
        }
    }

    private fun parseErrorMessage(e: Exception): String {
        return when {
            e is java.net.UnknownHostException -> "🌐 No se puede conectar al servidor. Verifique su conexión a internet."
            e is java.net.SocketTimeoutException -> "⏰ Tiempo de espera agotado. Las APIs no responden."
            e is java.io.IOException -> "🔌 Error de conexión. Verifique su red e intente nuevamente."
            e.message?.contains("404", ignoreCase = true) == true -> "🔍 API no encontrada (404). Verifique las URLs en build.gradle"
            e.message?.contains("401", ignoreCase = true) == true -> "🔐 No autorizado (401). Verifique credenciales."
            e.message?.contains("403", ignoreCase = true) == true -> "🚫 Acceso denegado (403). Sin permisos para acceder."
            e.message?.contains("500", ignoreCase = true) == true -> "⚡ Error interno del servidor (500). Intente más tarde."
            e.message?.contains("502", ignoreCase = true) == true -> "🔧 Bad Gateway (502). Servicio no disponible."
            e.message?.contains("503", ignoreCase = true) == true -> "🛠️ Servicio no disponible (503). Las APIs están en mantenimiento."
            e.message?.contains("connection", ignoreCase = true) == true -> "📡 Error de conexión. Verifique las URLs: OLAP y MongoDB"
            else -> "❌ Error al conectar con las APIs: ${e.message ?: "Desconocido"}"
        }
    }
}