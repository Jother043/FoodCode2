package com.example.foodcode2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val loginVM: LoginVM by viewModels<LoginVM> { LoginVM.Factory }

    var skipWelcome = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectors()
        setListeners()

    }

    //Función que se encarga de recolectar los datos de la vista
    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.uiState.collect {
                    skipWelcome = it.showViewPage
                }
            }
        }
    }

    //Función que se encarga de añadir los listeners a los botones
    private fun setListeners() {
        binding.btnSignup.setOnClickListener {
            val name = binding.editTextName.text.toString()
            validateName(name)
        }

        //TODO: Implementar la funcionalidad de los botones de inicio de sesión.

        binding.btnImageFacebook.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.facebook), Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.btnImageGoogle.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.google), Snackbar.LENGTH_SHORT)
                .show()
        }

        binding.btnImageGit.setOnClickListener {
            Snackbar.make(requireView(), getString(R.string.github), Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    //Función que se encarga de validar el nombre ingresado por el usuario
    private fun validateName(name: String) {
        if (name.isBlank()) {
            Snackbar.make(requireView(), getString(R.string.emptyname), Snackbar.LENGTH_SHORT)
                .show()
        } else {
            loginVM.saveSettings(name, skipWelcome)
            if (skipWelcome) {
                val action = LoginFragmentDirections.actionLoginFragment2ToMenuFragment4("")
                findNavController().navigate(action)
            } else {
                val action = LoginFragmentDirections.actionLoginFragment2ToNoticeFragment()
                findNavController().navigate(action)
            }
        }

    }
}