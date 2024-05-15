package com.example.foodcode2.ui.noticias

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.foodcode2.R
import com.example.foodcode2.databinding.FragmentTab2NoticeBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class Tab2NoticeFragment : Fragment() {

    private var _binding: FragmentTab2NoticeBinding? = null
    private val binding
        get() = _binding!!

    private val noticeVM: Tab2NoticeVM by viewModels<Tab2NoticeVM> { Tab2NoticeVM.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    /**
     * Esta funcion se encarga de manejar los eventos de los botones de la vista
     */
    private fun setListeners() {
        binding.btnBack.setOnClickListener {
            noticeVM.saveSettingsWelcome(binding.checkBox.isChecked)
        }
    }


    private fun collectors() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                noticeVM.uiState.collect {
                    if (it.saveShowViewPage) {

                        val action = NoticeFragmentDirections.actionNoticeFragmentToMenuFragment("")
                        findNavController().navigate(action)
                    }
                    if (it.error) {
                        Snackbar.make(
                            requireView(),
                            getString(R.string.errorSave),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentTab2NoticeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.acceder)
        setListeners()
        collectors()
        return binding.root
    }

}