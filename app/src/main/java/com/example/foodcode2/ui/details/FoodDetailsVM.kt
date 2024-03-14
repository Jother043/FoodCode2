import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.dependencies.FoodCode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FoodDetailsUiState(
    val food: Food? = null,
    val isLoading: Boolean = false,
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

    fun setFood(id: Int) {
        viewModelScope.launch {
            val foodResp = foodRepository.getFoodDetailById(id)
            if (foodResp.isSuccessful) {
                val foodListResponse = foodResp.body()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        food = foodListResponse
                    )
                }
            } else {
                //Escribimos en el log el error.
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return FoodDetailsVM(
                    (application as FoodCode).appContainer.FoodRepository
                ) as T
            }
        }
    }
}
