package com.example.foodcode2.repositories

import com.example.foodcode2.api.Food
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private val db = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    //Obtener los productos añadidos a favoritos.
    suspend fun getFavorites(): List<Food> {

        val userId = firebaseAuth.currentUser?.uid
        val favorites = mutableListOf<Food>()

        if (userId != null) {
            val snapshot = db.collection("users").document(userId).collection("favorites").get().await()
            for (document in snapshot.documents) {
                val food = document.toObject(Food::class.java)
                if (food != null) {
                    favorites.add(food)
                }
            }
        }

        return favorites
    }

    //Añadir un producto a favoritos.
    suspend fun addFavorite(food: Food) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("favorites").document(food.code).set(food).await()
        }
    }


}