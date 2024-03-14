package com.example.foodcode2

import Food
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodcode2.databinding.FragmentFavFoodBinding

class FavFoodFragment : Fragment(), NavigationCallback {
    //Este fragmento muestra la lista de series favoritas que se han añadido en la lista de series
    //Se ha creado un nuevo fragmento para que se muestre en una pestaña diferente

    private var _binding: FragmentFavFoodBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFavFoodBinding.inflate(inflater, container, false)
        //initRecyclerView()
        return binding.root

    }

    //Funcion que muestra la lista que se va a ir actualizando con las series favoritas
    /*private fun initRecyclerView() {
        val foodAdapter = FoodAdapter(foods, ::favFood, this)
        binding.rvFavFood.adapter = foodAdapter
        val layaoutManager = LinearLayoutManager(context)
        binding.rvFavFood.layoutManager = layaoutManager
    }

     */

    //Se elimina de la lista de series y se añade a la lista de favoritos
   /* fun favFood(position: Int) {
        foods.removeAt(position)
        binding.rvFavFood.adapter?.notifyItemRemoved(position)
        foods[position].fav = true
        binding.rvFavFood.adapter?.notifyItemChanged(position)
    }

    */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FavFoodFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun navigateToDetails(food: Food) {
        findNavController().navigate(R.id.action_listFragment_to_foodDetailsFragment)
    }

}