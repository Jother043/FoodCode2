package com.example.foodcode2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodApiConfig {

    companion object {
        //URL base de la API.
        private const val BASE_URL = " https://world.openfoodfacts.org/api/v2/"

        //Definición de la api de Retrofit2.
        fun provideRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

}