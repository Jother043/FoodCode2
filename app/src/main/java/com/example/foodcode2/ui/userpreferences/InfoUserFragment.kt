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
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.data.UserPreferences
import com.example.foodcode2.dependencies.FoodCode
import com.example.foodcode2.repositories.UserRepositories
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InfoUserFragment : Fragment() {

    private lateinit var userNameTextView: TextView
    private lateinit var userCheckbox: CheckBox
    private lateinit var saveButton: Button
    private lateinit var userRepositories: UserRepositories

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_user, container, false)

        userNameTextView = view.findViewById(R.id.tvNombre)
        userCheckbox = view.findViewById(R.id.checkBox2)
        saveButton = view.findViewById(R.id.buttonLogOut)

        // En InfoUserFragment.kt
        lifecycleScope.launch {
            val userName = userRepositories.getUserName().first()
            if (userName.isEmpty() || userName == UserPreferences.ANONYMOUS) {
                Log.d("InfoUserFragment", "No se encontró ningún nombre de usuario.")
                userNameTextView.text = "Usuario desconocido"
            } else {
                Log.d("InfoUserFragment", "El nombre es: ${userName}")
                userNameTextView.text = userName
            }
        }

        saveButton.setOnClickListener {
            lifecycleScope.launch {
                userRepositories.saveSettings("name", userCheckbox.isChecked)
                //Navegamos al fragmento de login
                findNavController().navigate(R.id.action_infoUserFragment_to_loginFragment2)
            }
        }
        return view
    }
}