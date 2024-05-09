package com.example.foodcode2.ui.userpreferences

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
import com.example.foodcode2.databinding.FragmentInfoUserBinding
import com.example.foodcode2.ui.login.LoginVM
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class InfoUserFragment : Fragment() {

    private lateinit var binding: FragmentInfoUserBinding

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

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
                loginVM.userState.collect {
                    binding.tvUser.text = it.name
                }
            }
        }
    }

    private fun setListerners() {

        binding.buttonLogOut.setOnClickListener {
            firebaseAuth.signOut()
            // Navigate to the login fragment
            findNavController().popBackStack(R.id.loginFragment2, true)
        }

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_infoUserFragment_to_userSettingsFragment)
        }
    }
}