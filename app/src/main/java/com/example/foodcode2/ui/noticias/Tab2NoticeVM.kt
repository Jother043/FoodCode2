package com.example.foodcode2.ui.noticias

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

class Tab2NoticeVM (private val userRepository: UserRepositories
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserPreferences> = MutableStateFlow(
        UserPreferences()
    )
    val uiState: StateFlow<UserPreferences> = _uiState.asStateFlow()


    private suspend fun updateStateSuccess() {
        _uiState.update { currentState ->
            currentState.copy(
                saveShowViewPage = true
            )
        }
    }

    private suspend fun updateStateError() {
        _uiState.update { currentState ->
            currentState.copy(
                error = true
            )
        }
    }

    fun saveSettingsWelcome(checked: Boolean) {
        Log.d("datastore", "skipWelcome: $checked")
        viewModelScope.launch {
            //edita el DataStore.
            try {
                userRepository.saveSettingsViewPage(checked)
                updateStateSuccess()
            } catch (e: Error) {
                updateStateError()
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

                return Tab2NoticeVM(
                    (application as FoodCode).appContainer.userRepositories
                ) as T
            }
        }
    }
}