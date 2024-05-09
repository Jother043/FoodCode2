package com.example.foodcode2.ui.SingUp

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.data.User
import com.example.foodcode2.databinding.FragmentSingUpBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database


class SingUpFragment : Fragment() {

    private lateinit var binding: FragmentSingUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                if (user != null) {
                    val User = User(nombre, email, password, false)

                    // Obtiene la referencia a la base de datos
                    val database = FirebaseDatabase.getInstance().getReference("Users")

                    // Guarda los datos del usuario en la base de datos
                    database.child(user.uid).setValue(User).addOnSuccessListener {
                        binding.editTextNombre.text?.clear()
                        binding.editTextEmail.text?.clear()
                        binding.editTextContraseA.text?.clear()

                        // Enviar correo de verificación
                        user.sendEmailVerification().addOnCompleteListener { verTask ->
                            if (verTask.isSuccessful) {
                                Snackbar.make(
                                    binding.root,
                                    "Usuario registrado correctamente. Por favor verifica tu correo electrónico.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            } else {
                                Snackbar.make(
                                    binding.root,
                                    "Error al enviar el correo de verificación.",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                        //volver a la pantalla de inicio de sesión
                        findNavController().popBackStack()

                    }.addOnFailureListener {
                        if (it.message == "Initial task failed for action RecaptchaAction(action=signUpPassword)with exception - The email address is already in use by another account.") {
                            showAlert("El correo electrónico ya está en uso.")
                        } else {
                            showAlert("Error al registrar el usuario.")
                        }
                    }
                }
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