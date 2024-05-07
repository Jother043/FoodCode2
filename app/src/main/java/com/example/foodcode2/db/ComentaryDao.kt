package com.example.foodcode2.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodcode2.data.UserComentary

@Dao
interface ComentaryDao {

    //Obtener todos los comentarios
    @Query("SELECT * FROM userComentary")
    fun getAll(): List<UserComentary>

    //Insertar un comentario
    @Insert
    fun insert(coment: UserComentary)

    //Obtener los comentarios de un alimento
    @Query("SELECT * FROM UserComentary WHERE foodId = :id")
    fun getFoodComents(id: String): List<UserComentary>
}