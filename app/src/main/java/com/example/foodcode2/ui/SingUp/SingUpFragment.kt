package com.example.foodcode2.ui.SingUp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentSingUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSingUpBinding.inflate(inflater, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.editTextNombre.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextContraseA.text.toString()

            // Validate the input
            if (nombre.isBlank() || email.isBlank() || password.isBlank()) {
                showAlert("Por favor, rellene todos los campos.")
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                showAlert("Por favor, introduzca un correo electrónico válido.")
            } else if (password.length < 8) {
                showAlert("La contraseña debe tener al menos 8 caracteres.")
            } else if (!password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*".toRegex())) {
                showAlert("La contraseña debe contener al menos una letra mayúscula, una letra minúscula, un número y un carácter especial.")
            } else {
                // Register the user
                registrarUsuario(nombre, email, password)
            }
        }

        return binding.root
    }

    private fun registrarUsuario(nombre: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Save the user's name
                    val user = auth.currentUser
                    user?.updateProfile(
                        UserProfileChangeRequest.Builder().setDisplayName(nombre).build()
                    )

                    // Send verification email
                    user?.sendEmailVerification()?.addOnCompleteListener { emailTask ->
                        if (emailTask.isSuccessful) {
                            showAlert("Usuario registrado correctamente. Se ha enviado un correo de verificación.")
                            findNavController().navigate(R.id.action_singUpFragment_to_loginFragment2)
                        } else {
                            showAlert("Error al enviar el correo de verificación.")
                        }
                    }
                } else {
                    showAlert("Error al registrar el usuario")
                }
            }
    }

    private fun showAlert(message: String) {
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton("Aceptar", null)
            .show()
    }
}