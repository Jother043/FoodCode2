package com.example.foodcode2

data class Food(
    val name : String = "",
    val description : String = "",
    val photo : String = "",
    val calories : Int = 0,
    val proteins : Int = 0,
    val fav : Boolean = false
) {
}

