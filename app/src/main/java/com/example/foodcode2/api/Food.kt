package com.example.foodcode2.api

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
    @PrimaryKey
    @ColumnInfo(name = "code") var code: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "energyKcal") var energyKcal: Double = 0.0,
    @ColumnInfo(name = "ecoscoreGrade") var ecoscoreGrade: String = "",
    @ColumnInfo(name = "imageUrl") var imageUrl: String = "",
    @ColumnInfo(name = "nutriments") var nutriments: String = "",
    @ColumnInfo(name = "allergens") var allergens: String = "",
    @ColumnInfo(name = "brands") var brands: String = "",
    @ColumnInfo(name = "categories") var categories: String = "",
    @ColumnInfo(name = "traces") var traces: String = "",
    @ColumnInfo(name = "packaging") var packaging: String = "",
    @ColumnInfo(name = "carbohydrates") var carbohydrates: Double = 0.0,
    @ColumnInfo(name = "carbohydratesUnit") var carbohydratesUnit: String = "",
    @ColumnInfo(name = "energy") var energy: Double = 0.0,
    @ColumnInfo(name = "fat") var fat: Double = 0.0,
    @ColumnInfo(name = "fatUnit") var fatUnit: String = "",
    @ColumnInfo(name = "proteins") var proteins: Double = 0.0,
    @ColumnInfo(name = "proteinsUnit") var proteinsUnit: String = "",
    @ColumnInfo(name = "salt") var salt: Double = 0.0,
    @ColumnInfo(name = "saltUnit") var saltUnit: String = "",
    @ColumnInfo(name = "saturatedFat") var saturatedFat: Double = 0.0,
    @ColumnInfo(name = "saturatedFatUnit") var saturatedFatUnit: String = "",
    @ColumnInfo(name = "sodium") var sodium: Double = 0.0,
    @ColumnInfo(name = "sodiumUnit") var sodiumUnit: String = "",
    @ColumnInfo(name = "sugars") var sugars: Double = 0.0,
    @ColumnInfo(name = "sugarsUnit") var sugarsUnit: String = "",
    @ColumnInfo(name = "manufacturingPlaces") var manufacturingPlaces: String = "",
    @ColumnInfo(name = "ingredientss") var ingredientss: String? = "",
    @ColumnInfo(name = "image_ingredients_small_url") var image_ingredients_small_url: String = "",
    @ColumnInfo(name = "image_nutrition_url") var image_nutrition_url: String = "",
    @ColumnInfo(name = "additives_original_tags") var additives_original_tags: String = "",
    var isFavorite: Boolean = false,
)

