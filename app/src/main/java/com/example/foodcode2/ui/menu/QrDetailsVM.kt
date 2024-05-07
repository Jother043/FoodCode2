package com.example.foodcode2.ui.menu

import FoodRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.api.Food
import com.example.foodcode2.dependencies.FoodCode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class QrDetailsUiState(
    val isLoading: Boolean = false, val food: Food? = null, val error: String = ""
)

class QrDetailsVM(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<QrDetailsUiState> = MutableStateFlow(
        QrDetailsUiState()
    )
    val uiState: StateFlow<QrDetailsUiState> = _uiState

    val _barcodeResult = MutableStateFlow<String?>(null)
    val barcodeResult: StateFlow<String?> = _barcodeResult
    fun setFood(barcode: String) {
        if(barcode.isEmpty()){
            _uiState.value = QrDetailsUiState(error = "Error obteniendo comidas por ids")
            return
        }else {

            Log.d("FoodListVM", "El codigo escaneado es el siguiente : $barcode")
            _uiState.value = QrDetailsUiState(isLoading = true)
            viewModelScope.launch {
                // Create a list with a single barcode
                val barcodes = listOf(barcode)
                val response = foodRepository.getProductByBarcode(barcodes)
                if (response.isSuccessful) {
                    response.body()?.let { foodResponse ->
                        // Convierte la respuesta del API a un objeto Food de foodsListResponse
                        foodResponse.forEach { food ->
                            food.isFavorite = false
                        }
                        _uiState.value = QrDetailsUiState(food = foodResponse.first())
                    }
                } else {
                    _uiState.value = QrDetailsUiState(error = "Error obteniendo comidas por ids")
                }
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>, extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return QrDetailsVM(
                    (application as FoodCode).appContainer.FoodRepository
                ) as T
            }
        }
    }
}