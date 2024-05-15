package com.example.foodcode2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.databinding.FragmentCreditBinding

class CreditFragment : Fragment() {

    private var _binding: FragmentCreditBinding? = null
    private val binding
        get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreditBinding.inflate(layoutInflater, container, false)

        //Cambio el título del toolbar al de la página actual
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.credits)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtengo una referencia al MainActivity y vinculo el botón a una constante
        val mainActivity = activity as MainActivity


        // Configuro el clic del botón para llamar a la función en MainActivity
        binding.buttonContact.setOnClickListener {
            mainActivity.createMail()
        }

        binding.btnBack.setOnClickListener{
            findNavController().popBackStack()
        }
    }

}