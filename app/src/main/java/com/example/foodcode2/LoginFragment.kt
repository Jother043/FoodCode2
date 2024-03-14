package com.example.foodcode2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar

class LoginFragment : Fragment() {

    //Create a binding object so that we can access views in the fragment_login.xml layout.
    private lateinit var binding: FragmentLoginBinding
    private lateinit var etName: EditText

    /**
     * Llamamos a la función onCreateView() y le pasamos los parámetros habituales.
     * En el cuerpo de la función, creamos una variable binding que hace referencia a la clase
     * FragmentLoginBinding y la inicializamos con el método inflate() que se genera a partir del
     * nombre del layout fragment_login.xml.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btnSignup.setOnClickListener {
            if (etName.text.toString().isNotEmpty()) {
                //findNavController().navigate(R.id.action_loginFragment2_to_menuFragment)
                findNavController().navigate(R.id.action_loginFragment2_to_noticeFragment)
            } else {
                Snackbar.make(
                    binding.root,
                    "El nombre no puede estar vacío",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        //Hacemos invisible la barra de navegación.
        view?.let { super.onViewCreated(it, savedInstanceState) }
        val navBar: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation)
        navBar?.visibility = View.GONE


        etName = binding.editTextName
        val name = etName.text.toString()
        goToElection()

        return binding.root
    }

    private fun goToElection() {
        if (etName.text.toString().isNotEmpty()) {
            findNavController().navigate(R.id.action_loginFragment2_to_menuFragment)
        } else {
            if (etName.text.toString().isEmpty() && binding.btnSignup.isPressed)
                Snackbar.make(
                    binding.root,
                    "El nombre no puede estar vacío",
                    Snackbar.LENGTH_SHORT
                ).show()
        }
    }

    companion object {
        const val NAME = "name"
    }

}