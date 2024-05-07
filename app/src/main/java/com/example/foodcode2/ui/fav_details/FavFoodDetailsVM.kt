package com.example.foodcode2.ui.fav_details

import FoodRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.api.Food
import com.example.foodcode2.dependencies.FoodCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavFoodDetailsUiState(
    val isLoading: Boolean = true,
    val food: Food? = null
)

class FavFoodDetailsVM(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FavFoodDetailsUiState> = MutableStateFlow(
        FavFoodDetailsUiState()
    )
    val uiState: StateFlow<FavFoodDetailsUiState> = _uiState.asStateFlow()

    //Obtiene los detalles de un alimento.
    fun setFood(barcodeProduct: String) {
        viewModelScope.launch {
            val foodResp = foodRepository.getFoodDetailById(barcodeProduct)
            if (foodResp.isSuccessful) {
                val food = foodResp.body()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        food = food
                    )
                }
            } else {
                Log.d(
                    "FavFoodDetailsVM",
                    "Error al obtener los detalles de la receta: ${foodResp.errorBody()?.string()}"
                )
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Get the Application object from extras
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return FavFoodDetailsVM(
                    (application as FoodCode).appContainer.FoodRepository
                ) as T
            }
        }
    }
}