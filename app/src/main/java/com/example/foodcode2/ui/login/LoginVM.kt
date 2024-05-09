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
import com.google.firebase.database.FirebaseDatabase
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

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()

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
                if (user?.isEmailVerified == true) {
                    // Obtiene los datos del usuario desde Firebase Realtime Database
                    database.getReference("users").child(user.uid).get().addOnSuccessListener {
                        val userData = it.getValue(UserPreferences::class.java)
                        if (userData != null) {
                            _uiState.update { userData } // Actualiza el estado de la UI con los datos del usuario obtenidos
                        } else {
                            _uiState.update { UserPreferences(errorMessage = "No se encontraron los datos del usuario") }
                        }
                    }.addOnFailureListener {
                        _uiState.update { UserPreferences(errorMessage = "Error al obtener los datos del usuario") }
                    }
                } else {
                    _uiState.update { UserPreferences(errorMessage = "Por favor verifica tu correo electrónico") }
                }
            } else {
                _uiState.update { UserPreferences(errorMessage = "Error al iniciar sesión") }
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