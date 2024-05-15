package com.example.foodcode2.api

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foodcode2.data.FoodsListResponse

@Entity
data class Food(
    @PrimaryKey
    @ColumnInfo(name = "code") var code: String = "",
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "energyKcal") var energyKcal: Double = 0.0,
    @ColumnInfo(name = "ecoscoreGrade") var ecoscoreGrade: String = "",
    @ColumnInfo(name = "imageUrl") var imageUrl: String = "",
    var isFavorite: Boolean = false
)

