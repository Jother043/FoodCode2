package com.example.foodcode2.repositories

import com.example.foodcode2.db.FoodDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class FavoriteFoodRepository(
    private val foodDao: FoodDao,
    private val ioDispatcher : CoroutineDispatcher = Dispatchers.IO
) {

}