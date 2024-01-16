package com.example.FoodCode

class Food {

    var name: String = ""
    var description: String = ""
    var image: Int = 0
    var isFavorite: Boolean = false

    constructor(name: String, description: String, image: Int, isFavorite: Boolean) {
        this.name = name
        this.description = description
        this.image = image
        this.isFavorite = isFavorite
    }
    


}