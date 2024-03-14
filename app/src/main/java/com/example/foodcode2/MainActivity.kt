package com.example.foodcode2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodcode2.databinding.ActivityMainBinding
import com.example.foodcode2.ui.herolist.ListFragment
import com.example.foodcode2.ui.userpreferences.InfoUserFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.listFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.NavHostFragment, ListFragment())
                        .commitNow()
                }

                R.id.fragmentFoodFav -> {

                }

                R.id.infoUserFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.NavHostFragment, InfoUserFragment()).commitNow()
                }
            }
            true
        }
    }

}
