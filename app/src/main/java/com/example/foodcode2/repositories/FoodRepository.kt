import android.util.Log
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.data.FoodsListResponse
import com.example.foodcode2.db.FoodDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class FoodRepository(
    val foodApiService: ApiService,

) {

    suspend fun getFullFood(id: Int): Response<FoodsListResponse> {
        return foodApiService.getFoodDetails(id)
    }
    /**
     * Obtiene unas comidas registradas en una lista de comidas.
     */
    suspend fun getFoodsByIds(ids: List<Int>): Response<List<Food>> {
        // Se crea una lista mutable de comidas.
        var foodList: MutableList<Food> = mutableListOf()
        // Para cada id en la lista de ids, se obtienen los detalles de la comida.
        Log.d("FoodRepository", "Numero de ids: ${ids.size}")
        ids.forEach() { idFood ->
            val foodResp = foodApiService.getFoodDetails(idFood)
            if (foodResp.isSuccessful) {
                val food = foodResp.body()?.toFood()
                if (food != null) {
                    foodList.add(food)
                }
            } else {
                val errorBody = foodResp.errorBody()?.string()
                Log.d("FoodRepository", "Error getting food details: $errorBody")
                return Response.error(404, foodResp.errorBody()!!)
            }
        }
        return Response.success(foodList)
    }

    /**
     * Obtiene una comida por su id.
     */
    suspend fun getFoodDetailById(id: Int): Response<Food> {
        val foodResp = foodApiService.getFoodDetails(id)
        if (foodResp.isSuccessful) {
            val food = foodResp.body()?.toFood()
            if (food != null) {
                return Response.success(food)
            }
        } else {
            val errorBody = foodResp.errorBody()?.string()
            Log.d("FoodRepository", "Error getting food details: $errorBody")
            return Response.error(404, foodResp.errorBody()!!)
        }
        return Response.error(404, foodResp.errorBody()!!)
    }

}
