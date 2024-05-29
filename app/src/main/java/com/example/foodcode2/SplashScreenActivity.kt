package com.example.foodcode2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.example.foodcode2.MainActivity
import com.example.foodcode2.R

class SplashScreenActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimation)
        lottieAnimationView.setAnimation("Animation - 1716276187103.json") // Reemplace '1716274730886.json' con el nombre de su archivo Lottie
        lottieAnimationView.playAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000) // Cambie este valor para controlar la duración de la pantalla de inicio
    }
}