package com.example.foodcode2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.foodcode2.databinding.ActivityMainBinding
import com.example.foodcode2.ui.login.LoginFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Creamos la estancia de la clase de Firebase
        val currentUser = FirebaseAuth.getInstance().currentUser
        //Si el usuario ya esta logueado lo mandamos al menú
        if (currentUser != null) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.NavHostFragment) as NavHostFragment
            val navController = navHostFragment.navController
            val action = LoginFragmentDirections.actionLoginFragment2ToMenuFragment4("")
            navController.navigate(action)
        }
    }

    fun createMail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.gmail)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.answers))
        startActivity(Intent.createChooser(intent, ""))
    }

}
