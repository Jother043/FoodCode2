package com.example.foodcode2

import android.content.Intent
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

    }

    fun createMail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "plain/text"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.gmail)))
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.answers))
        startActivity(Intent.createChooser(intent, ""))
    }

}
