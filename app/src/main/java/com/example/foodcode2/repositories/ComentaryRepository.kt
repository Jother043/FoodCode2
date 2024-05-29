package com.example.foodcode2.repositories

import com.example.foodcode2.api.Food
import com.example.foodcode2.data.UserComentary
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ComentaryRepository() {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    suspend fun insertComent(coment: UserComentary) {
        val userId = firebaseAuth.currentUser?.uid

        if (userId != null) {
            firestore.collection("coments").document(coment.foodId).collection("coments")
                .add(coment).await()
        }
    }

    suspend fun getFoodComents(foodId: String): List<UserComentary> {
        val userId = firebaseAuth.currentUser?.uid
        val coments = mutableListOf<UserComentary>()

        if (userId != null) {
            val snapshot =
                firestore.collection("coments").document(foodId).collection("coments").get().await()
            for (document in snapshot.documents) {
                val com = document.toObject(UserComentary::class.java)
                if (com != null) {
                    coments.add(com)
                }
            }
        }

        return coments
    }

}