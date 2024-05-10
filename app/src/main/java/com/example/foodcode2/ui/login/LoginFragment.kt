package com.example.foodcode2.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
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
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.auth.api.signin.GoogleSignIn

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

        binding.btnImageGoogle.setOnClickListener {
            googleSignIn()
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

    private fun googleSignIn() {

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("441853910501-p5g3igsqtbhi8uk1cp7ui1smalqg3mah.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Inicio de sesión con Google exitoso, autenticamos con Firebase
                val account = task.getResult(ApiException::class.java)!!
                loginVM.firebaseAuthWithGoogle(account.idToken!!)
                //Nos movemos a la siguiente pantalla de menú
                val action = LoginFragmentDirections.actionLoginFragment2ToMenuFragment4("")
                findNavController().navigate(action)
            } catch (e: ApiException) {
                Snackbar.make(
                    requireView(),
                    "Error al iniciar sesión con Google",
                    Snackbar.LENGTH_SHORT
                ).show()

                Log.d("LoginFragment", "Google sign in failed", e)

                //Si no hay conexión a internet, mostramos un mensaje de error
                if (e.message == "7: ") {
                    Snackbar.make(
                        requireView(),
                        "Error de red. Por favor, verifica tu conexión a Internet.",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}