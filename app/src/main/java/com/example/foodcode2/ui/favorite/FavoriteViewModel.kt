package com.example.foodcode2.ui.favorite

import FoodEntity
import FoodRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    //private val repository = FoodRepository()
    private val _foodList: MutableLiveData<List<FoodEntity>> = MutableLiveData()
    val foodList: LiveData<List<FoodEntity>>
        get() = _foodList

    fun getFavoritesFoodList() = viewModelScope.launch {
        //_foodList.value = repository.getDbFoodList()
    }
}