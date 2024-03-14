package com.example.foodcode2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.databinding.FragmentMenuBinding
import com.example.foodcode2.ui.login.LoginFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        binding = FragmentMenuBinding.inflate(layoutInflater)

        binding.btnList.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_listFragment)
        }

        binding.btnFavList.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_fragmentFoodFav)
        }


        binding.btnExitMenu.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_loginFragment2)
        }

        view?.let { super.onViewCreated(it, savedInstanceState) }
        val navBar: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation)
        navBar?.visibility = View.VISIBLE

        return binding.root
    }

}