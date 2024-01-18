package com.example.foodcode2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    //Create a binding object so that we can access views in the fragment_login.xml layout.
    private lateinit var binding: FragmentLoginBinding

    /**
     * Llamamos a la función onCreateView() y le pasamos los parámetros habituales.
     * En el cuerpo de la función, creamos una variable binding que hace referencia a la clase
     * FragmentLoginBinding y la inicializamos con el método inflate() que se genera a partir del
     * nombre del layout fragment_login.xml.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.btnSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment2_to_menuFragment)
        }

        return binding.root
    }

}