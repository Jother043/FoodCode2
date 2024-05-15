package com.example.foodcode2.ui.login

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.databinding.FragmentLoginBinding
import com.example.foodcode2.ui.SingUp.SingUpFragmentDirections
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlin.contracts.contract

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginVM: LoginVM by viewModels<LoginVM> { LoginVM.Factory }

    private lateinit var auth: FirebaseAuth

    private lateinit var mGoogleSignIn: GoogleSignInClient

    var skipWelcome = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        auth = FirebaseAuth.getInstance()


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
                        when {
                            uiState.isLoggedIn -> {
                                withContext(Dispatchers.Main) {
                                    MaterialAlertDialogBuilder(requireContext())
                                        .setTitle("Inicio de sesión")
                                        .setMessage("Inicio de sesión exitoso")
                                        .setPositiveButton("Aceptar") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    //nos movemos a la siguiente pantalla
                                    val action =
                                        LoginFragmentDirections.actionLoginFragment2ToMenuFragment4(
                                            ""
                                        )
                                    val navController = findNavController()
                                    //si la acción es válida navegamos
                                    if (navController.currentDestination?.getAction(action.actionId) != null) {
                                        navController.navigate(action)
                                    } else {
                                        //Mostar mensaje de error
                                    }
                                }
                            }

                            uiState.errorMessage.isNotEmpty() -> {
                                withContext(Dispatchers.Main) {
                                    MaterialAlertDialogBuilder(requireContext())
                                        .setTitle("Error")
                                        .setMessage(uiState.errorMessage)
                                        .setPositiveButton("Aceptar") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                }
                            }

                            uiState.isSucefullMessage.isNotEmpty() -> {
                                withContext(Dispatchers.Main) {
                                    MaterialAlertDialogBuilder(
                                        requireContext(),
                                        R.style.RoundedAlertDialog
                                    )
                                        .setTitle("Recuperación de contraseña")
                                        .setMessage(uiState.isSucefullMessage)
                                        .setPositiveButton("Aceptar") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                }
                            }

                            uiState.anonymous -> {
                                withContext(Dispatchers.Main) {
                                    MaterialAlertDialogBuilder(
                                        requireContext(),
                                        R.style.RoundedAlertDialog
                                    )
                                        .setTitle("Inicio de sesión")
                                        .setMessage("Inicio de sesión anónimo exitoso")
                                        .setPositiveButton("Aceptar") { dialog, _ ->
                                            dialog.dismiss()
                                        }
                                        .show()
                                    //nos movemos a la siguiente pantalla
                                    val action =
                                        LoginFragmentDirections.actionLoginFragment2ToMenuFragment4(
                                            ""
                                        )
                                    val navController = findNavController()
                                    //si la acción es válida navegamos
                                    if (navController.currentDestination?.getAction(action.actionId) != null) {
                                        navController.navigate(action)
                                    } else {
                                        //Mostar mensaje de error
                                    }
                                }
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showAlertError("Error al iniciar sesión")
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
            signIn()
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
                    showAlertError("No hay conexión a internet")
                }
            }
        }

        binding.tvResgistrarse.setOnClickListener {
            recoverPassword()
        }

        binding.btnImageAnonimo.setOnClickListener {
            if (loginVM.isNetworkAvailable(requireContext())) {
                lifecycleScope.launch(Dispatchers.IO) {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        loginVM.anonymous()
                    }
                }
            } else {
                showAlertError("No hay conexión a internet")
            }
        }
    }

    //funcion que se encargar de mostrar material dialog
    private fun recoverPassword() {
        val inflater = layoutInflater
        val dialogView: View = inflater.inflate(R.layout.dialog_input, null)
        val input: EditText = dialogView.findViewById(R.id.dialog_input)

        MaterialAlertDialogBuilder(requireContext(), R.style.RoundedAlertDialog)
            .setTitle("Introzca su correo electrónico para recuperar su contraseña")
            .setView(dialogView)
            .setPositiveButton("Aceptar") { dialog, _ ->
                val userInput = input.text.toString()
                if (userInput.isNotEmpty()) {
                    //Navegar a la siguiente pantalla
                    lifecycleScope.launch(Dispatchers.IO) {
                        repeatOnLifecycle(Lifecycle.State.STARTED) {
                            loginVM.recuperarContrasena(userInput)
                        }
                    }
                } else {
                    showAlertError("Por favor, introduzca su correo electrónico")
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAlertError(message: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.RoundedAlertDialog)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    if (user != null) {
                        val action =
                            LoginFragmentDirections.actionLoginFragment2ToMenuFragment4(user.displayName.toString())
                        findNavController().navigate(action)
                    } else {
                        showAlertError("Error al iniciar sesión")
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showAlertError("Error al iniciar sesión")
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

}