package com.example.foodcode2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.foodcode2.databinding.FragmentNoticeBinding

import com.google.android.material.tabs.TabLayoutMediator

class NoticeFragment : Fragment() {

    private var _binding: FragmentNoticeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding  = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vpNotice.adapter = NoticeAdapter(this)


        TabLayoutMediator(binding.tabNotice, binding.vpNotice) { tab, position ->
            tab.text = when(position) {
               0 -> "Novedades"
                else -> "Créditos"
            }
        }.attach()
    }
}

class NoticeAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        val fragment = if (position == 0)
            Tab1NoticeFragment()
        else
            Tab2NoticeFragment()

        return fragment
    }
}