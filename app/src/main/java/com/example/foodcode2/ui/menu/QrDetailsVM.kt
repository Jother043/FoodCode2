package com.example.foodcode2.ui.menu

import FoodRepository
import android.util.Log
import androidx.core.app.NotificationCompat.MessagingStyle.Message
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
    val isLoading: Boolean = false,
    val food: Food? = null,
    val error: String = "",
    val addMessage: String = ""
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

    fun setFood(barcode: String) {
        if (barcode.isEmpty()) {
            _uiState.value =
                QrDetailsUiState(error = "No se ha escaneado ningun codigo de barras aun.")
            return
        } else {

            Log.d("FoodListVM", "El codigo escaneado es el siguiente : $barcode")
            _uiState.value = QrDetailsUiState(isLoading = true)
            viewModelScope.launch {
                // Crea una lista con el código de barras escaneado
                val barcodes = listOf(barcode)
                try {
                    val response = foodRepository.getProductByBarcode(barcodes)
                    // Si la respuesta es exitosa, se obtiene el primer elemento de la lista
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
                } catch (e: Exception) {
                    _uiState.value =
                        QrDetailsUiState(error = "Error obteniendo comida por codigo de barras")
                }
            }
        }
    }

    internal fun addFoodToFavorites(food: Food) {
        // Crea un nuevo objeto con los datos del producto
        val foodData = hashMapOf(
            "code" to food.code,
            "title" to food.title,
            "imageUrl" to food.imageUrl,
            "ecoscoreGrade" to food.ecoscoreGrade,
            "energyKcal" to food.energyKcal,
            "nutriments" to food.nutriments,
            "allergens" to food.allergens,
            "brands" to food.brands,
            "categories" to food.categories,
            "traces" to food.traces,
            "packaging" to food.packaging,
            "carbohydrates" to food.carbohydrates,
            "carbohydratesUnit" to food.carbohydratesUnit,
            "energy" to food.energy,
            "fat" to food.fat,
            "fatUnit" to food.fatUnit,
            "proteins" to food.proteins,
            "proteinsUnit" to food.proteinsUnit,
            "salt" to food.salt,
            "saltUnit" to food.saltUnit,
            "saturatedFat" to food.saturatedFat,
            "saturatedFatUnit" to food.saturatedFatUnit,
            "sodium" to food.sodium,
            "sodiumUnit" to food.sodiumUnit,
            "sugars" to food.sugars,
            "sugarsUnit" to food.sugarsUnit,
            "manufacturingPlaces" to food.manufacturingPlaces,
            "ingredientss" to food.ingredientss,
            "image_ingredients_small_url" to food.image_ingredients_small_url,
            "image_nutrition_url" to food.image_nutrition_url,
            "additives_original_tags" to food.additives_original_tags
        )

        // Obtiene el ID del usuario actual
        val userId = firebaseAuth.currentUser?.uid

        // Si el usuario está logueado, añade el producto a la colección de favoritos
        if (!userId.isNullOrEmpty()) {
            //Hacemos un try catch para manejar excepciones
            try {

                db.collection("users").document(userId).collection("favorites")
                    .whereEqualTo("title", food.title)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (documents.isEmpty) {
                            // Si el producto no existe en la colección de favoritos, lo añade
                            db.collection("users").document(userId).collection("favorites")
                                .add(foodData)
                                .addOnSuccessListener {
                                    _uiState.value =
                                        QrDetailsUiState(addMessage = "Producto añadido a favoritos")
                                }
                        } else {
                            _uiState.value =
                                QrDetailsUiState(error = "El producto ya está en favoritos")
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("MenuFragment", "Error checking document", e)
                    }
            } catch (e: Exception) {
                _uiState.value =
                    QrDetailsUiState(error = "Error añadiendo producto a favoritos")
            }
        } else
            Log.w("MenuFragment", "Error: no user is signed in")

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