package com.example.foodcode2.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val userRepositories: UserRepositories
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserPreferences> = MutableStateFlow(
        UserPreferences()
    )
    val uiState: StateFlow<UserPreferences> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            updateState()
        }
    }

    private suspend fun updateState() {
        userRepositories.getUserPrefs().collect { userPrefFlow ->
            _uiState.update {
                userPrefFlow.copy()
            }
        }
    }

    /**
     * Función que guarda el nombre y el estado del checkbox en el DataStore.
     * @param name Nombre del usuario.
     * @param checked Estado del checkbox.
     */
    fun saveSettings(name: String, checked: Boolean) {
        Log.d("Guardando", "nombre: $name, checked: $checked")
        viewModelScope.launch {
            userRepositories.saveName(name)
            updateState()
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

                return LoginVM(
                    (application as FoodCode).appContainer.userRepositories
                ) as T
            }
        }
    }
}