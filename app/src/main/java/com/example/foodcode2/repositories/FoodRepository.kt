import android.util.Log
import com.example.foodcode2.api.ApiService
import com.example.foodcode2.api.Food
import com.example.foodcode2.data.FoodsListResponse
import retrofit2.Response

class FoodRepository(
    val foodApiService: ApiService,

    ) {

    suspend fun getFullFood(barcode: String): Response<FoodsListResponse> {
        return foodApiService.getProduct(barcode)
    }

    /**
     * Obtiene unas comidas registradas en una lista de comidas.
     */
    suspend fun getProductByBarcode(barcodes: List<String>): Response<List<Food>> {

        var foodList: MutableList<Food> = mutableListOf()

        try {
            /*
             * Iteramos sobre la lista de códigos de barras.(Que siempre será de tamaño 1)
             * y obtenemos la información de cada producto.
             * Siempre sera de tamaño 1 ya que solo se escanea un producto a la vez.
             */
            barcodes.forEach() { barcodeProduct ->
                //val foodResp variable que almacena la respuesta de la API.
                //TODO: deserialización de la respuesta de la API.
                Log.d("FoodRepository", "getProductByBarcode: $barcodeProduct")
                val foodResp = foodApiService.getProduct(barcodeProduct)
                Log.d("FoodRepository", "getProductByBarcode: $foodResp")
                if (foodResp.isSuccessful && foodResp.body() != null) {
                    val food = foodResp.body()?.toFood()
                    if (food != null) {
                        foodList.add(food)
                        Log.d("FoodRepository", "El producto es: $food")
                    }
                } else {
                    val errorBody = foodResp.errorBody()?.string()
                    Log.d("FoodRepository", "Error getting food details: $errorBody")
                    return Response.error(
                        404,
                        foodResp.errorBody() ?: return Response.error(404, null)
                    )
                }
            }
        } catch (e: Exception) {
            Log.d("FoodRepository", "Error getting food details: ${e.message}")
            return Response.error(404, null)
        }
        return Response.success(foodList)
    }

    /**
     * Obtiene una comida por su id.
     */
    //TODO: PUEDE QUE TENGAMOS QUE ELEMINAR ESTE MÉTODO.
    suspend fun getFoodDetailById(barcode: String): Response<Food> {
        val foodResp = foodApiService.getProduct(barcode)
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
