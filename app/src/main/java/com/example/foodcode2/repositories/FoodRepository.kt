import android.util.Log
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.data.FoodsListResponse
import kotlinx.coroutines.withTimeout
import retrofit2.Response
import kotlin.random.Random

class FoodRepository(
    val foodApiService: ApiService
) {
    companion object {
        const val FOOD_HEROES = 52772
    }

    suspend fun getFullFood(id: Int): Response<FoodsListResponse> {
        return foodApiService.getFoodDetails(id)
    }

    private val usedRandomNumbers = mutableSetOf<Int>()

    suspend fun getRandFullFood(): Response<FoodsListResponse> {
        return withTimeout(5000) { // Timeout after 5000 milliseconds (5 seconds)
            var x: Int
            do {
                x = (52771..FOOD_HEROES).random()
                Log.d("FoodRepository", "Generated random number: $x")
            } while (!usedRandomNumbers.add(x))
            Log.d("FoodRepository", "Getting food details for id: $x")
            getFullFood(x)
        }
    }

    suspend fun getFood(letter: String): Response<FoodsListResponse> {
        var foodResp = foodApiService.getFoodList(letter)
        if (foodResp.isSuccessful) {
            return foodResp
        } else
            return Response.error(null, null)
    }

    suspend fun getRandFood(): Response<FoodsListResponse> {
        return withTimeout(5000) { // Timeout after 5000 milliseconds (5 seconds)
            val seed = System.currentTimeMillis()
            Log.d("FoodRepository", "Generated seed: $seed")
            var x = (52771..FOOD_HEROES).random(Random(seed))
            Log.d("FoodRepository", "Generated random number: $x")
            getFullFood(x)
        }
    }

    suspend fun getSomeRandFoods(num: Int): Response<List<FoodsListResponse>> {
        var foodList: MutableList<FoodsListResponse> = mutableListOf()
        for (i in 52771..num) {
            val foodResp = getRandFullFood() // use the current instance
            if (foodResp.isSuccessful) {
                foodResp.body()?.let { foodList.add(it) }
            } else {
                val errorBody = foodResp.errorBody()?.string()
            }
        }
        return Response.success(foodList)
    }

    suspend fun getFoodsByIds(ids: List<Int>): Response<List<Food>> {
        // Se crea una lista mutable de comidas.
        var foodList: MutableList<Food> = mutableListOf()
        // Para cada id en la lista de ids, se obtienen los detalles de la comida.
        for (id in ids) {
            //Se obtiene la comida por id.
            Log.d("FoodRepository", "Getting food details for id: $id")
            val foodResp = getFullFood(id)
            if (foodResp.isSuccessful) {
                foodResp.body()?.let {
                    foodList.add(it.toFood())
                    Log.d("FoodRepository", "Added food to list: ${it.toFood()}")
                }
                Log.d("FoodRepository", "Current food list: $foodList")
            } else {
                val errorBody = foodResp.errorBody()?.string()
                Log.d("FoodRepository", "Error getting food details: $errorBody")
            }
        }
        return Response.success(foodList)
    }
}
