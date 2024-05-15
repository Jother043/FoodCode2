package com.example.foodcode2.ui.settings

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

class UserSettingsVM(
    private val userSettingsRepository: UserRepositories
) : ViewModel() {

    private val _uiState : MutableStateFlow<UserPreferences> = MutableStateFlow(UserPreferences())
    val uiState : StateFlow<UserPreferences> = _uiState.asStateFlow()

    init {
        //al iniciar toma el estado desde el DataStore.
        viewModelScope.launch {
            updateState()
        }
    }

    //actualiza el flujo de estado con los datos de DataStore.
    private suspend fun updateState() {
        userSettingsRepository.getUserPrefs().collect { userPrefFlow ->
            _uiState.update { currentState ->
                userPrefFlow.copy()
            }
        }
    }

    fun saveSettings(name: String, checked : Boolean) {
        Log.d("datastore", "skipWelcome: $checked")
        viewModelScope.launch {
            //edita el DataStore.
            try {
                userSettingsRepository.saveSettings(name,checked, true)
                updateStateSuccess()
            } catch (e: Error) {
                updateStateError()
            }

        }
    }

    private fun updateStateSuccess() {
        _uiState.update { currentState ->
            currentState.copy(
                saveShowViewPage = true
            )
        }
    }

    private  fun updateStateError() {
        _uiState.update { currentState ->
            currentState.copy(
                error = true
            )
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
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return UserSettingsVM(
                    (application as FoodCode).appContainer.userRepositories
                ) as T
            }
        }
    }
}