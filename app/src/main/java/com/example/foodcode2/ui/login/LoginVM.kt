package com.example.foodcode2.ui.login

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginVM(
    private val userRepositories: UserRepositories
) : ViewModel() {

    // Estado de la UI
    private val _userState: MutableStateFlow<UserPreferences> = MutableStateFlow(
        UserPreferences()
    )
    val userState: StateFlow<UserPreferences> = _userState.asStateFlow()

    // Inicializa Firebase Auth
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()


    init {
        viewModelScope.launch {
            updateState()
        }
    }

    /**
     * Actualiza el estado de la UI
     */
    suspend fun updateState() {
        userRepositories.getUserPrefs().collect { userPrefFlow ->
            _userState.update {
                userPrefFlow.copy()
            }
        }
    }

    // Función para iniciar sesión con Firebase
    fun signInWithFirebase(email: String, password: String) {

        // Restablece el estado de error antes de cada intento de inicio de sesión
        _userState.update {
            it.copy(
                errorMessage = ""
            )
        }

        try {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // El usuario ha iniciado sesión con éxito
                    Log.d("LoginVM", "signInWithFirebase:success")
                    // Actualiza el estado para mostrar que el usuario ha iniciado sesión
                    _userState.update {
                        it.copy(
                            isLoggedIn = true
                        )
                    }

                    // Guarda el estado del usuario en la base de datos firebase

                } else {
                    // Si el inicio de sesión falla, muestra un mensaje de error
                    Log.w("LoginVM", "signInWithFirebase:failure", task.exception)
                    //Actualizamos el estado para mostrar un mensaje de error
                    _userState.update {
                        it.copy(
                            errorMessage = "Error al iniciar sesión"
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("LoginVM", "signInWithFirebase:failure", e)
            _userState.update {
                it.copy(
                    errorMessage = "Error al iniciar sesión"
                )
            }
        }
    }

    private fun checkIfUserIsLoggedIn() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            // El usuario ya ha iniciado sesión
            _userState.update {
                it.copy(
                    isLoggedIn = true
                )
            }
        }
    }

    // Función para validar el email
    fun validateName(email: String): Boolean {

        if (email.isEmpty()) {
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return false
        }
        return true
    }

    // Función para validar la contraseña
    fun validatePassword(password: String): Boolean {
        if (password.isEmpty()) {

            return false
        }
        return true
    }

    // Función para comprobar si hay conexión a internet
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
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



