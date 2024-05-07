package com.example.foodcode2.adapter

import com.example.foodcode2.api.Food
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.databinding.FoodsFavItemBinding


class FavoriteAdapter (
    private var _favList: MutableList<Food>,
    private val onClickItem: (String) -> Unit,
    private val onClickDelFavorites: (Food) -> Unit
): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    /**
     * Clase que se encarga de asignar los valores de la comida a los elementos de la vista
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = FoodsFavItemBinding.bind(view)

        fun bind(food: Food, onClickItem: (String) -> Unit, onClickDelFavorites: (Food) -> Unit){
            binding.tvName.text = food.title
            binding.tvCalories.text = food.energyKcal.toString()
            binding.tvProteins.text = food.energyKcal.toString()
            binding.ivPhoto.load(food.imageUrl){
                transformations(CircleCropTransformation())
            }
            //hacer que cuando se haga click en el el item se navegue a la pantalla de detalles del alimento seleccionado y se le pase el alimento seleccionado como argumento a la pantalla de detalles
            binding.root.setOnClickListener {
                onClickItem(food.code)
            }
            //hacer que cuando se haga click en el boton de eliminar favoritos se elimine el alimento de la lista de favoritos y la base de datos.
            binding.ivFavDelFood.setOnClickListener {
                onClickDelFavorites(food)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(com.example.foodcode2.R.layout.foods_fav_item,parent,false))
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.ViewHolder, position: Int) {
        holder.bind(_favList[position],onClickItem, onClickDelFavorites)
    }

    override fun getItemCount(): Int {
        return _favList.size
    }

    fun setFavList(favList : List<Food>) {
        _favList = favList.toMutableList()
    }


}
