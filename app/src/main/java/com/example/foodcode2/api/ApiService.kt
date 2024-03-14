package com.example.foodcode2.api

import com.example.foodcode2.data.FoodsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("search.php")
    suspend fun getFoodList(@Query("f") latter : String) : Response<FoodsListResponse>

    @GET("lookup.php")
    suspend fun getFoodDetails(@Query("i") idFood: Int) : Response<FoodsListResponse>

    @GET("random.php")
    suspend fun getFoodRandom() : Response<FoodsListResponse>
}