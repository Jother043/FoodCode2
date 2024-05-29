package com.example.foodcode2.ui.userpreferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.example.foodcode2.ui.login.LoginVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InfoUserVM(private val userRepositories: UserRepositories) : ViewModel() {

    private val _userState: MutableStateFlow<UserPreferences> = MutableStateFlow(
        UserPreferences()
    )
    val userState: StateFlow<UserPreferences> = _userState.asStateFlow()

    init {
        viewModelScope.launch {
            updateState()
        }
    }

    private suspend fun updateState() {
        userRepositories.getUserPrefs().collect {
            _userState.value = it
        }
    }

    fun obtenerUsuario() {
        viewModelScope.launch {
            userRepositories.obtenerUsuario()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                // Obtiene la aplicación desde el ViewModelProvider
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])

                return InfoUserVM(
                    (application as FoodCode).appContainer.userRepositories
                ) as T
            }
        }
    }

}