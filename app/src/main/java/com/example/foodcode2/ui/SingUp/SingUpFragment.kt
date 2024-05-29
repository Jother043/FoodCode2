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
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentSingUpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        binding = FragmentSingUpBinding.inflate(inflater, container, false)

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextContraseA.text.toString()

            if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                showAlert("Por favor, rellene todos los campos.")
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showAlert("Por favor, introduzca un correo electrónico válido.")
            } else if (password.length < 8) {
                showAlert("La contraseña debe tener al menos 8 caracteres.")
            } else if (!password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.]).*".toRegex())) {
                showAlert("La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.")
            } else {
                if (SingUpViewModel.isNetworkAvailable(requireContext())) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            SingUpViewModel.signUpUser(nombre, email, password)
                        }
                    }
                } else {
                    Snackbar.make(binding.root, "No hay conexión a internet", Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }

        lifecycleScope.launch(Dispatchers.Main) {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                SingUpViewModel.userStateSingUp.collect { userPreferences ->
                    if (userPreferences.errorMessage.isNotBlank() && userPreferences.errorMessage.isNotEmpty()) {
                        showAlert(userPreferences.errorMessage)
                    }
                    if (userPreferences.isRegistered) {
                        binding.editTextNombre.text!!.clear()
                        binding.editTextEmail.text!!.clear()
                        binding.editTextContraseA.text!!.clear()
                        Snackbar.make(
                            binding.root,
                            "Te has registrado correctamente. Verifica tu correo electrónico.",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        SingUpViewModel.sendEmailVerification()
                        val action = SingUpFragmentDirections.actionSingUpFragmentToLoginFragment2()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        return binding.root
    }

    private fun showAlert(message: String) {
        MaterialAlertDialogBuilder(requireContext(), R.style.RoundedAlertDialog)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}
