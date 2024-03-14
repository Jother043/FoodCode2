
import Food
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodcode2.NavigationCallback
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FoodItemBinding

import com.example.foodcode2.ui.herolist.ListFragmentDirections
import com.google.android.material.snackbar.Snackbar

class FoodAdapter(
    var listFood: MutableList<Food>,
    private val onClickItem: (Int) -> Unit,
    private val onClickToFavorites: (Int) -> Unit,
    private val callback: NavigationCallback
) : RecyclerView.Adapter<FoodAdapter.ListViewHolder>() {
    class ListViewHolder(view: View, private val callback: NavigationCallback) :
        RecyclerView.ViewHolder(view) {
        companion object {
            const val DRAWABLE = "drawable"
        }

        private val binding = FoodItemBinding.bind(view)

        fun bind(food: Food, onClickToFavorites: (Int) -> Unit) {
            binding.tvName.text = food.title
            binding.tvCalories.text = food.strArea
            binding.tvProteins.text = food.strCategory
            binding.ivPhoto.load(food.strMealThumb)

            binding.root.setOnClickListener {
                callback.navigateToDetails(food)
            }

            binding.ivFavFood.setOnClickListener {
                Snackbar.make(
                    it,
                    "Has añadido a favoritos el alimento: ${food.title}",
                    Snackbar.LENGTH_SHORT
                ).show()
                onClickToFavorites(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(
            layoutInflater.inflate(R.layout.food_item, parent, false),
            this.callback
        )
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFood[position], onClickToFavorites)
        holder.itemView.setOnClickListener {
            onClickItem(position)
        }
    }

    fun setFoodList(foodList: List<Food>) {
        listFood = foodList.toMutableList()
    }
}