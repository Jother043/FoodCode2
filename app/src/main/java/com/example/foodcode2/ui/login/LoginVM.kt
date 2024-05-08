package com.example.foodcode2.ui.login

import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val errorMessage: String = ""
)

class LoginVM(
    private val userRepositories: UserRepositories
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserPreferences> = MutableStateFlow(
        UserPreferences()
    )
    val uiState: StateFlow<UserPreferences> = _uiState.asStateFlow()


    private val _uiStateLogin: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiStateLogin: StateFlow<LoginUiState> = _uiStateLogin.asStateFlow()

    // Inicializa Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _loginUiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())

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

    // Función para iniciar sesión con Firebase
    // Función para iniciar sesión con Firebase
    fun signInWithFirebase(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val user = firebaseAuth.currentUser
                if(user?.isEmailVerified == true) {
                    _loginUiState.value = LoginUiState(isLoggedIn = true)
                    _uiState.update { UserPreferences(isLoggedIn = true) } // Actualiza el estado de la UI
                } else {
                    _loginUiState.value = LoginUiState(errorMessage = "Verifica tu correo electrónico")
                    _uiState.update { UserPreferences(errorMessage = "Verifica tu correo electrónico") } // Actualiza el estado de la UI
                }
            } else {
                // Si el inicio de sesión falla, muestra un mensaje al usuario.
                _loginUiState.value = LoginUiState(errorMessage = "Error al iniciar sesión")
                _uiState.update { UserPreferences(errorMessage = "Error al iniciar sesión") } // Actualiza el estado de la UI
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

                return LoginVM(
                    (application as FoodCode).appContainer.userRepositories
                ) as T
            }
        }
    }


}