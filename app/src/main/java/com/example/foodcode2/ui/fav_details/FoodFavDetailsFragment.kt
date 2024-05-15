package com.example.foodcode2.ui.fav_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentFoodDetailsFavBinding
import kotlinx.coroutines.launch

class FoodFavDetailsFragment : Fragment() {

    private var _binding: FragmentFoodDetailsFavBinding? = null

    private val binding
        get() = _binding!!

    val args: FoodFavDetailsFragmentArgs by navArgs()

    private val foodFavDetailsVM by viewModels<FavFoodDetailsVM> { FavFoodDetailsVM.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodDetailsFavBinding.inflate(layoutInflater, container, false)

        foodFavDetailsVM.setFood(args.foodId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollectors()

        setListeners()

    }

    //Función que se encarga de recolectar los datos de la comida y mostrarlos en la vista
    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                foodFavDetailsVM.uiState.collect { uiState ->
                    if (uiState.isLoading) {
                        binding.loadingGif.visibility = View.VISIBLE
                    } else {
                        binding.loadingGif.visibility = View.INVISIBLE
                        uiState.food?.let { food ->
                            binding.tvName.text = food.title
                            binding.ivPhoto.load(food.imageUrl) {
                                transformations(CircleCropTransformation())
                            }
                            binding.tvDesc.text = food.energyKcal.toString()
                            binding.tvCategoria.text = food.energyKcal.toString()
                            binding.tvOrigen.text = food.energyKcal.toString()
                        }
                    }
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabAdd.setOnClickListener {
            val action =
                FoodFavDetailsFragmentDirections.actionFoodFavDetailsFragmentToComentaryFragment(
                    foodId = args.foodId
                )
            findNavController().navigate(action)
        }
    }
}