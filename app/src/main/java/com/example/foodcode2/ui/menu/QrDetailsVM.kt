package com.example.foodcode2.ui.menu

import FoodRepository
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodcode2.api.Food
import com.example.foodcode2.dependencies.FoodCode
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class QrDetailsUiState(
    val isLoading: Boolean = false, val food: Food? = null, val error: String = ""
)

class QrDetailsVM(
    private val foodRepository: FoodRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<QrDetailsUiState> = MutableStateFlow(
        QrDetailsUiState()
    )
    val uiState: StateFlow<QrDetailsUiState> = _uiState

    private val db = FirebaseFirestore.getInstance() //TODO: Implementar Firestore
    private val firebaseAuth = FirebaseAuth.getInstance()

    val _barcodeResult = MutableStateFlow<String?>(null)
    val barcodeResult: StateFlow<String?> = _barcodeResult
    fun setFood(barcode: String) {
        if (barcode.isEmpty()) {
            _uiState.value =
                QrDetailsUiState(error = "No se ha escaneado ningun codigo de barras aun.")
            return
        } else {

            Log.d("FoodListVM", "El codigo escaneado es el siguiente : $barcode")
            _uiState.value = QrDetailsUiState(isLoading = true)
            viewModelScope.launch {
                // Create a list with a single barcode
                val barcodes = listOf(barcode)
                val response = foodRepository.getProductByBarcode(barcodes)
                if (response.isSuccessful) {
                    response.body()?.let { foodResponse ->
                        // Convierte la respuesta del API a un objeto Food de foodsListResponse
                        foodResponse.forEach { food ->
                            food.isFavorite = false
                        }
                        _uiState.value = QrDetailsUiState(food = foodResponse.first())
                    }
                } else {
                    if (response.code() == 502) {
                        _uiState.value =
                            QrDetailsUiState(error = "La API no responde, intente mas tarde")
                    } else {
                        _uiState.value =
                            QrDetailsUiState(error = "Error obteniendo comida por codigo de barras")
                    }
                }
            }
        }
    }

    internal fun addFoodToFavorites(food: Food) {
        // Crea un nuevo objeto con los datos del producto
        val foodData = hashMapOf(
            "title" to food.title,
            "imageUrl" to food.imageUrl,
            "ecoscoreGrade" to food.ecoscoreGrade,
            "energyKcal" to food.energyKcal
        )

        // Obtiene el ID del usuario actual
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null && userId.isNotEmpty()) {
            // Comprueba si el producto ya existe en la colección de favoritos
            db.collection("users").document(userId).collection("favorites")
                .whereEqualTo("title", food.title)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        // Si el producto no existe en la colección de favoritos, lo añade
                        db.collection("users").document(userId).collection("favorites")
                            .add(foodData)
                            .addOnSuccessListener { documentReference ->
                                _uiState.value = QrDetailsUiState(error = "Producto añadido a favoritos")
                            }
                            .addOnFailureListener { e ->
                                Log.w("MenuFragment", "Error adding document", e)
                            }
                    } else {
                        _uiState.value = QrDetailsUiState(error = "El producto ya está en favoritos")
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("MenuFragment", "Error checking document", e)
                }
        } else {
            Log.w("MenuFragment", "Error: no user is signed in")
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return QrDetailsVM(
                    (application as FoodCode).appContainer.FoodRepository
                ) as T
            }
        }
    }
}