package com.example.foodcode2.repositories

import android.util.Log
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

    suspend fun getFullFood(barcode: String): Food? {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            Log.d("El usuario es: ", userId)
            val snapshot = db.collection("users").document(userId).collection("favorites").whereEqualTo("code", barcode).get().await()
            for (document in snapshot.documents) {
                val food = document.toObject(Food::class.java)
                if (food != null) {
                    return food
                }
            }
        }
        return null
    }

    suspend fun deleteFavorite(food: Food) {
        val userId = firebaseAuth.currentUser?.uid
        if (userId != null) {
            db.collection("users").document(userId).collection("favorites").whereEqualTo("title", food.title).get().await().documents.forEach {
                it.reference.delete()
            }
        }
    }

}