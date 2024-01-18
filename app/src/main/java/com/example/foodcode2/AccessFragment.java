package com.example.foodcode2;

import android.os.Bundle
import android.content.Intent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.seriessphere.databinding.FragmentAccessBinding


class AccessFragment : Fragment() {

private lateinit var binding: FragmentAccessBinding
        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
        }

        override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {

        binding = FragmentAccessBinding.inflate(layoutInflater)


        //Cogemos el nombre del usuario de la pantalla anterior y lo mostramos en la pantalla de acceso
        val name = arguments?.getString(LoginFragment.NAME)
        binding.etUsername.text = name


        binding.btnList.setOnClickListener {
        findNavController().navigate(R.id.action_accessFragment_to_seriesListFragment)
        }

        binding.btnFavList.setOnClickListener {
        findNavController().navigate(R.id.action_accessFragment_to_seriesFavListFragment)
        }

        binding.btnExitMenu.setOnClickListener {
        findNavController().navigate(R.id.action_accessFragment_to_loginFragment)
        }

        return binding.root
        }

        }
