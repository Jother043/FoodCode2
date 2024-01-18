package com.example.foodcode2

object Datasource {
    fun getSeriesList(): MutableList<Food> {

        val seriesList = mutableListOf<Food>(

            Food(
                "Pizza",
                "Pizza is a savory dish of Italian origin consisting of a usually round, flattened base of leavened wheat-based dough topped with tomatoes, cheese, and often various other ingredients (such as anchovies, mushrooms, onions, olives, pineapple, meat, etc.), which is then baked at a high temperature, traditionally in a wood-fired oven.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Supreme_pizza.jpg/800px-Supreme_pizza.jpg",
                266,
                11,
                false
            ),
            Food(
                "Hamburger",
                "A hamburger (also burger for short) is a sandwich consisting of one or more cooked patties of ground meat, usually beef, placed inside a sliced bread roll or bun. The patty may be pan fried, grilled, smoked or flame broiled.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0b/RedDot_Burger.jpg/800px-RedDot_Burger.jpg",
                295,
                13,
                false
            ),
            Food(
                "Hot Dog",
                "A hot dog (also spelled hotdog) is a food consisting of a grilled or steamed sausage served in the slit of a partially sliced bun. It can also refer to the sausage itself. The sausage used is a wiener (Vienna sausage) or a frankfurter (Frankfurter Würstchen, also just called frank).",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fb/Hotdog_-_Evan_Swigart.jpg/800px-Hotdog_-_Evan_Swigart.jpg",
                151,
                6,
                false
            ),
            Food(
                "Donut",
                "A doughnut or donut (the latter spelling often seen in American English) is a type of fried dough confection or dessert food. The doughnut is popular in many countries and prepared in various forms as a sweet snack that can be homemade or purchased in bakeries, supermarkets, food stalls, and franchised specialty vendors.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/a/a5/Glazed-Donut.jpg/800px-Glazed-Donut.jpg",
                452,
                5,
                false
            ),
            Food(
                "Ice Cream",
                "Ice cream (derived from earlier iced cream or cream ice) is a sweetened frozen food typically eaten as a snack or dessert. It may be made from dairy milk or cream and is flavoured with a sweetener, either sugar or an alternative, and any spice, such as cocoa or vanilla.",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Ice_Cream_dessert_02.jpg/800px-Ice_Cream_dessert_02.jpg",
                207,
                4,
                false
            ),
        )

        seriesList.shuffle()
        return seriesList
    }

    fun getFavSeriesList(): MutableList<Food> {

        val favFoodList = mutableListOf<Food>()
        for (food in getSeriesList()) {
            if (food.fav) {
                favFoodList.add(food)
            }
        }
        return favFoodList
    }
}