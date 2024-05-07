package com.example.foodcode2.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentUserSettingsBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class UserSettingsFragment : Fragment() {

    private lateinit var binding: FragmentUserSettingsBinding

    private val settingsVM: UserSettingsVM by viewModels<UserSettingsVM> { UserSettingsVM.Factory }

    var showViewPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUserSettingsBinding.inflate(layoutInflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCollectors()
        setListeners()

    }

    private fun setCollectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                settingsVM.uiState.collect { user ->
                    binding.etUserName.setText(settingsVM.uiState.value.name)
                    showViewPage = user.showViewPage
                    binding.cbSkipWelcomeSettings.isChecked = user.showViewPage
                }
            }
        }
    }

    private fun setListeners() {
        binding.btnBackSettings.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.btnSaveSettings.setOnClickListener {
            showViewPage = binding.cbSkipWelcomeSettings.isChecked
            validateName(binding.etUserName.text.toString())
            Snackbar.make(requireView(),getString(R.string.settingsSave), Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun validateName(name: String) {
        if(name.isBlank())
            Snackbar.make(requireView(),getString(R.string.emptyname),Snackbar.LENGTH_SHORT).show()
        else
            settingsVM.saveSettings(name,showViewPage)
    }


}