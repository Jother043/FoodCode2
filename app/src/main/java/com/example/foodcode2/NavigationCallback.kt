package com.example.foodcode2

import Food

interface NavigationCallback {
    fun navigateToDetails(food: Food)
}