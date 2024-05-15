package com.example.foodcode2.db

import com.example.foodcode2.api.Food
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodcode2.data.UserComentary


@Database(
    entities = [Food :: class, UserComentary :: class],
    version = 2,
    exportSchema = false)
abstract class FoodDatabase : RoomDatabase() {
    //Dao para los alimentos
    abstract fun foodDao(): FoodDao

    //Dao para los comentarios
    abstract fun comentaryDao(): ComentaryDao

    //Singleton para la base de datos de alimentos.
    companion object {
        @Volatile
        private var Instance : FoodDatabase? = null

        fun getDatabase(context: Context): FoodDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, FoodDatabase::class.java, "food_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}