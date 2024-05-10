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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
            //cerrar la sesion de google si esta iniciada
            if (firebaseAuth.currentUser != null) {
                FirebaseAuth.getInstance().signOut()
            }
            //Buscar la cuenta de google
            val googleSignInOptions =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken("441853910501-p5g3igsqtbhi8uk1cp7ui1smalqg3mah.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            // Google sign out
            val googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
            googleSignInClient.signOut().addOnCompleteListener {
                //reinicio de la app
                val intent = requireActivity().intent
                requireActivity().finish()
                startActivity(intent)
            }

        }

        binding.fabSettings.setOnClickListener {
            findNavController().navigate(R.id.action_infoUserFragment_to_userSettingsFragment)
        }
    }
}