import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.dependencies.FoodCode

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FoodListUiState(
    val foodList: List<Food> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FoodListVM(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<FoodListUiState> = MutableStateFlow(FoodListUiState())
    val uiState: StateFlow<FoodListUiState> = _uiState.asStateFlow()

    init {
        val ids = listOf(52771, 52772, 52773, 52774, 52775, 52776, 52777) // Reemplaza esto con los IDs que quieras obtener
        fetchFoodsByIds(ids)
    }

    fun fetchFoodsByIds(ids: List<Int>) {
        _uiState.value = FoodListUiState(isLoading = true)
        viewModelScope.launch {
            val response = foodRepository.getFoodsByIds(ids)
            if (response.isSuccessful) {
                response.body()?.let { foods ->
                    _uiState.value = FoodListUiState(foodList = foods)
                }
            } else {
                _uiState.value = FoodListUiState(error = "Error fetching foods")
            }
        }
    }

    companion object {

        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create
                        (modelClass: Class<T>,
                         extras: CreationExtras)
            : T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return FoodListVM(
                    (application as FoodCode).appContainer.FoodRepository
                ) as T
            }
        }
    }


}
