package com.example.foodcode2.data

data class UserComentary(
    var id: String = "",
    var user: String = "",
    var foodId: String = "",
    var comentary: String = ""
) {
    companion object {
        const val FOOD_ID = "foodId"
        const val COMENTARY = "comentary"
        const val USER = "user"
    }
}