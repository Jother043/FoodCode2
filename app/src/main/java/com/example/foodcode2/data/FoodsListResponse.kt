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
    val statusVerbose: String?
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
        val nutritionGrades: String?
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
        )
    }
}

