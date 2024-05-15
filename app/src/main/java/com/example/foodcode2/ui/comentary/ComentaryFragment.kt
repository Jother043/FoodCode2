package com.example.foodcode2.ui.comentary

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodcode2.adapter.ComentaryAdapter
import com.example.foodcode2.data.UserComentary
import com.example.foodcode2.databinding.FragmentComentaryBinding
import com.example.foodcode2.ui.login.LoginVM
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class ComentaryFragment : Fragment() {

    private var _binding: FragmentComentaryBinding? = null

    val binding
        get() = _binding!!

    val args: ComentaryFragmentArgs by navArgs()

    private val comentaryVM by viewModels<ComentaryVM> { ComentaryVM.Factory }
    private val loginVM: LoginVM by viewModels<LoginVM> { LoginVM.Factory }

    private lateinit var comentsAdapter: ComentaryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentComentaryBinding.inflate(inflater, container, false)

        comentaryVM.getComentary(args.foodId)

        setListeners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecView()

        collectors()

    }

    private fun initRecView() {
        comentsAdapter = ComentaryAdapter(
            _comentaryList = mutableListOf(),
        )
        binding.rvComents.adapter = comentsAdapter
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
            binding.rvComents.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        else {
            binding.rvComents.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                comentaryVM.uiState.collect {
                    if (!it.isLoading) {
                        binding.loadingGif.visibility = View.INVISIBLE
                        comentsAdapter.setComentsList(it.comentList)
                        comentsAdapter.notifyDataSetChanged()
                    } else {
                        binding.loadingGif.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setListeners() {

        binding.btnBackComents.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnGuardarComent.setOnClickListener {

            // Si el campo de texto no está vacío, se añade el comentario
            if (binding.etIntroComent.text.isNotBlank()) {

                val comentario = UserComentary(
                    user = loginVM.userState.value.name,
                    foodId = args.foodId,
                    comentary = binding.etIntroComent.text.toString()

                )
                comentaryVM.setComentary(comentario)

                // Ocultar el teclado después de añadir el comentario
                val inputMethodManager =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

                binding.etIntroComent.text = null
                Snackbar.make(
                    requireView(),
                    "Comentario anadido correctamente",
                    Snackbar.LENGTH_SHORT
                ).show()

            } else {
                Snackbar.make(
                    requireView(),
                    "No puedes eneviar un comentario vacio",
                    Snackbar.LENGTH_SHORT
                ).show()
            }


        }
    }

}