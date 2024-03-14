package com.example.foodcode2.ui.herolist

import Food
import FoodAdapter
import FoodListVM
import android.content.res.Configuration
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodcode2.databinding.FragmentListBinding
import kotlinx.coroutines.launch

class ListFragment : Fragment(){

    private var _binding: FragmentListBinding? = null
    val binding
        get() = _binding!!

    private val foodListVM by viewModels<FoodListVM> { FoodListVM.Factory }

    private lateinit var foodAdapter: FoodAdapter

    private val foodListState: MutableList<Food> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentListBinding.inflate(inflater, container, false)
        binding.textView.text = "Food List"
        return binding.root
    }

    private fun selectFood(foodId: Int) {
        //Como paso esta comida a la pantalla de detalles?
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment2(foodId)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecView()

        setCollectors()
    }

    private fun initRecView() {
        foodAdapter = FoodAdapter(
            _listFood = mutableListOf(),
            onClickItem = { foodId -> selectFood(foodId) },
            onClickToFavorites = {  }
        )
        binding.rvFoods.adapter = foodAdapter
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.rvFoods.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        else {
            binding.textView.visibility = View.GONE
            binding.rvFoods.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                foodListVM.uiState.collect { state ->
                    if (state.isLoading) {
                        binding.loadingGif.visibility = View.VISIBLE
                    } else {
                        binding.loadingGif.visibility = View.INVISIBLE
                        foodAdapter.setFoodList(state.foodList)
                        foodAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }




}