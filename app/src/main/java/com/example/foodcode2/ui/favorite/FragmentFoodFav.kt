package com.example.foodcode2.ui.favorite

import Food
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.NavigationCallback
import com.example.foodcode2.R
import com.example.foodcode2.adapter.FavoriteAdapter
import com.example.foodcode2.databinding.FragmentFavFoodBinding

class FragmentFoodFav : Fragment(), NavigationCallback {

    private var _binding: FragmentFavFoodBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavFoodBinding.inflate(inflater, container, false)
        favoriteAdapter = FavoriteAdapter()
        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /*
        binding.apply {
            //Load data
            viewModel.getFavoritesFoodList()
            viewModel.foodList.observe(viewLifecycleOwner) { dataStatus ->
                dataStatus.data?.let { foods ->
                    if (foods.isEmpty()) {
                        // Handle empty state
                    } else {
                        favoriteAdapter.setData(foods)
                        rvFavFood.setupRecyclerView(LinearLayoutManager(requireContext()), favoriteAdapter)
                        favoriteAdapter.setOnItemClickListener { food ->
                            navigateToDetails(food)
                        }
                    }
                }
            }
        }

         */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}