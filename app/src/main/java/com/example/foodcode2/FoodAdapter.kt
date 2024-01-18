package com.example.foodcode2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodcode2.databinding.FoodItemBinding
import com.google.android.material.snackbar.Snackbar

class FoodAdapter (
    private val listFood: MutableList<Food>,
    private val onClickToFavorites: (Int) -> Unit
) : RecyclerView.Adapter<FoodAdapter.ListViewHolder>() {

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        companion object {
            const val DRAWABLE = "drawable"
        }

        private val binding = FoodItemBinding.bind(view)

        fun bind(food: Food, onClickToFavorites: (Int) -> Unit) {
            binding.tvName.text = food.name
            binding.tvCalories.text = food.calories.toString()
            binding.tvProteins.text = food.proteins.toString()
            val context = itemView.context
            binding.ivPhoto.setImageResource(
                context.resources.getIdentifier(
                    food.photo,
                    DRAWABLE, context.packageName
                )
            )

            binding.root.setOnClickListener {
                Snackbar.make(
                    it,
                    "Has seleccionado el alimento: ${food.name}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            binding.ivFavFood.setOnClickListener {
                Snackbar.make(
                    it,
                    "Has añadido a favoritos el alimento: ${food.name}",
                    Snackbar.LENGTH_SHORT
                ).show()
                onClickToFavorites(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ListViewHolder(layoutInflater.inflate(R.layout.food_item, parent, false))
    }

    override fun getItemCount(): Int {
        return listFood.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listFood[position], onClickToFavorites)
    }
}