
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FoodItemBinding

class FoodAdapter(
    private var _listFood: MutableList<Food>,
    private val onClickItem: (Int) -> Unit,
    private val onClickToFavorites: (Food) -> Unit,
) : RecyclerView.Adapter<FoodAdapter.ListViewHolder>() {
    class ListViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val binding = FoodItemBinding.bind(view)

        fun bind(food: Food, onClickItem: (Int) -> Unit, onClickToFavorites: (Food) -> Unit){
            binding.tvName.text = food.title
            binding.tvCalories.text = food.strArea
            binding.tvProteins.text = food.strCategory
            binding.ivPhoto.load(food.strMealThumb)
            //hacer que cuando se haga click en el el item se navegue a la pantalla de detalles del alimento seleccionado y se le pase el alimento seleccionado como argumento a la pantalla de detalles
            binding.root.setOnClickListener {
                onClickItem(food.id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(
            layoutInflater.inflate(R.layout.food_item, parent, false),

        )
    }

    override fun getItemCount(): Int {
        return _listFood.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(_listFood[position],onClickItem, onClickToFavorites)

    }

    fun setFoodList(foodList: List<Food>) {
        _listFood = foodList.toMutableList()
    }
}