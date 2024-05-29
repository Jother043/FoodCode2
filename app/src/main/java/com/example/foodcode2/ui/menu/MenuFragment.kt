package com.example.foodcode2.ui.menu


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
import com.example.foodcode2.api.Food
import com.example.foodcode2.databinding.FragmentMenuBinding
import com.example.foodcode2.dependencies.AppContainer
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.ui.comentary.ComentaryFragment
import com.example.foodcode2.ui.comentary.OnBackButtonPressedComentary
import com.example.foodcode2.ui.favFood.FavFoodFragment
import com.example.foodcode2.ui.favFood.OnFoodSelectedListener
import com.example.foodcode2.ui.fav_details.FoodFavDetailsFragment
import com.example.foodcode2.ui.fav_details.FoodFavDetailsFragmentArgs
import com.example.foodcode2.ui.fav_details.OnBackButtonPressed
import com.example.foodcode2.ui.userpreferences.InfoUserFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MenuFragment : Fragment(), OnFoodSelectedListener, OnBackButtonPressed,
    OnBackButtonPressedComentary {

    //Se inicializa el binding
    private var _binding: FragmentMenuBinding? = null

    //Se obtiene el binding
    private val binding get() = _binding!!

    //Se inicializa el viewmodel
    private val qrDetailsVM by viewModels<QrDetailsVM> { QrDetailsVM.Factory }
    val args: MenuFragmentArgs by navArgs()
    private var vibrator: Vibrator? = null
    private lateinit var appContainer: AppContainer
    private var isFlashOn = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        appContainer = (requireActivity().application as FoodCode).appContainer
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // No hacer nada
                }
            })

        // Obtiene el resultado del escaneo del bundle de argumentos
        val barcode = args.barcode
        binding.tvProductName.text = "Producto: $barcode"

        // Solicita la información del producto al ViewModel
        Log.d("MenuFragment", "El código de barras escaneado es: $barcode")
        Log.d("MenuFragment", qrDetailsVM.setFood(barcode).toString())
        qrDetailsVM.setFood(barcode)

        // Observa los cambios en el estado de la interfaz de usuario
        lifecycleScope.launch(Dispatchers.IO) {
            qrDetailsVM.uiState.collect { uiState ->
                withContext(Dispatchers.Main) {
                    // Actualiza la vista en función del estado de la interfaz de usuario
                    if (uiState.isLoading) {
                        // Muestra un indicador de carga
                    } else if (uiState.error.isNotEmpty()) {
                        //Hacemos invisible los componentes de la card y mostramos una animación de error
                        binding.lLayoutCard.visibility = View.GONE
                        binding.noScannedAnimation.visibility = View.VISIBLE
                        binding.scanPrompt.visibility = View.VISIBLE
                        binding.noInternetAnimation.playAnimation()

                    } else if (uiState.food != null) {
                        binding.constraintLayout.visibility = View.GONE
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
                            error(R.drawable.imagen_no_disponible)
                        }
                        //Si el producto no tiene un valor de Nutri-Score, se muestra un mensaje
                        if (uiState.food.ecoscoreGrade == "not-applicable") {
                            binding.tvNutritionScore.text =
                                "Nutri-Score: No disponible para este tipo de producto"
                            //pintamos el fondo de gris para indicar que no hay valor
                            binding.tvRecomendacion.text = "No hay recomendación para este producto"
                            binding.linearLayout2.setBackgroundResource(R.drawable.borde_gris)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.none)
                        } else if (uiState.food.ecoscoreGrade == "a") {
                            binding.tvRecomendacion.text = "Este producto es muy saludable"
                            binding.linearLayout2.setBackgroundResource(R.drawable.bordes_verde_fuerte)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.a)
                            binding.tvNutritionScore.text =
                                "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                        } else if (uiState.food.ecoscoreGrade == "b") {
                            binding.tvRecomendacion.text = "Este producto es saludable"
                            binding.linearLayout2.setBackgroundResource(R.drawable.borde_verde)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.b)
                            binding.tvNutritionScore.text =
                                "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                        } else if (uiState.food.ecoscoreGrade == "c") {
                            binding.tvRecomendacion.text =
                                "Este producto esta en la media de saludabilidad"
                            binding.linearLayout2.setBackgroundResource(R.drawable.borde_amarillo)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.c)
                            binding.tvNutritionScore.text =
                                "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                        } else if (uiState.food.ecoscoreGrade == "d") {
                            binding.tvRecomendacion.text = "Este producto no es saludable"
                            binding.linearLayout2.setBackgroundResource(R.drawable.border_naranja)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.d)
                            binding.tvNutritionScore.text =
                                "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                        } else if (uiState.food.ecoscoreGrade == "e") {
                            binding.tvRecomendacion.text = "Este producto no es nada saludable"
                            binding.linearLayout2.setBackgroundResource(R.drawable.rounded_border_red)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.e)
                            binding.tvNutritionScore.text =
                                "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                        } else {
                            binding.tvNutritionScore.text =
                                "Nutri-Score: ${uiState.food.ecoscoreGrade}"
                            binding.tvRecomendacion.text = "No hay recomendación para este producto"
                            binding.linearLayout2.setBackgroundResource(R.drawable.borde_gris)
                            binding.ivNutritionScore.setBackgroundResource(R.drawable.none)

                        }
                        binding.tvDescription.text = uiState.food.energyKcal.toString() + " kcal"
                    } else if (uiState.error.isNotEmpty()) {
                        binding.tvProductName.text = "Producto no encontrado"
                    }
                }
            }
        }

        binding.btnAddToFavorites.setOnClickListener {
            val food = qrDetailsVM.uiState.value.food
            if (food != null) {
                qrDetailsVM.addFoodToFavorites(food)
                //Muestro el layout
                binding.constraintLayout.visibility = View.VISIBLE
                lifecycleScope.launch {
                    qrDetailsVM.uiState.collect {
                        withContext(Dispatchers.Main) {
                            if (it.error.isNotEmpty()) {
                                val dialog = MaterialAlertDialogBuilder(
                                    requireContext(),
                                    R.style.RoundedAlertDialog
                                )
                                    .setTitle("El producto ya esta en favoritos")
                                    .setMessage("El producto con código de barras ${food.code} ya se encuentra en tus favoritos")
                                    .setPositiveButton("Aceptar") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                    .create()
                                dialog.window?.attributes?.windowAnimations =
                                    R.style.DialogAnimation
                                dialog.show()

                            } else if (it.addMessage.isNotEmpty()) {
                                val dialog = MaterialAlertDialogBuilder(
                                    requireContext(),
                                    R.style.RoundedAlertDialog
                                )
                                    .setTitle("Producto añadido a favoritos")
                                    .setMessage("El producto con código de barras ${food.code} ha sido añadido a tus favoritos")
                                    .setPositiveButton("Aceptar") { dialog, which ->
                                        dialog.dismiss()
                                    }
                                    .create()
                                dialog.window?.attributes?.windowAnimations =
                                    R.style.DialogAnimation
                                dialog.show()
                                //vuelvo a MOSTRAR LA ANIMACIÓN
                                binding.noScannedAnimation.visibility = View.VISIBLE
                                binding.scanPrompt.visibility = View.VISIBLE
                                binding.noScannedAnimation.playAnimation()


                            }
                        }
                    }
                }
            } else {
                Snackbar.make(
                    view,
                    "Error al añadir el producto a favoritos",
                    Snackbar.LENGTH_SHORT
                ).show()
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
//                R.id.listFragment -> {
//                    // Oculta la imagen
//                    binding.card.visibility = View.GONE
//                    binding.linearLayout2.visibility = View.GONE
//                    binding.linearLayout.visibility = View.GONE
//
//                    // Crea una nueva instancia del ListFragment
//                    val listFragment = ListFragment()
//                    // Realiza la transacción de fragmentos
//                    childFragmentManager.beginTransaction()
//                        .replace(R.id.frame_layout, listFragment)
//                        .commit()
//                    true
//                }

                R.id.fragmentFoodFav -> {
                    // Oculta la imagen
                    binding.card.visibility = View.GONE
                    binding.linearLayout2.visibility = View.GONE
                    binding.linearLayout.visibility = View.GONE
                    // Crea una nueva instancia del FragmentFoodFav
                    val fragmentFoodFav = FavFoodFragment().apply {
                        listener = this@MenuFragment
                    }
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
                    val infoUserFragment = InfoUserFragment(appContainer.userRepositories)
                    // Realiza la transacción de fragmentos
                    childFragmentManager.beginTransaction()
                        .replace(R.id.frame_layout, infoUserFragment)
                        .commit()
                    true
                }


                else -> false
            }
        }

        binding.btnAddToFavorites.setOnClickListener {

        }

        return binding.root
    }


    private fun initScanner() {
        if (!isNetworkAvailable(requireContext())) {
            // No hay conexión a Internet, muestra la animación
            binding.linearLayout2.visibility = View.GONE
            binding.constraintLayout.visibility = View.GONE
            binding.card.visibility = View.GONE
            binding.noInternetAnimation.visibility = View.VISIBLE
            binding.noInternetAnimation.playAnimation()
            binding.nointernetError.visibility = View.VISIBLE
        } else {
            binding.root.setOnLongClickListener(View.OnLongClickListener {
                if (isFlashOn) {
                    isFlashOn = false

                } else {
                    isFlashOn = true

                }
                true
            })
            // Hay conexión a Internet, oculta la animación
            binding.noInternetAnimation.visibility = View.GONE
            binding.noInternetAnimation.pauseAnimation()

            val integrator = IntentIntegrator.forSupportFragment(this)
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
            integrator.setPrompt("Scanea el código de barra de tus productos")
            integrator.setCameraId(0)
            integrator.setBarcodeImageEnabled(true)
            // Habilitar el sonido del escáner
            integrator.setBeepEnabled(true)
            // Habilitar el flash de la cámara
            integrator.setTorchEnabled(isFlashOn)
            integrator.initiateScan()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(context, "Cancelado", Toast.LENGTH_LONG).show()

            } else {
                vibrator?.vibrate(VibrationEffect.createOneShot(120, 30))
                Log.d("Resultado", result.contents)
                val action = MenuFragmentDirections.actionMenuFragmentSelf(result.contents)
                findNavController().navigate(action)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    var listener: OnFoodSelectedListener? = null
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onFoodSelected(foodId: String) {
        // Crear una nueva instancia de FoodFavDetailsFragment
        val foodFavDetailsFragment = FoodFavDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("foodId", foodId)
            }
        }

        // Realizar la transacción de fragmentos
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, foodFavDetailsFragment)
            .commit()
    }

    override fun onBackButtonPressed() {
        val fragmentMenu = MenuFragment().apply {
            arguments = Bundle().apply {
                putString("barcode", "")
            }
        }

        // Realiza la transacción de fragmentos
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragmentMenu)
            .commit()

    }

    //Si hay una sesión iniciada no puedo acceder a la pantalla de login
    override fun onResume() {
        super.onResume()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.VISIBLE
    }

    //botón para ir a la pantalla de favoritos
    override fun onBackButtonPressedGoToList() {
        val childFragments = childFragmentManager.fragments
        childFragments.forEach { fragment ->
            childFragmentManager.beginTransaction().remove(fragment).commit()
        }

        val fragmentFoodFav = FavFoodFragment().apply {
            listener = this@MenuFragment
        }
        // Realiza la transacción de fragmentos
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragmentFoodFav)
            .commit()
    }

    //botón para ir a la pantalla de comentarios
    override fun onButoonPressedGoToComentary(foodId: String) {
        // Crear una nueva instancia de FoodFavDetailsFragment
        val foodComentaryFragment = ComentaryFragment().apply {
            arguments = Bundle().apply {
                putString("foodId", foodId)
            }
        }

        // Realizar la transacción de fragmentos
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, foodComentaryFragment)
            .commit()

    }

    override fun onBackButtonPressedGoToDetails(foodId: String) {
        val childFragments = childFragmentManager.fragments
        childFragments.forEach { fragment ->
            childFragmentManager.beginTransaction().remove(fragment).commit()
        }

        val fragmentFoodFav = FoodFavDetailsFragment().apply {
            arguments = Bundle().apply {
                putString("foodId", foodId)
            }
            listener = this@MenuFragment
        }
        // Realiza la transacción de fragmentos
        childFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragmentFoodFav)
            .commit()
    }


}