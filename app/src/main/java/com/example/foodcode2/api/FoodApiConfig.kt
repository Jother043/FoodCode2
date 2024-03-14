package com.example.foodcode2.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FoodApiConfig {
    companion object {


        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

        //Definición de la api de Retrofit2.
        fun provideRetrofit() : Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}