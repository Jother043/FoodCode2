package com.example.foodcode2.ui.login

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.R
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    val user = firebaseAuth.currentUser
                    if (task.isSuccessful && user != null) {
                        if (user.isEmailVerified) {
                            // El usuario ha iniciado sesión con éxito
                            Log.d("LoginVM", "signInWithFirebase:success")
                            // Actualiza el estado para mostrar que el usuario ha iniciado sesión
                            _userState.update {
                                it.copy(
                                    isLoggedIn = true
                                )
                            }
                        } else {
                            _userState.update {
                                it.copy(
                                    errorMessage = "Por favor, verifica tu correo electrónico"
                                )
                            }
                        }
                    } else {
                        // Si el inicio de sesión falla, muestra un mensaje de error
                        Log.w("LoginVM", "signInWithFirebase:failure", task.exception)
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


    // Función para validar el email
    fun validateName(email: String): Boolean {

        _userState.update {
            it.copy(
                errorMessage = ""
            )
        }

        // Si el email está vacío o no es un email válido, retorna false
        if (email.isEmpty() && email.isBlank()) {
            _userState.update {
                it.copy(
                    errorMessage = "El email no puede estar vacío"
                )
            }
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _userState.update {
                it.copy(
                    errorMessage = "El email no es válido"
                )
            }
            return false
        }
        return true
    }

    // Función para validar la contraseña
    fun validatePassword(password: String): Boolean {

        // Si la contraseña está vacía, retorna false
        if (password.isEmpty()) {
            _userState.update {
                it.copy(
                    errorMessage = "La contraseña no puede estar vacía"
                )
            }
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

    fun recuperarContrasena(email: String) {
        //Código para recuperar la contraseña
        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _userState.update {
                        it.copy(
                            isSucefullMessage = "Se ha enviado un correo para restablecer la contraseña"
                        )
                    }
                } else {
                    _userState.update {
                        it.copy(
                            errorMessage = "Error al enviar el correo"
                        )
                    }
                }
            }
    }

    fun obtenerUsuario() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            userRepositories.obtenerUsuario()
        }
    }

    fun anonymous() {

        _userState.update {
            it.copy(
                errorMessage = ""
            )
        }

        firebaseAuth.signInAnonymously().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // El usuario ha iniciado sesión con éxito
                Log.d("LoginVM", "signInWithFirebase:success")
                // Actualiza el estado para mostrar que el usuario ha iniciado sesión
                _userState.update {
                    it.copy(
                        anonymous = true
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



