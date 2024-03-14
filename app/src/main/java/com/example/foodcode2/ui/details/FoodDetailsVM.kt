import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FoodDetailsUiState(
    val food: Food? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class FoodDetailsVM(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<FoodDetailsUiState> = MutableStateFlow(
        FoodDetailsUiState()
    )
    val uiState: StateFlow<FoodDetailsUiState> = _uiState.asStateFlow()

    init {

    }

    fun fetchFoodsByIds(ids: List<Int>) {
        _uiState.value = FoodDetailsUiState(isLoading = true)
        viewModelScope.launch {
            val response = foodRepository.getFoodsByIds(ids)
            if (response.isSuccessful) {
            } else {
                Log.d("FoodDetailsVM", "Error fetching foods")
            }
        }
    }

    fun setFood(idFood: String) {
        viewModelScope.launch {
            val foodResp = foodRepository.getFullFood(idFood.toInt())
            val foodListResponse = foodResp.body()
            val food = foodListResponse?.meals?.firstOrNull()?.toFood()
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                food = food
            )
        }
    }
}
