package com.example.foodcode2.ui.userpreferences

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import coil.transform.CircleCropTransformation
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentInfoUserBinding
import com.example.foodcode2.repositories.ProductRepository
import com.example.foodcode2.repositories.UserRepositories
import com.example.foodcode2.ui.login.LoginVM
import com.example.foodcode2.ui.menu.MenuFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class InfoUserFragment(private val userRepositories: UserRepositories) : Fragment() {


    private lateinit var binding: FragmentInfoUserBinding

    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    private val infoUserVM: InfoUserVM by viewModels<InfoUserVM> { InfoUserVM.Factory }


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
                userRepositories.obtenerUsuario()
                infoUserVM.userState.collect {
                    val user = firebaseAuth.currentUser
                    val photoUrl = user?.photoUrl
                    Log.d("url", "url: $photoUrl")
                    binding.tvUser.text = "Tu username es: ${it.name}"
                    binding.tvEmail.text = "Correo:  ${it.email}"
                    if (photoUrl != null) {
                        binding.ivPhoto.load(photoUrl) {
                            transformations(CircleCropTransformation())
                        }
                    } else {
                        binding.ivPhoto.load(R.drawable.ic_profile) {
                            transformations(CircleCropTransformation())
                        }
                    }
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
                    .requestIdToken("441853910501-nm493gdu3vbtsg6f1ee7n5chv5s4t07b.apps.googleusercontent.com")
                    .requestEmail()
                    .build()
            // Google sign out
            val googleSignInClient =
                GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
            googleSignInClient.signOut().addOnCompleteListener {
                //reinicio de la app
                val intent = requireActivity().intent
                requireActivity().finish()
                startActivity(intent)
            }

        }

        binding.volver.setOnClickListener {
            (parentFragment as? MenuFragment)?.onBackButtonPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        infoUserVM.obtenerUsuario()
    }
}
