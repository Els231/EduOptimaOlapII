package com.example.eduoptimaolapii.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eduoptimaolapii.data.repository.MongoDBRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MongoCollection(
    val name: String,
    val count: Long,
    val documents: List<Any>
)

data class MongoDBState(
    val isLoading: Boolean = false,
    val collections: List<MongoCollection> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class MongoDBViewModel @Inject constructor(
    private val mongoDBRepository: MongoDBRepository
) : ViewModel() {

    private val _mongoState = MutableStateFlow(MongoDBState())
    val mongoState: StateFlow<MongoDBState> = _mongoState.asStateFlow()

    fun loadMongoData() {
        _mongoState.value = _mongoState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                val collections = mongoDBRepository.getCollections()
                _mongoState.value = MongoDBState(
                    isLoading = false,
                    collections = collections
                )
            } catch (e: Exception) {
                _mongoState.value = _mongoState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Error al cargar datos de MongoDB"
                )
            }
        }
    }

    fun refreshData() {
        loadMongoData()
    }
}