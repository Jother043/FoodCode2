package com.example.foodcode2.ui.userpreferences

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.databinding.FragmentInfoUserBinding
import com.example.foodcode2.databinding.FragmentListBinding
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import com.example.foodcode2.ui.login.LoginVM
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InfoUserFragment : Fragment() {

    private lateinit var binding: FragmentInfoUserBinding

    private val loginVM: LoginVM by viewModels<LoginVM> { LoginVM.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentInfoUserBinding.inflate(layoutInflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.username)

        setListerners()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollectors()
    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                loginVM.uiState.collect {
                    binding.tvUser.text = it.name
                }
            }
        }
    }

    private fun setListerners() {

        binding.buttonLogOut.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_infoUserFragment_to_userSettingsFragment)
        }
    }
}