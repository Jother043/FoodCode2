package com.example.foodcode2.data

import com.example.foodcode2.api.Food
import com.google.gson.annotations.SerializedName

data class FoodsListResponse(
    @SerializedName("code")
    val code: String?,
    @SerializedName("product")
    val product: Product?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("status_verbose")
    val statusVerbose: String?,
    @SerializedName("nutriments")
    val nutriments: Nutriments?,
    @SerializedName("allergens")
    val allergens: String?,
    @SerializedName("brands")
    val brands: String?,
    @SerializedName("categories")
    val categories: String?,
    @SerializedName("traces")
    val traces: String?,
    @SerializedName("packaging")
    val packaging: String?,
    @SerializedName("manufacturing_places")
    val manufacturingPlaces: String?,
    @SerializedName("ingredients")
    val ingredients: ingredientss?,
    @SerializedName("image_ingredients_small_url")
    val image_ingredients_small_url: String?,
    @SerializedName("image_nutrition_url")
    val image_nutrition_url: String?,
    @SerializedName("additives_original_tags")
    val additives_original_tags: additives?,
) {
    data class Product(
        @SerializedName("product_name")
        val productName: String?,
        @SerializedName("image_front_url")
        val imageFrontUrl: String?,
        @SerializedName("nutriments")
        val nutriments: Nutriments?,
        @SerializedName("nutriscore_data")
        val nutriscoreData: NutriscoreData?,
        @SerializedName("nutrition_grades")
        val nutritionGrades: String?,
        @SerializedName("allergens")
        val allergens: String?,
        @SerializedName("brands")
        val brands: String?,
        @SerializedName("categories")
        val categories: String?,
        @SerializedName("traces")
        val traces: String?,
        @SerializedName("packaging")
        val packaging: String?,
        @SerializedName("manufacturing_places")
        val manufacturingPlaces: String?,
        @SerializedName("image_ingredients_small_url")
        val image_ingredients_small_url: String?,
        @SerializedName("image_nutrition_url")
        val image_nutrition_url: String?,

    )

    data class additives(
        @SerializedName("0")
        val additive0: String?,
    )

    data class ingredientss(
        @SerializedName("vegan")
        val vegan: String?,
        @SerializedName("vegetarian")
        val vegetarian: String?,
    )

    data class Nutriments(
        @SerializedName("carbohydrates")
        val carbohydrates: Double?,
        @SerializedName("carbohydrates_unit")
        val carbohydratesUnit: String?,
        @SerializedName("energy")
        val energy: Double?,
        @SerializedName("energy-kcal")
        val energyKcal: Double?,
        @SerializedName("energy-kcal_unit")
        val energyKcalUnit: String?,
        @SerializedName("fat")
        val fat: Double?,
        @SerializedName("fat_unit")
        val fatUnit: String?,
        @SerializedName("proteins")
        val proteins: Double?,
        @SerializedName("proteins_unit")
        val proteinsUnit: String?,
        @SerializedName("salt")
        val salt: Double?,
        @SerializedName("salt_unit")
        val saltUnit: String?,
        @SerializedName("saturated-fat")
        val saturatedFat: Double?,
        @SerializedName("saturated-fat_unit")
        val saturatedFatUnit: String?,
        @SerializedName("sodium")
        val sodium: Double?,
        @SerializedName("sodium_unit")
        val sodiumUnit: String?,
        @SerializedName("sugars")
        val sugars: Double?,
        @SerializedName("sugars_unit")
        val sugarsUnit: String?
    )

    data class NutriscoreData(
        @SerializedName("nutriscore_not_applicable_for_category")
        val nutriscoreNotApplicableForCategory: String?
    )

    // Método para convertir la respuesta de la comida a un objeto Food
    fun toFood(): Food {
    return Food(
        code = code ?: "",
        title = product?.productName ?: "",
        energyKcal = product?.nutriments?.energyKcal ?: 0.0,
        ecoscoreGrade = product?.nutritionGrades ?: "",
        imageUrl = product?.imageFrontUrl ?: "",
        nutriments = product?.nutriments.toString(),
        allergens = product?.allergens ?: "",
        brands = product?.brands ?: "",
        categories = product?.categories ?: "",
        traces = product?.traces ?: "",
        packaging = product?.packaging ?: "",
        carbohydrates = product?.nutriments?.carbohydrates ?: 0.0,
        carbohydratesUnit = product?.nutriments?.carbohydratesUnit ?: "",
        energy = product?.nutriments?.energy ?: 0.0,
        fat = product?.nutriments?.fat ?: 0.0,
        fatUnit = product?.nutriments?.fatUnit ?: "",
        proteins = product?.nutriments?.proteins ?: 0.0,
        proteinsUnit = product?.nutriments?.proteinsUnit ?: "",
        salt = product?.nutriments?.salt ?: 0.0,
        saltUnit = product?.nutriments?.saltUnit ?: "",
        saturatedFat = product?.nutriments?.saturatedFat ?: 0.0,
        saturatedFatUnit = product?.nutriments?.saturatedFatUnit ?: "",
        sodium = product?.nutriments?.sodium ?: 0.0,
        sodiumUnit = product?.nutriments?.sodiumUnit ?: "",
        sugars = product?.nutriments?.sugars ?: 0.0,
        sugarsUnit = product?.nutriments?.sugarsUnit ?: "",
        manufacturingPlaces = product?.manufacturingPlaces ?: "",
        ingredientss = ingredients?.toString(),
        image_ingredients_small_url = product?.image_ingredients_small_url ?: "",
        image_nutrition_url = product?.image_nutrition_url ?: "",
        additives_original_tags = additives_original_tags?.additive0 ?: "",

    )
}
}

