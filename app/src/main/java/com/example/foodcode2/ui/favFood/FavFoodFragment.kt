package com.example.foodcode2.ui.favFood

import com.example.foodcode2.api.Food
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
import com.example.foodcode2.R
import com.example.foodcode2.adapter.FavoriteAdapter
import com.example.foodcode2.databinding.FragmentFavFoodBinding
import com.example.foodcode2.ui.comentary.ComentaryVM
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

class FavFoodFragment : Fragment() {
    //Este fragmento muestra la lista de series favoritas que se han añadido en la lista de series
    //Se ha creado un nuevo fragmento para que se muestre en una pestaña diferente

    private var _binding: FragmentFavFoodBinding? = null
    private val binding
        get() = _binding!!

    private val favFoodVM by viewModels<FavFoodVM> { FavFoodVM.Factory }
    private val comentaryVM by viewModels<ComentaryVM> { ComentaryVM.Factory }
    private lateinit var favoriteAdapter: FavoriteAdapter
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecView()

        collectors()

        setListeners()

    }

    //Funcion que se encarga de recolectar los datos e inicializar el recyclerView
    private fun initRecView() {
        favoriteAdapter = FavoriteAdapter(
            _favList = mutableListOf(),
            onClickItem = { food -> selectFood(food) },
            onClickDelFavorites = { food -> confirmDeleteFood(food) }
        )
        binding.rvFavFood.adapter = favoriteAdapter
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.rvFavFood.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        else {
            binding.textView.visibility = View.GONE
            binding.rvFavFood.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }


    //Función que se encarga de recolectar los datos de la comida y mostrarlos en la vista
    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favFoodVM.uiState.collect {
                    if (!it.isLoading) {
                        binding.loadingGif.visibility = View.INVISIBLE
                        favoriteAdapter.setFavList(it.favFoodList)
                        favoriteAdapter.notifyDataSetChanged()
                    } else {
                        binding.loadingGif.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    //Función que se encarga de navegar a la pantalla de detalles de la comida
    private fun selectFood(foodId: String) {
        comentaryVM.uiState.value.foodId = foodId
        val action = FavFoodFragmentDirections.actionFavFoodFragmentToFoodFavDetailsFragment(foodId)
        findNavController().navigate(action)
    }

    private fun confirmDeleteFood(food: Food) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.remove))
            .setMessage(resources.getString(R.string.confirm_delete))

            .setNegativeButton(resources.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                // Respond to positive button press
                favFoodVM.delFavFood(food)
            }
            .show()
    }

    //Funcion para volver a la pantalla anterior
    private fun setListeners() {
        binding.btBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}