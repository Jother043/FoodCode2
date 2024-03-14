package com.example.foodcode2.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentLoginBinding
import com.example.foodcode2.dependencies.AppContainer
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.example.foodcode2.ui.userpreferences.InfoUserVM
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var etName: EditText
    private lateinit var userRepositories: UserRepositories
    private lateinit var infoUserVM: InfoUserVM

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val appContainer = (activity?.application as FoodCode).appContainer
        infoUserVM = ViewModelProvider(this).get(InfoUserVM::class.java)

        etName = binding.editTextName

        binding.btnSignup.setOnClickListener {
            val userName = etName.text.toString()
            if (userName.isNotEmpty()) {
                //Si el nombre no está vacío se guarda en la base de datos si el
                // checkbox no esta seleccionado se navega a la pantalla noticias si no a la pantalla de menu
                lifecycleScope.launch {
                    infoUserVM.saveUserName(userName)
                    val userPreferences = infoUserVM.getUserPreferences()
                    val showViewPage = userPreferences["showViewPage"] as? Boolean
                    if (showViewPage == true) {
                        val action = LoginFragmentDirections.actionLoginFragment2ToMenuFragment()
                        findNavController().navigate(action)
                    } else {
                        val action = LoginFragmentDirections.actionLoginFragment2ToNoticeFragment()
                        findNavController().navigate(action)
                    }
                }
            } else {
                Snackbar.make(
                    binding.root, "El nombre no puede estar vacío", Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        val navBar: BottomNavigationView? = activity?.findViewById(R.id.bottom_navigation)
        navBar?.visibility = View.GONE

        return binding.root
    }
}