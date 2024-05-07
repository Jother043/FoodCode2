package com.example.foodcode2.db

import com.example.foodcode2.api.Food
import androidx.room.*

@Dao
interface FoodDao {
    //Obtener todos los alimentos
    @Query("SELECT * FROM food")
    fun getAll(): List<Food>

    //Obtener un alimento por su id
    @Query("SELECT * FROM food WHERE code IN (:foodIds)")
    fun loadAllByIds(foodIds: IntArray): List<Food>

    //Obtener un alimento por su nombre
    @Query("SELECT * FROM food WHERE title LIKE :name LIMIT 1")
    fun findByName(name: String): Food

    //Obtener un alimento por su id
    @Query("SELECT * FROM food WHERE code LIKE :id")
    fun isFav(id: String): Food

    //Insertar un alimento
    @Insert
    fun insert(food: Food)

    //Actualizar un alimento
    @Delete
    fun delete(food: Food)
}