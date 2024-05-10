package com.example.foodcode2.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentLoginBinding
import com.example.foodcode2.dependencies.FoodCode
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginVM: LoginVM by viewModels<LoginVM> { LoginVM.Factory }

    var skipWelcome = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)


        return binding.root
    }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectors()
        setListeners()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    loginVM.userState.collect { uiState ->
                        if (uiState.isLoggedIn) {
                            withContext(Dispatchers.Main) {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Inicio de sesión")
                                    .setMessage("Inicio de sesión exitoso")
                                    .setPositiveButton("Aceptar") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                                //nos movemos a la siguiente pantalla
                                val action =
                                    LoginFragmentDirections.actionLoginFragment2ToMenuFragment4("")
                                val navController = findNavController()
                                //si la acción es válida navegamos
                                if (navController.currentDestination?.getAction(action.actionId) != null) {
                                    navController.navigate(action)
                                } else {
                                    //Mostar mensaje de error
                                }
                            }
                        } else if (uiState.errorMessage.isNotEmpty()) {
                            withContext(Dispatchers.Main) {
                                AlertDialog.Builder(requireContext())
                                    .setTitle("Inicio de sesión")
                                    .setMessage(uiState.errorMessage)
                                    .setPositiveButton("Aceptar") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .show()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    AlertDialog.Builder(requireContext())
                        .setTitle("Inicio de sesión")
                        .setMessage("Error al iniciar sesión")
                        .setPositiveButton("Aceptar") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.userState.collect { uiState ->


                    // Si el usuario no ha iniciado sesión, configura los listeners y recolecta los datos de la vista
                    collectors()
                    setListeners()

                }
            }
        }
    }

    private fun navigateToMainScreen() {
        val action = LoginFragmentDirections.actionLoginFragment2ToMenuFragment4("")
        val navController = findNavController()
        if (navController.currentDestination?.getAction(action.actionId) != null) {
            navController.navigate(action)
        }
    }

    //Función que se encarga de recolectar los datos de la vista
    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.userState.collect {
                    skipWelcome = it.showViewPage
                }
            }
        }
    }

    //Función que se encarga de añadir los listeners a los botones
    private fun setListeners() {

        //TODO: Implementar la funcionalidad de los botones de inicio de sesión.

        binding.btnImageFacebook.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.facebook), Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.btnImageGoogle.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.google), Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.btnImageGit.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.github), Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.tvContraseAOlvidada.setOnClickListener {

            val action = LoginFragmentDirections.actionLoginFragment2ToSingUpFragment()
            findNavController().navigate(action)

            //Limpiar los campos de texto
            binding.editTextName.text?.clear()
            binding.editTextContraseA.text?.clear()
        }

        binding.btnSignup.setOnClickListener {

            val email = binding.editTextName.text.toString()
            val password = binding.editTextContraseA.text.toString()

            if (loginVM.validateName(email) && loginVM.validatePassword(password)) {
                if (loginVM.isNetworkAvailable(requireContext())) {
                    loginVM.signInWithFirebase(email, password)
                } else {
                    Snackbar.make(
                        requireView(),
                        "Error de red. Por favor, verifica tu conexión a Internet.",
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }
}