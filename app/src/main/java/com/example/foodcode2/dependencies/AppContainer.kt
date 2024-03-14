package com.example.foodcode2.dependencies

import FoodRepository
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.api.FoodApiConfig
import com.example.foodcode2.repositories.UserRepositories



class AppContainer(context : Context) {




    //Creación del servicio, usando la api.
    private val FoodApiService = FoodApiConfig.provideRetrofit().create(ApiService::class.java)

    //Creación del repositorio que hará uso de la API.
    val FoodRepository : FoodRepository = FoodRepository(FoodApiService)

}
