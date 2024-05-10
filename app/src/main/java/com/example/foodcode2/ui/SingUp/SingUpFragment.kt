package com.example.foodcode2.ui.SingUp

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.databinding.FragmentSingUpBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding
    private val SingUpViewModel: SingUpVM by viewModels<SingUpVM> { SingUpVM.Factory }

    @SuppressLint("UnsafeRepeatOnLifecycleDetector")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSingUpBinding.inflate(inflater, container, false)

        //Binding del boton de registro
        binding.btnRegistrar.setOnClickListener {

            //Obtiene los valores de los campos
            val nombre = binding.editTextNombre.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextContraseA.text.toString()

            // Validamos los campos
            if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                showAlert("Por favor, rellene todos los campos.")
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showAlert("Por favor, introduzca un correo electrónico válido.")
            } else if (password.length < 8) {
                showAlert("La contraseña debe tener al menos 8 caracteres.")
            } else if (!password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*".toRegex())) {
                showAlert("La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.")
            } else {
                // registra al usuario si hay conexión a internet
                if (SingUpViewModel.isNetworkAvailable(requireContext()) ) {
                    SingUpViewModel.signUpUser(nombre, email, password)
                } else {
                    Snackbar.make(
                        binding.root,
                        "No hay conexión a internet",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }

        /**
         * Observa el estado del usuario.
         */
        lifecycleScope.launch(Dispatchers.Main) {
                lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    SingUpViewModel.userStateSingUp.collect { userPreferences ->
                        if (userPreferences.errorMessage.isNotBlank() && userPreferences.errorMessage.isNotEmpty()) {
                            Snackbar.make(
                                binding.root,
                                userPreferences.errorMessage,
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                        if (userPreferences.isRegistered) {
                            //Vacia los campos
                            binding.editTextNombre.text!!.clear()
                            binding.editTextEmail.text!!.clear()
                            binding.editTextContraseA.text!!.clear()
                            //Notifica al usuario
                            Snackbar.make(
                                binding.root,
                                "Te has registrado correctamente",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            //Navega al fragmento de inicio de sesión
                            val action =
                                SingUpFragmentDirections.actionSingUpFragmentToLoginFragment2()
                            findNavController().navigate(action)
                        }
                    }
                }

        }

        return binding.root
    }


    /**
     * Muestra un cuadro de diálogo con un mensaje.
     */
    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }

}