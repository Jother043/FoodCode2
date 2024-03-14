package com.example.foodcode2.data

import Food
import com.google.gson.annotations.SerializedName

data class FoodsListResponse(
    @SerializedName("meals")
    val meals: List<FoodResponse>
) {
    fun toFood(): Food {
        return meals[0].toFood()
    }

    data class FoodResponse(
        @SerializedName("dateModified")
        val dateModified: Any?, // null
        @SerializedName("idMeal")
        val idMeal: Int, // 53052
        @SerializedName("strArea")
        val strArea: String?, // Malaysian
        @SerializedName("strCategory")
        val strCategory: String?, // Beef
        @SerializedName("strCreativeCommonsConfirmed")
        val strCreativeCommonsConfirmed: Any?, // null
        @SerializedName("strDrinkAlternate")
        val strDrinkAlternate: Any?, // null
        @SerializedName("strImageSource")
        val strImageSource: Any?, // null
        @SerializedName("strIngredient1")
        val strIngredient1: String?, // Minced Beef
        @SerializedName("strIngredient10")
        val strIngredient10: String?,
        @SerializedName("strIngredient11")
        val strIngredient11: String?,
        @SerializedName("strIngredient12")
        val strIngredient12: String?,
        @SerializedName("strIngredient13")
        val strIngredient13: String?,
        @SerializedName("strIngredient14")
        val strIngredient14: String?,
        @SerializedName("strIngredient15")
        val strIngredient15: String?,
        @SerializedName("strIngredient16")
        val strIngredient16: String?,
        @SerializedName("strIngredient17")
        val strIngredient17: String?,
        @SerializedName("strIngredient18")
        val strIngredient18: String?,
        @SerializedName("strIngredient19")
        val strIngredient19: String?,
        @SerializedName("strIngredient2")
        val strIngredient2: String?, // Onion
        @SerializedName("strIngredient20")
        val strIngredient20: String?,
        @SerializedName("strIngredient3")
        val strIngredient3: String?, // Eggs
        @SerializedName("strIngredient4")
        val strIngredient4: String?, // Chilli
        @SerializedName("strIngredient5")
        val strIngredient5: String?, // Baguette
        @SerializedName("strIngredient6")
        val strIngredient6: String?, // Salt
        @SerializedName("strIngredient7")
        val strIngredient7: String?, // Pepper
        @SerializedName("strIngredient8")
        val strIngredient8: String?, // Mayonnaise
        @SerializedName("strIngredient9")
        val strIngredient9: String?,
        @SerializedName("strInstructions")
        val strInstructions: String?, // Mix all the ingredients in a bowl.Heat a pan or griddle with a little vegetable oil.Pour the mixture onto the pan and place a piece of open-faced baguette on top.Press on the bread with a spatula and grill for 2 minutes.Turn the bread over to make it a little crispy.Remove from pan and cut the bread into small portions.Add mayonnaise and/or Sambal before cutting the sandwich (optional).
        @SerializedName("strMeal")
        val strMeal: String?, // Roti john
        @SerializedName("strMealThumb")
        val strMealThumb: String?, // https://www.themealdb.com/images/media/meals/hx335q1619789561.jpg
        @SerializedName("strMeasure1")
        val strMeasure1: String?, // 1/4 lb
        @SerializedName("strMeasure10")
        val strMeasure10: String?,
        @SerializedName("strMeasure11")
        val strMeasure11: String?,
        @SerializedName("strMeasure12")
        val strMeasure12: String?,
        @SerializedName("strMeasure13")
        val strMeasure13: String?,
        @SerializedName("strMeasure14")
        val strMeasure14: String?,
        @SerializedName("strMeasure15")
        val strMeasure15: String?,
        @SerializedName("strMeasure16")
        val strMeasure16: String?,
        @SerializedName("strMeasure17")
        val strMeasure17: String?,
        @SerializedName("strMeasure18")
        val strMeasure18: String?,
        @SerializedName("strMeasure19")
        val strMeasure19: String?,
        @SerializedName("strMeasure2")
        val strMeasure2: String?, // 1
        @SerializedName("strMeasure20")
        val strMeasure20: String?,
        @SerializedName("strMeasure3")
        val strMeasure3: String?, // 3
        @SerializedName("strMeasure4")
        val strMeasure4: String?, // 1 tbs
        @SerializedName("strMeasure5")
        val strMeasure5: String?, // 1/2
        @SerializedName("strMeasure6")
        val strMeasure6: String?, // To taste
        @SerializedName("strMeasure7")
        val strMeasure7: String?, // To taste
        @SerializedName("strMeasure8")
        val strMeasure8: String?, // Top
        @SerializedName("strMeasure9")
        val strMeasure9: String?,
        @SerializedName("strSource")
        val strSource: String?, // https://www.196flavors.com/malaysia-roti-john/
        @SerializedName("strTags")
        val strTags: Any?, // null
        @SerializedName("strYoutube")
        val strYoutube: String? // https://www.youtube.com/watch?v=cl4YH8wblRs
    ) {
        fun toFood(): Food {
            return Food(
                id = idMeal,
                title = strMeal ?: "",
                strDrinkAlternate = strDrinkAlternate?.toString(),
                strCategory = strCategory,
                strArea = strArea,
                instructions = strInstructions ?: "",
                strMealThumb = strMealThumb,
                strTags = strTags?.toString(),
                strYoutube = strYoutube ?: "",
                ingredient1 = strIngredient1 ?: "",
                ingredient2 = strIngredient2 ?: "",
                ingredient3 = strIngredient3 ?: "",
                ingredient4 = strIngredient4 ?: "",
                ingredient5 = strIngredient5 ?: "",
                ingredient6 = strIngredient6 ?: "",
                ingredient7 = strIngredient7 ?: "",
                ingredient8 = strIngredient8 ?: "",
                ingredient9 = strIngredient9 ?: "",
                ingredient10 = strIngredient10 ?: "",
                ingredient11 = strIngredient11 ?: "",
                ingredient12 = strIngredient12 ?: "",
                ingredient13 = strIngredient13 ?: "",
                ingredient14 = strIngredient14 ?: "",
                ingredient15 = strIngredient15 ?: "",
                ingredient16 = strIngredient16 ?: "",
                ingredient17 = strIngredient17 ?: "",
                ingredient18 = strIngredient18 ?: "",
                ingredient19 = strIngredient19 ?: "",
                ingredient20 = strIngredient20 ?: "",
                measure1 = strMeasure1 ?: "",
                measure2 = strMeasure2 ?: "",
                measure3 = strMeasure3 ?: "",
                measure4 = strMeasure4 ?: "",
                measure5 = strMeasure5 ?: "",
                measure6 = strMeasure6 ?: "",
                measure7 = strMeasure7 ?: "",
                measure8 = strMeasure8 ?: "",
                measure9 = strMeasure9 ?: "",
                measure10 = strMeasure10 ?: "",
                measure11 = strMeasure11 ?: "",
                measure12 = strMeasure12 ?: "",
                measure13 = strMeasure13 ?: "",
                measure14 = strMeasure14 ?: "",
                measure15 = strMeasure15 ?: "",
                measure16 = strMeasure16 ?: "",
                measure17 = strMeasure17 ?: "",
                measure18 = strMeasure18 ?: "",
                measure19 = strMeasure19 ?: "",
                measure20 = strMeasure20 ?: "",
                strSource = strSource,
                strImageSource = strImageSource?.toString(),
                strCreativeCommonsConfirmed = strCreativeCommonsConfirmed?.toString(),
                dateModified = dateModified?.toString()
            )
        }
    }
}