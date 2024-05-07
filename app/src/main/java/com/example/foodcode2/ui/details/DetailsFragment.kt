package com.example.foodcode2.ui.details

import FoodDetailsVM
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.databinding.FragmentDetailsBinding
import kotlinx.coroutines.launch

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!

    val args: DetailsFragmentArgs by navArgs()

    private val detailsVM by viewModels<FoodDetailsVM> { FoodDetailsVM.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(layoutInflater, container, false)

        detailsVM.setFood(args.food)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectors()

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    //Función que se encarga de recolectar los datos de la comida y mostrarlos en la vista
    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailsVM.uiState.collect { foodState ->
                    if (!foodState.isLoading) {
                        binding.pbLoadingDetails.isVisible = false
                        foodState.food?.let { food ->
                            binding.tvName.text = food.title
                            binding.ivPhoto.load(food.imageUrl) {
                                transformations(CircleCropTransformation())
                            }
                            binding.rbPower.text = food.energyKcal.toString()
                            binding.rbIntelligence.text = food.ecoscoreGrade
                            binding.tvDesc.text = food.title

                        }
                    } else {
                        binding.pbLoadingDetails.isVisible = true
                    }
                }
            }
        }
    }

}