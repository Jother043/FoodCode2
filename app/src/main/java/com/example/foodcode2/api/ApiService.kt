package com.example.foodcode2.api

import com.example.foodcode2.data.FoodsListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Obtener la información de un producto a partir de su código de barras escaneado.
    @GET("product/{barcode}?fields=product_name,nutriscore_data,nutriments,nutrition_grades,image_front_url,allergens,brands,categories,traces,packaging,manufacturing_places" +
            ",additives_original_tags,image_front_small_url,image_ingredients_small_url,image_nutrition_url,ingredients")
       suspend fun getProduct(@Path("barcode") barcode: String): Response<FoodsListResponse>

}