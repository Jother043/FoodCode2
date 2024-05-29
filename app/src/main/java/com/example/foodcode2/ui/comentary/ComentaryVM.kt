package com.example.foodcode2.ui.comentary

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.data.UserComentary
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.ComentaryRepository
import com.example.foodcode2.repositories.UserRepositories
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class ComentaryUiState(
    val isLoading: Boolean = true,
    val comentList: List<UserComentary> = emptyList(),
    val autorName: String = "",
    var foodId: String = "",
    val text: String = "",
)

class ComentaryVM(
    private val comentaryRepository: ComentaryRepository,

) : ViewModel() {

    private val _uiState: MutableStateFlow<ComentaryUiState> = MutableStateFlow(
        ComentaryUiState()
    )
    val uiState: StateFlow<ComentaryUiState> = _uiState.asStateFlow()


    val auth = FirebaseAuth.getInstance()
    val data = Firebase.database

    //Obtiene los comentarios de un alimento.
    fun getComentary(idFood: String) {
        viewModelScope.launch {
            val coments = comentaryRepository.getFoodComents(idFood)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    comentList = coments,
                    foodId = idFood,
                )
            }
        }
    }

    //Agrega un comentario a un alimento.
    fun setComentary(comentary: UserComentary) {
        viewModelScope.launch {
            comentaryRepository.insertComent(comentary)
            getComentary(comentary.foodId)
        }
    }

    suspend fun obtenerUsuario(): String? {
        val user = auth.currentUser
        val uid = user?.uid
        var userName: String? = null
        if (user != null) {
            if (user.displayName != null && user.displayName!!.isNotBlank()) {
                userName = user.displayName.toString()
            } else {
                userName = suspendCoroutine { continuation ->
                    data.reference.child("Users").child(uid!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val name = snapshot.child("name").value.toString()
                                continuation.resume(name)
                            }

                            @SuppressLint("RestrictedApi")
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Error", error.message)
                                continuation.resumeWithException(DatabaseException(error.message))
                            }
                        })
                }
            }
        }
        return userName
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
