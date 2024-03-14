
import androidx.room.Entity
import androidx.room.PrimaryKey

const val FOOD_TABLE = "food_table"
@Entity(tableName = FOOD_TABLE)
data class FoodEntity(
    @PrimaryKey
    var id: Int = 0,
    var title: String = "",
    var category: String = "",
    var area: String = "",
    var img: String = ""
)