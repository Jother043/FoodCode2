package com.example.foodcode2.dependencies

import FoodRepository
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.api.FoodApiConfig
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.db.FoodDatabase
import com.example.foodcode2.repositories.ComentaryRepository
import com.example.foodcode2.repositories.FavoriteFoodRepository
import com.example.foodcode2.repositories.ProductRepository
import com.example.foodcode2.repositories.UserRepositories
import com.google.firebase.database.FirebaseDatabase

val Context.userDataStore by preferencesDataStore(name = UserPreferences.SETTINGS_FILE)

class AppContainer(context : Context) {


    //Creación del servicio, usando la api.
    private val FoodApiService = FoodApiConfig.provideRetrofit().create(ApiService::class.java)

    //Creación del repositorio que hará uso de la API.
    val FoodRepository : FoodRepository = FoodRepository(FoodApiService)

    //Repositorio de configuración de usuario.
    private val _userRepositories: UserRepositories by lazy {
        UserRepositories(context.userDataStore, FirebaseDatabase.getInstance())
    }
    val userRepositories get() = _userRepositories

    //Repositorio de Recetas Favoritas
    private val _favoriteFoodRepository : FavoriteFoodRepository by lazy {
        FavoriteFoodRepository(FoodDatabase.getDatabase(context).foodDao())
    }
    val favoriteFoodRepository get() = _favoriteFoodRepository

    //Repositorio de Comentarios
    private val _comentaryRepository : ComentaryRepository by lazy {
        ComentaryRepository(FoodDatabase.getDatabase(context).comentaryDao())
    }
    val comentaryRepository get() = _comentaryRepository

    private val _productRepository : ProductRepository by lazy {
        ProductRepository()
    }
    val productRepository get() = _productRepository



}
