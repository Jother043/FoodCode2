package com.example.foodcode2.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userComentary")
data class UserComentary(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "user")
    var user: String,
    @ColumnInfo(name = "foodId")
    var foodId: String,
    @ColumnInfo(name = "comentary")
    var comentary: String
) {
    companion object {
        const val FOOD_ID = "foodId"
        const val COMENTARY = "comentary"
        const val USER = "user"
    }
}
