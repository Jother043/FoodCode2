package com.example.foodcode2.ui.herolist

import com.example.foodcode2.api.Food
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
import com.example.foodcode2.dependencies.FoodCode
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    val binding
        get() = _binding!!

    private val foodListVM by viewModels<FoodListVM> {
        FoodListVM.FoodListVMFactory(
            (requireActivity().application as FoodCode).appContainer.FoodRepository,
            (requireActivity().application as FoodCode).appContainer.favoriteFoodRepository
        )
    }

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
        binding.textView.text = "Lista de Comidas"
        return binding.root
    }

    //Función que se encarga de añadir una comida a la lista de favoritos
    private fun addFavorite(food: Food) {
        foodListVM.saveToFavorite(food)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                foodListVM.uiState.collect { foodState ->
                    //Si la comida ya está en la lista de favoritos, se muestra un mensaje de que ya está en la lista
                    if (foodListVM.uiState.value.isFavorite) {
                        Snackbar.make(
                            binding.root,
                            "${food.title} Ya esta en la lista de favoritos.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "${food.title} Añadido a la lista de favoritos.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    //Función que se encarga de seleccionar una comida de la lista y mostrar sus detalles
    private fun selectFood(foodId: String) {
        //Como paso esta comida a la pantalla de detalles?
        val action = ListFragmentDirections.actionListFragmentToDetailsFragment2(foodId)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        initRecView()

        collectors()
    }

    //Función que se encarga de recolectar los datos e inicializar el recyclerView
    private fun initRecView() {
        foodAdapter = FoodAdapter(
            _listFood = mutableListOf(),
            onClickItem = { foodId -> selectFood(foodId) },
            onClickToFavorites = { food -> addFavorite(food) }
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

    //Función que se encarga de recolectar los datos de la comida y mostrarlos en la vista
    private fun collectors() {
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

    //Función que se encarga de añadir una comida a la lista de favoritos
    private fun setListeners() {

    }


}