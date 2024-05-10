package com.example.foodcode2.ui.SingUp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SingUpVM(
    private val userRepositories: UserRepositories
) : ViewModel() {

    private val _userStateSingUp: MutableStateFlow<UserPreferences> = MutableStateFlow(
        UserPreferences()
    )
    val userStateSingUp: StateFlow<UserPreferences> = _userStateSingUp.asStateFlow()

    private val dataBase: FirebaseDatabase = FirebaseDatabase.getInstance()

    private val firebaseAuth = FirebaseAuth.getInstance()

    //Este init se encarga de actualizar el estado de la UI.
    init {
        viewModelScope.launch {
            updateState()
        }
    }

    /**
     * Esta funcion se encarga de actualizar el estado de la UI.
     *
     */
    private suspend fun updateState() {
        userRepositories.getUserPrefs().collect { userPrefFlow ->
            _userStateSingUp.update {
                userPrefFlow.copy()
            }
        }
    }

    fun signUpUser(name: String, email: String, password: String) {

        // Restablece el estado de error antes de cada intento de registro
        _userStateSingUp.update {
            it.copy(
                errorMessage = ""
            )
        }

        try {
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        _userStateSingUp.update { UserPreferences(isRegistered = true) }
                        // El usuario se ha registrado con éxito, ahora guardamos sus detalles en la base de datos
                        saveUserToDatabase(name, email)
                    } else {
                        // Algo salió mal
                        _userStateSingUp.update { UserPreferences(errorMessage = "Error al registrar el usuario") }
                    }
                }.addOnFailureListener { exception ->
                    if (exception is java.net.UnknownHostException) {
                        // Error de red
                        _userStateSingUp.update { UserPreferences(errorMessage = "Error de red. Por favor, verifica tu conexión a Internet.") }
                    }
                    //Si el correo ya está registrado en la base de datos de Firebase se muestra un mensaje de error
                    if (exception.message == "The email address is already in use by another account.") {
                        _userStateSingUp.update { UserPreferences(errorMessage = "El correo ya está registrado") }
                    }
                }

        } catch (e: Exception) {
            _userStateSingUp.update { UserPreferences(errorMessage = "Error al registrar el usuario") }
        }
    }

    /**
     * Guarda los detalles del usuario en la base de datos de Firebase.
     */
    private fun saveUserToDatabase(name: String, email: String) {
        val uid = firebaseAuth.currentUser?.uid ?: return
        val userPreferences = UserPreferences(
            name,
            email,
            showViewPage = true,
            saveShowViewPage = true,
            isLoggedIn = false
        )
        try {
            dataBase.getReference("Users").child(uid).setValue(userPreferences)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Los detalles del usuario se han guardado con éxito
                        _userStateSingUp.update { userPreferences.copy(isLoggedIn = true) } // Actualiza isLoggedIn a true
                    } else {
                        // Algo salió mal
                        _userStateSingUp.update { UserPreferences(errorMessage = "Error al guardar los detalles del usuario") }
                    }
                }.addOnFailureListener { exception ->
                    if (exception is java.net.UnknownHostException) {
                        // Error de red
                        _userStateSingUp.update { UserPreferences(errorMessage = "Error de red. Por favor, verifica tu conexión a Internet.") }
                    }
                }
        } catch (e: Exception) {
            _userStateSingUp.update { UserPreferences(errorMessage = "Error al guardar los detalles del usuario") }
        }
    }

    /**
     * Comprueba si hay conexión a internet.
     */
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

                return SingUpVM(
                    (application as FoodCode).appContainer.userRepositories
                ) as T
            }
        }
    }

}
