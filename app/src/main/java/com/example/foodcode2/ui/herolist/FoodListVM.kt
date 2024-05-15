import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.api.Food
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.FavoriteFoodRepository
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class FoodListUiState(
    val foodList: List<Food> = emptyList(),
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null
)

class FoodListVM(
    private val foodRepository: FoodRepository,
    private val favFoodsRepository: FavoriteFoodRepository

) : ViewModel() {

    private val _uiState: MutableStateFlow<FoodListUiState> = MutableStateFlow(FoodListUiState())
    val uiState: StateFlow<FoodListUiState> = _uiState.asStateFlow()


    //Obtiene los alimentos por IDs.
    fun fetchFoodsByIds(ids: List<String>) {
        Log.d("FoodListVM", "El codigo escaneado es el siguiente : $ids")
        _uiState.value = FoodListUiState(isLoading = true)
        viewModelScope.launch {
            val response = foodRepository.getProductByBarcode(ids)
            if (response.isSuccessful) {
                response.body()?.let { foods ->
                    foods.forEach { food ->
                        food.isFavorite = isFav(food)
                    }
                    _uiState.value = FoodListUiState(foodList = foods)
                }
            } else {
                _uiState.value = FoodListUiState(error = "Error obteniendo comidas por ids")
            }
        }
    }

    //Guarda un alimento en la lista de favoritos.
    fun saveToFavorite(food: Food) {

        viewModelScope.launch {
            val isFav = isFav(food)
            if (!isFav) {
                _uiState.update {
                    it.copy(
                        isFavorite = false
                    )
                }
                favFoodsRepository.insertFood(food)
            } else {
                _uiState.update {
                    it.copy(
                        isFavorite = true
                    )
                }
            }
        }
    }

    //Determina si un alimento es favorito.
    suspend fun isFav(food: Food): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext favFoodsRepository.isFav(food) != null
        }

    }

    suspend fun deleteFood(food: Food) {
        favFoodsRepository.deleteFood(food)
    }


    // Este companion object es necesario para poder crear una instancia de este ViewModel
    class FoodListVMFactory(
        private val foodRepository: FoodRepository,
        private val favFoodsRepository: FavoriteFoodRepository
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FoodListVM::class.java)) {
                return FoodListVM(foodRepository, favFoodsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}
