package com.example.foodcode2.dependencies
import android.app.Application

class FoodCode : Application() {

    private lateinit var _appContainer : AppContainer
    val appContainer get() = _appContainer

    override fun onCreate() {
        super.onCreate()
        _appContainer = AppContainer(this)
    }
}