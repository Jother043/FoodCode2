package com.example.foodcode2.ui.menu

import FoodListVM
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentMenuBinding
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.ui.favFood.FavFoodFragment
import com.example.foodcode2.ui.fav_details.FoodFavDetailsFragmentArgs
import com.example.foodcode2.ui.herolist.ListFragment
import com.example.foodcode2.ui.userpreferences.InfoUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.flow.callbackFlow


class MenuFragment : Fragment() {

    //Se inicializa el binding
    private var _binding: FragmentMenuBinding? = null

    //Se obtiene el binding
    private val binding get() = _binding!!

    //Se inicializa el viewmodel
    private val qrDetailsVM by viewModels<QrDetailsVM> { QrDetailsVM.Factory }

    val args: MenuFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtiene el resultado del escaneo del bundle de argumentos
        val barcode = args.barcode
        binding.tvProductName.text = "Producto: $barcode"

        // Solicita la información del producto al ViewModel
        Log.d("MenuFragment", "El código de barras escaneado es: $barcode")
        Log.d("MenuFragment", qrDetailsVM.setFood(barcode).toString())
        qrDetailsVM.setFood(barcode)

        // Observa los cambios en el estado de la interfaz de usuario
        lifecycleScope.launchWhenStarted {
            qrDetailsVM.uiState.collect { uiState ->
                // Actualiza la vista en función del estado de la interfaz de usuario
                if (uiState.isLoading) {
                    // Muestra un indicador de carga
                } else if (uiState.error.isNotEmpty()) {
                    // Muestra el error
                    Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()
                } else if (uiState.food != null) {
                    // Muestra la información del producto
                    binding.tvProductName.text = uiState.food.title
                    Log.d("MenuFragment", "El nombre del producto es: ${uiState.food.title}")
                    Log.d("MenuFragment", "La imagen del producto es: ${uiState.food.imageUrl}")
                    binding.ivProductImage.load(uiState.food.imageUrl) {
                        // Transforma la imagen en un cuadrado con bordes redondeados
                        transformations(CircleCropTransformation())

                        // Muestra una imagen de carga mientras se carga la imagen
                        placeholder(R.drawable.loading_gif)
                        // Muestra una imagen de error si no se puede cargar la imagen
                        error(R.drawable.ajustes)
                    }
                    //Si el producto no tiene un valor de Nutri-Score, se muestra un mensaje
                    if (uiState.food.ecoscoreGrade == "not-applicable") {
                        binding.tvNutritionScore.text =
                            "Nutri-Score: No disponible para este tipo de producto"
                        //pintamos el fondo de gris para indicar que no hay valor
                        binding.tvRecomendacion.text = "No hay recomendación para este producto"
                        binding.linearLayout2.setBackgroundResource(R.drawable.borde_gris)
                        binding.ivNutritionScore.setBackgroundResource(R.drawable.none)
                    }else if (uiState.food.ecoscoreGrade == "a") {
                        binding.tvRecomendacion.text = "Este producto es muy saludable"
                        binding.linearLayout2.setBackgroundResource(R.drawable.bordes_verde_fuerte)
                        binding.ivNutritionScore.setBackgroundResource(R.drawable.a)
                    }
                    else if (uiState.food.ecoscoreGrade == "b") {
                        binding.tvRecomendacion.text = "Este producto es saludable"
                        binding.linearLayout2.setBackgroundResource(R.drawable.borde_verde)
                        binding.ivNutritionScore.setBackgroundResource(R.drawable.b)
                    }
                    else if (uiState.food.ecoscoreGrade == "c") {
                        binding.tvRecomendacion.text = "Este producto esta en la media de saludabilidad"
                        binding.linearLayout2.setBackgroundResource(R.drawable.borde_amarillo)
                        binding.ivNutritionScore.setBackgroundResource(R.drawable.c)
                    }
                    else if (uiState.food.ecoscoreGrade == "d") {
                        binding.tvRecomendacion.text = "Este producto no es saludable"
                        binding.linearLayout2.setBackgroundResource(R.drawable.border_naranja)
                        binding.ivNutritionScore.setBackgroundResource(R.drawable.d)
                    }
                    else if (uiState.food.ecoscoreGrade == "e") {
                        binding.tvRecomendacion.text = "Este producto no es nada saludable"
                        binding.linearLayout2.setBackgroundResource(R.drawable.rounded_border_red)
                        binding.ivNutritionScore.setBackgroundResource(R.drawable.e)

                    }else {
                        binding.tvNutritionScore.text = "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                    }
                    binding.tvDescription.text = uiState.food.energyKcal.toString() + " kcal"
                }
            }
        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {

        _binding = FragmentMenuBinding.inflate(layoutInflater)

        binding.fab2.setOnClickListener {
            initScanner()
        }

        binding.bottomNavigationView.background = null

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.listFragment -> {
                    // Oculta la imagen
                    binding.card.visibility = View.GONE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout.visibility = View.GONE

                    // Crea una nueva instancia del ListFragment
                    val listFragment = ListFragment()
                    // Realiza la transacción de fragmentos
                    childFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, listFragment)
                        .commit()
                    true
                }

                R.id.fragmentFoodFav -> {
                    // Oculta la imagen
                    binding.card.visibility = View.GONE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout.visibility = View.GONE
                    // Crea una nueva instancia del FragmentFoodFav
                    val fragmentFoodFav = FavFoodFragment()
                    // Realiza la transacción de fragmentos
                    childFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, fragmentFoodFav)
                        .commit()
                    true
                }

                R.id.infoUserFragment -> {
                    // Oculta la imagen
                    binding.card.visibility = View.GONE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout.visibility = View.GONE
                    // Crea una nueva instancia del InfoUserFragment
                    val infoUserFragment = InfoUserFragment()
                    // Realiza la transacción de fragmentos
                    childFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, infoUserFragment)
                        .commit()
                    true
                }

                else -> false
            }
        }

        return binding.root
    }


    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        integrator.setPrompt("Scanea el código de barra de tus productos")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.setOrientationLocked(false) // Permite que el escáner cambie de orientación
        integrator.initiateScan()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()

            } else {
                Log.d("Resultado", result.contents)
                val action = MenuFragmentDirections.actionMenuFragmentSelf(result.contents)
                findNavController().navigate(action)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun collectors() {
        //TODO: Implementar collectors para el viewmodel de QrDetailsVM y mostrar los datos en la vista
    }

}