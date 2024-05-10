package com.example.foodcode2.adapter

import com.example.foodcode2.api.Food
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
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
            binding.tvCal.text = food.energyKcal.toString()
            if(food.ecoscoreGrade == "a"){
                binding.tvEco.setBackgroundResource(R.drawable.a)
            }else if(food.ecoscoreGrade == "b"){
                binding.tvEco.setBackgroundResource(R.drawable.b)
            }else if(food.ecoscoreGrade == "c"){
                binding.tvEco.setBackgroundResource(R.drawable.c)
            }else if(food.ecoscoreGrade == "d"){
                binding.tvEco.setBackgroundResource(R.drawable.d)
            }else if(food.ecoscoreGrade == "e"){
                binding.tvEco.setBackgroundResource(R.drawable.e)
            }else if(food.ecoscoreGrade == "unknown"){
                binding.tvEco.setBackgroundResource(R.drawable.none)
            }else if(food.ecoscoreGrade == "not-applicable"){
                binding.tvEco.setBackgroundResource(R.drawable.none)
            }

            binding.ivPhoto.load(food.imageUrl){
                transformations(CircleCropTransformation())
            }
            //hacer que cuando se haga click en el el item se navegue a la pantalla de detalles del alimento seleccionado y se le pase el alimento seleccionado como argumento a la pantalla de detalles
            binding.root.setOnClickListener {
                onClickItem(food.code)
            }
            //hacer que cuando se haga click en el boton de eliminar favoritos se elimine el alimento de la lista de favoritos y la base de datos.
            binding.ivFavFood.setOnClickListener {
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
