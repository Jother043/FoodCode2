package com.example.foodcode2.ui.favFood

import android.content.Context.VIBRATOR_SERVICE
import com.example.foodcode2.api.Food
import android.content.res.Configuration
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.HapticFeedbackConstants
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieDrawable
import com.example.foodcode2.R
import com.example.foodcode2.adapter.FavoriteAdapter
import com.example.foodcode2.databinding.FragmentFavFoodBinding
import com.example.foodcode2.ui.comentary.ComentaryVM
import com.example.foodcode2.ui.menu.MenuFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch

interface OnFoodSelectedListener {
    fun onFoodSelected(foodId: String)
    fun onBackButtonPressed()
}


class FavFoodFragment : Fragment() {
    //Este fragmento muestra la lista de series favoritas que se han añadido en la lista de series
    //Se ha creado un nuevo fragmento para que se muestre en una pestaña diferente

    private var _binding: FragmentFavFoodBinding? = null
    private val binding
        get() = _binding!!

    private val favFoodVM by viewModels<FavFoodVM> { FavFoodVM.Factory }
    private val comentaryVM by viewModels<ComentaryVM> { ComentaryVM.Factory }
    private lateinit var favoriteAdapter: FavoriteAdapter

    private var vibrator: Vibrator? = null

    var listener: OnFoodSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        // Inicializa el vibrador
        vibrator = requireActivity().getSystemService(VIBRATOR_SERVICE) as Vibrator
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
        listener()
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
            binding.tvTitle.visibility = View.GONE
            binding.rvFavFood.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        val itemTouchHelper =
            //Clase que se encarga de detectar el deslizamiento de un item en el recyclerView
            ItemTouchHelper(object : SwipeToDeleteCallback(this.requireContext()) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val position = viewHolder.adapterPosition
                    val food = favoriteAdapter.getFoodAtPosition(position)
                    //vibracion del movil al deslizar el item
                    vibrator?.vibrate(VibrationEffect.createOneShot(120, 30))
                    val isDeleted = confirmDeleteFood(food)

                    if (!isDeleted) {
                        // Si el alimento no se ha eliminado, vuelve a su estado original
                        favoriteAdapter.notifyItemChanged(position)
                    }

                }
            })

        itemTouchHelper.attachToRecyclerView(binding.rvFavFood)
    }

    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                favFoodVM.uiState.collect { uiState ->
                    if (uiState.isLoading) {
                        // Si se está cargando algo, muestra y reproduce la animación de carga
                        binding.loadingAnimation.visibility = View.VISIBLE
                        binding.loadingAnimation.playAnimation()
                    } else {
                        // Si no se está cargando nada, oculta la animación de carga
                        binding.loadingAnimation.visibility = View.GONE
                        binding.loadingAnimation.pauseAnimation()

                        if (uiState.favFoodList.isEmpty()) {
                            // No hay favoritos, muestra la animación y el texto, oculta el RecyclerView
                            binding.noFavoritesAnimation.visibility = View.VISIBLE
                            binding.noFavoritesAnimation.repeatCount = LottieDrawable.INFINITE
                            binding.noFavoritesAnimation.playAnimation()
                            binding.noFavoritesText.visibility = View.VISIBLE
                            binding.rvFavFood.visibility = View.GONE
                        } else {
                            // Hay favoritos, oculta la animación y el texto, muestra el RecyclerView
                            binding.noFavoritesAnimation.visibility = View.GONE
                            binding.noFavoritesAnimation.pauseAnimation()
                            binding.noFavoritesText.visibility = View.GONE
                            binding.rvFavFood.visibility = View.VISIBLE
                            favoriteAdapter.setFavList(uiState.favFoodList)
                            favoriteAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
    }

    //Función que se encarga de navegar a la pantalla de detalles de la comida desde fireStore
    private fun selectFood(foodId: Food) {
        listener?.onFoodSelected(foodId.code)
    }

    private fun confirmDeleteFood(food: Food): Boolean {
        var elininado = false
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.RoundedAlertDialog)
            .setTitle("Eliminar de favoritos")
            .setMessage("¿Estás seguro de que deseas eliminar este alimento de tus favoritos?")
            .setPositiveButton("Eliminar") { _, _ ->
                favFoodVM.deleteFavorite(food)
                favoriteAdapter.notifyDataSetChanged() // Actualiza el RecyclerView
                elininado = true
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
                elininado = false
            }
            .create()
        dialog.window?.attributes?.windowAnimations =
            R.style.DialogAnimation
        dialog.show()
        return elininado
    }

    private fun listener() {
        binding.toolbarDetail.setNavigationOnClickListener {
            (parentFragment as? MenuFragment)?.onBackButtonPressed()
        }
    }

}