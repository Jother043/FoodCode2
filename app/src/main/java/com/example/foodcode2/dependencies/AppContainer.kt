package com.example.foodcode2.dependencies

import FoodRepository
import android.content.Context
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.api.FoodApiConfig


class AppContainer(context : Context) {

    //Creación del servicio, usando la api.
    private val FoodApiService = FoodApiConfig.provideRetrofit().create(ApiService::class.java)

    //Creación del repositorio que hará uso de la API.
    val FoodRepository : FoodRepository = FoodRepository(FoodApiService)

}
