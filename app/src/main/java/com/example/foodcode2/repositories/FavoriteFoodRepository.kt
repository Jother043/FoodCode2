package com.example.foodcode2.repositories

import com.example.foodcode2.api.Food
import com.example.foodcode2.db.FoodDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteFoodRepository(
    private val foodDao: FoodDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    //Función para obtener todos los alimentos favoritos.
    suspend fun getAll(): List<Food> = withContext(ioDispatcher) {
        return@withContext foodDao.getAll()
    }

    //Función para obtener un alimento por su id.
    suspend fun isFav(food: Food) = withContext(ioDispatcher) {
        return@withContext foodDao.isFav(food.code)
    }

    //Función para insertar un alimento.
    suspend fun insertFood(food: Food) = withContext(ioDispatcher) {
        return@withContext foodDao.insert(food)
    }

    //Función para eliminar un alimento.
    suspend fun deleteFood(food: Food) = withContext(ioDispatcher) {
        return@withContext foodDao.delete(food)
    }
}