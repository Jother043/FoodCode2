package com.example.foodcode2.ui.details
import FoodDetailsVM
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.foodcode2.databinding.FragmentFoodDetailsBinding
import kotlinx.coroutines.launch


class FoodDetailsFragment : Fragment() {

    private var _binding: FragmentFoodDetailsBinding? = null
    val binding
        get() = _binding!!

    val args: FoodDetailsFragmentArgs by navArgs()

    private val foodDetailsVM by viewModels<FoodDetailsVM> { FoodDetailsVM.Factory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFoodDetailsBinding.inflate(inflater, container, false)
        foodDetailsVM.setFood(args.food)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setCollectors()
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                foodDetailsVM.uiState.collect { foodState ->
                    if(!foodState.isLoading) {
                        binding.foodTitleTxt.text = foodState.food?.title ?: ""
                        binding.measureImg.load(foodState.food?.strMealThumb)
                    }
                }
            }
        }
    }
}