// File: app/src/main/java/com/example/eduoptimaolapii/ui/viewmodels/DashboardViewModel.kt
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
import javax.inject.Inject


data class DashboardState(
    val isLoading: Boolean = false,
    val dashboardResumen: DashboardResumen? = null,
    val matriculasPorMes: Map<String, Float> = emptyMap(),
    val estudiantesPorMunicipio: Map<String, Float> = emptyMap(),
    val rendimientoPorGrado: Map<String, Float> = emptyMap(),
    val eventosProximos: List<EventoInminente> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState: StateFlow<DashboardState> = _dashboardState.asStateFlow()

    fun loadDashboardData() {
        _dashboardState.value = _dashboardState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Cargar datos del OLAP - SOLO APIs
                val resumen = dashboardRepository.getDashboardResumen()
                val matriculasMes = dashboardRepository.getMatriculasPorMes()
                val estudiantesMunicipio = dashboardRepository.getEstudiantesPorMunicipio()
                val rendimientoGrado = dashboardRepository.getRendimientoPorGrado()
                val eventos = dashboardRepository.getEventosProximos()

                _dashboardState.value = DashboardState(
                    isLoading = false,
                    dashboardResumen = resumen,
                    matriculasPorMes = matriculasMes,
                    estudiantesPorMunicipio = estudiantesMunicipio,
                    rendimientoPorGrado = rendimientoGrado,
                    eventosProximos = eventos
                )
            } catch (e: Exception) {
                // SOLO ERROR - NO DATOS DE DEMOSTRACIÓN
                _dashboardState.value = DashboardState(
                    isLoading = false,
                    error = when {
                        e.message?.contains("network", ignoreCase = true) == true ->
                            "❌ Error de conexión. Verifique su internet"
                        e.message?.contains("timeout", ignoreCase = true) == true ->
                            "⏰ Timeout. Las APIs no responden"
                        e.message?.contains("404", ignoreCase = true) == true ->
                            "🔍 API no encontrada. Verifique las URLs"
                        e.message?.contains("401", ignoreCase = true) == true ->
                            "🔐 No autorizado. Verifique credenciales"
                        e.message?.contains("500", ignoreCase = true) == true ->
                            "⚡ Error del servidor. Intente más tarde"
                        else -> "❌ Error al cargar datos: ${e.message}"
                    }
                )
            }
        }
    }

    fun refreshData() {
        loadDashboardData()
    }
}