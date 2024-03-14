package com.example.foodcode2.db

import FoodEntity
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [FoodEntity::class], version = 1, exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao

}