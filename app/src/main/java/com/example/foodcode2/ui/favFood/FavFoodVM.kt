package com.example.foodcode2.ui.favFood

import com.example.foodcode2.api.Food
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.FavoriteFoodRepository
import com.example.foodcode2.repositories.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavFoodUiState(
    val isLoading: Boolean = true,
    val favFoodList: List<Food> = emptyList(),
)

class FavFoodVM(
    private val productRepository: ProductRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<FavFoodUiState> = MutableStateFlow(FavFoodUiState())
    val uiState: StateFlow<FavFoodUiState> = _uiState.asStateFlow()

    init {
        fetchFavFoods()
    }

    //Obtiene los alimentos favoritos.
    fun fetchFavFoods() {
        viewModelScope.launch {
            _uiState.value = FavFoodUiState(isLoading = true)
            val favFoodsList = productRepository.getFavorites()
            _uiState.value = FavFoodUiState(isLoading = false, favFoodList = favFoodsList)
        }
    }

//    //Elimina un alimento de la lista de favoritos.
//    fun delFavFood(food: Food) {
//        viewModelScope.launch {
//            _uiState.update {
//                val delFavFoodList = it.favFoodList.toMutableList()
//                delFavFoodList.remove(food)
//                it.copy(
//                    favFoodList = delFavFoodList
//                )
//            }
//            favFoodRepository.deleteFood(food)
//        }
//    }

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
                return FavFoodVM(
                    (application as FoodCode).appContainer.productRepository
                ) as T
            }
        }
    }
}