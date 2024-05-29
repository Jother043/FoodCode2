package com.example.foodcode2.ui.comentary

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
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
import com.example.foodcode2.R
import com.example.foodcode2.adapter.ComentaryAdapter
import com.example.foodcode2.data.UserComentary
import com.example.foodcode2.databinding.FragmentComentaryBinding
import com.example.foodcode2.ui.fav_details.OnBackButtonPressed
import com.example.foodcode2.ui.login.LoginVM
import com.example.foodcode2.ui.menu.MenuFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch

interface OnBackButtonPressedComentary {
    fun onBackButtonPressedGoToDetails(foodId : String)
}

class ComentaryFragment : Fragment() {

    private var _binding: FragmentComentaryBinding? = null
    private val firebaseUser = Firebase.auth.currentUser
    private var vibrator: Vibrator? = null

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

        vibrator = requireActivity().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
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

        binding.toolbarDetail.setNavigationOnClickListener {
            (parentFragment as? MenuFragment)?.onBackButtonPressedGoToDetails(args.foodId)
        }

        binding.btnGuardarComent.setOnClickListener {

            vibrator?.vibrate(VibrationEffect.createOneShot(120, 30))

            // Si el campo de texto no está vacío, se añade el comentario
            if (binding.etIntroComent.text.isNotBlank()) {
                lifecycleScope.launch {
                    try {
                        val user: String
                        val coment: UserComentary
                        if (firebaseUser!!.displayName!!.isBlank()) {
                            user = comentaryVM.obtenerUsuario() ?: "Anónimo"
                            coment = UserComentary(
                                user = user,
                                id = firebaseUser.uid ?: "",
                                foodId = args.foodId,
                                comentary = binding.etIntroComent.text.toString()
                            )
                        } else {
                            coment = UserComentary(
                                user = firebaseUser.displayName ?: "Anónimo",
                                id = firebaseUser.uid ?: "",
                                foodId = args.foodId,
                                comentary = binding.etIntroComent.text.toString()
                            )
                        }

                        comentaryVM.setComentary(coment)

                        // Se limpia el campo de texto
                        binding.etIntroComent.text.clear()

                        // Se oculta el teclado
                        val imm =
                            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(binding.etIntroComent.windowToken, 0)
                    } catch (e: Exception) {
                        // Manejar la excepción aquí
                        Log.e("Error", e.message ?: "Error desconocido")
                    }
                }
            }
        }

    }
}