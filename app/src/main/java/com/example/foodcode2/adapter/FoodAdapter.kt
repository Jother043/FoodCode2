import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
import com.example.foodcode2.api.Food
import com.example.foodcode2.databinding.FoodItemBinding

class FoodAdapter(
    private var _listFood: MutableList<Food>,
    private val onClickItem: (String) -> Unit,
    private val onClickToFavorites: (Food) -> Unit,
) : RecyclerView.Adapter<FoodAdapter.ListViewHolder>() {


    inner class ListViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        private val binding = FoodItemBinding.bind(view)

        /**
         * Funcion que se encarga de asignar los valores de la comida a los elementos de la vista
         */
        fun bind(food: Food, onClickItem: (String) -> Unit, onClickToFavorites: (Food) -> Unit) {

            binding.tvName.text = food.title
            binding.tvCalories.text = food.energyKcal.toString()
            binding.tvProteins.text = food.energyKcal.toString()
            binding.ivPhoto.load(food.imageUrl) {
                transformations(CircleCropTransformation())
            }

            if (food.isFavorite) {
                binding.ivFavFood.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorFavorite
                    )
                )
            } else {
                binding.ivFavFood.setColorFilter(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.colorNotFavorite
                    )
                )
            }
            //hacer que cuando se haga click en el el item se navegue a la pantalla de detalles del alimento seleccionado y se le pase el alimento seleccionado como argumento a la pantalla de detalles
            binding.root.setOnClickListener {
                onClickItem(food.code)
            }
            //hacer que cuando se haga click en el boton de favoritos se añada el alimento a la lista de favoritos
            binding.ivFavFood.setOnClickListener {
                onClickToFavorites(food)
                updateFavoriteStatus(food)
            }
        }
    }

    /**
     * Funcion que se encarga de actualizar el estado de favorito de un alimento en la lista de alimentos
     *
     * @param food alimento a actualizar
     */
    fun updateFavoriteStatus(food: Food) {
        //Busca el indice del alimento en la lista de alimentos
        val index = _listFood.indexOfFirst { it.code == food.code }
        //Si el alimento se encuentra en la lista de alimentos
        if (index != -1) {
            //si el alimento no esta en favoritos se cambia el estado de favorito del alimento
            if(!_listFood[index].isFavorite) {
                _listFood[index].isFavorite = !food.isFavorite
                notifyItemChanged(index)
            }else{
                //TODO: Eliminar de favoritos si ya esta en favoritos.
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(
            layoutInflater.inflate(R.layout.food_item, parent, false),

            )
    }
    /**
     * Funcion que se encarga de retornar el numero de elementos en la lista de alimentos
     */
    override fun getItemCount(): Int {
        return _listFood.size
    }

    /**
     * Funcion que se encarga de asignar los valores de la comida a los elementos de la vista
     */
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(_listFood[position], onClickItem, onClickToFavorites)

    }

    /**
     * Funcion se utiliza para actualizar la lista de alimentos _listFood con una nueva lista de alimentos.
     */
    fun setFoodList(foodList: List<Food>) {
        _listFood = foodList.toMutableList()
    }
}