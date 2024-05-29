package com.example.foodcode2.ui.fav_details

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
import com.example.foodcode2.adapter.ImageAdapter
import com.example.foodcode2.databinding.FragmentFoodDetailsFavBinding
import com.example.foodcode2.ui.favFood.OnFoodSelectedListener
import com.example.foodcode2.ui.menu.MenuFragment
import kotlinx.coroutines.launch

interface OnBackButtonPressed {
    fun onBackButtonPressedGoToList()

    fun onButoonPressedGoToComentary(barcode: String)
}

class FoodFavDetailsFragment : Fragment() {

    private var _binding: FragmentFoodDetailsFavBinding? = null
    private lateinit var imageAdapter: ImageAdapter
    private val binding
        get() = _binding!!

    val args: FoodFavDetailsFragmentArgs by navArgs()

    private val foodFavDetailsVM by viewModels<FavFoodDetailsVM> { FavFoodDetailsVM.Factory }

    var listener: OnFoodSelectedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
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
    @SuppressLint("SetTextI18n")
    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                foodFavDetailsVM.uiState.collect { uiState ->
                    if (uiState.isLoading) {
                        binding.loadingAnimation.visibility = View.VISIBLE
                        binding.loadingAnimation.playAnimation()
                    } else {
                        binding.loadingAnimation.pauseAnimation()
                        binding.loadingAnimation.visibility = View.GONE
                        uiState.food?.let { food ->
                            binding.tvTitle.text = food.title
                            Log.d("foto", food.imageUrl)
                            binding.imgThumb.load(food.imageUrl) {
                                transformations(CircleCropTransformation())
                            }

                            binding.tvNutrientes.text = """
                                Carbohidratos: ${food.carbohydrates} ${food.carbohydratesUnit}
                                Energía: ${food.energy} kJ
                                Grasas: ${food.fat} ${food.fatUnit}
                                Proteínas: ${food.proteins} ${food.proteinsUnit}
                                Sal: ${food.salt} ${food.saltUnit}
                                Grasas saturadas: ${food.saturatedFat} ${food.saturatedFatUnit}
                                Sodio: ${food.sodium} ${food.sodiumUnit}
                                Azúcares: ${food.sugars} ${food.sugarsUnit}
                            """.trimIndent()
                            if (food.allergens == "") {
                                binding.tvAllergens.text = "No se han encontrado alérgenos"
                            } else binding.tvAllergens.text = food.allergens

                            if (food.ecoscoreGrade == "a") {
                                binding.tvScore.text = "Este alimento es saludable"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), android.R.color.holo_green_light
                                    )
                                )
                                //Obtenemos la imagen del score de open food facts y la mostramos
                                binding.imageViewScore.load(
                                    R.drawable.a
                                )
                            } else if (food.ecoscoreGrade == "b") {
                                binding.tvScore.text = "Este alimento es bastante saludable"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), android.R.color.holo_green_dark
                                    )
                                )
                                binding.imageViewScore.load(
                                    R.drawable.b
                                )
                            } else if (food.ecoscoreGrade == "c") {
                                binding.tvScore.text = "Este alimento es regular"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), android.R.color.holo_orange_light
                                    )
                                )
                                binding.imageViewScore.load(
                                    R.drawable.c
                                )
                            } else if (food.ecoscoreGrade == "d") {
                                binding.tvScore.text = "Este alimento es poco saludable"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), android.R.color.holo_orange_dark
                                    )
                                )
                                binding.imageViewScore.load(
                                    R.drawable.d
                                )
                            } else if (food.ecoscoreGrade == "e") {
                                binding.tvScore.text = "Este alimento es poco saludable"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), android.R.color.holo_red_dark
                                    )
                                )
                                binding.imageViewScore.load(
                                    R.drawable.e
                                )
                            } else if (food.ecoscoreGrade == "unknown") {
                                binding.tvScore.text = "La calidad es desconocida"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), android.R.color.holo_blue_light
                                    )
                                )
                                binding.imageViewScore.load(R.drawable.none)
                            } else {
                                binding.tvScore.text = "No se ha podido determinar la calidad"
                                binding.cardScore.setCardBackgroundColor(
                                    ContextCompat.getColor(
                                        requireContext(), R.color.botones
                                    )
                                )
                                binding.imageViewScore.load(R.drawable.none)
                            }
                            if (food.brands == "") {
                                binding.tvProveedor.text = "No se ha encontrado proveedor"
                            } else {
                                binding.tvProveedor.text = food.brands

                            }
                            if (food.categories == "") {
                                binding.tvCategoria.text = "No se ha encontrado categoría"
                            } else {
                                binding.tvCategoria.text = food.categories

                            }

                            if (food.traces == "") {
                                binding.traza.text = "No se ha encontrado trazas"
                            } else {
                                binding.traza.text = food.traces
                            }

                            if (food.packaging == "") {
                                binding.paquete.text = "No se ha encontrado envase"
                            } else {
                                binding.paquete.text = food.packaging
                            }

                            if (food.manufacturingPlaces == "") {
                                binding.tvManufacturing.text =
                                    "No se ha encontrado lugar de fabricación"
                            } else {
                                binding.tvManufacturing.text = food.manufacturingPlaces
                            }

                            if (food.additives_original_tags == "") {
                                binding.tvAdditives.text = "No se han encontrado aditivos"
                            } else {
                                binding.tvAdditives.text = food.additives_original_tags
                            }

                            uiState.food?.let { food ->
                                //anadimos las imagenes a la lista de imagenes
                                val listImage = mutableListOf<String>()
                                listImage.add(food.imageUrl)
                                listImage.add(food.image_nutrition_url)
                                listImage.add(food.image_ingredients_small_url)
                                //inicializamos el recycler view con las imagenes de la lista
                                initRecyclerView(listImage)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initRecyclerView(listImage: MutableList<String>) {
        imageAdapter = ImageAdapter(listImage)
        imageAdapter.setImages(listImage)
        binding.rvSimilarProducts.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvSimilarProducts.adapter = imageAdapter
        imageAdapter.notifyDataSetChanged()
    }

    private fun setListeners() {
        binding.toolbarDetail.setNavigationOnClickListener {
            (parentFragment as? MenuFragment)?.onBackButtonPressedGoToList()
        }

        binding.cardComentary.setOnClickListener {
            (parentFragment as? MenuFragment)?.onButoonPressedGoToComentary(args.foodId)
        }


//        binding.fabAdd.setOnClickListener {
//
//        }
    }
}