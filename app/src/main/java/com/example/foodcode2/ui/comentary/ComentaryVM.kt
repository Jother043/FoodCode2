package com.example.foodcode2.ui.comentary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserComentary
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.ComentaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ComentaryUiState(
    val isLoading: Boolean = true,
    val comentList: List<UserComentary> = emptyList(),
    val autorName: String = "",
    var foodId: String = "",
    val text: String = "",
)

class ComentaryVM(
    private val comentaryRepository: ComentaryRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<ComentaryUiState> = MutableStateFlow(
        ComentaryUiState()
    )
    val uiState: StateFlow<ComentaryUiState> = _uiState.asStateFlow()

    //Obtiene los comentarios de un alimento.
    fun getComentary(idFood: String) {
        viewModelScope.launch {
            val comentResp = comentaryRepository.getFoodComents(idFood)
            _uiState.update { currentSate ->
                currentSate.copy(
                    isLoading = false,
                    comentList = comentResp,
                )
            }
        }
    }

    //Agrega un comentario a un alimento.
    fun setComentary(comentary: UserComentary) {
        viewModelScope.launch {
            comentaryRepository.insertComent(comentary)
            _uiState.update { currentSate ->
                currentSate.copy(
                    autorName = comentary.user,
                    foodId = comentary.foodId,
                    text = comentary.comentary,
                )
            }
            getComentary(comentary.foodId)
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

                return ComentaryVM(
                    (application as FoodCode).appContainer.comentaryRepository
                ) as T
            }
        }
    }
}
