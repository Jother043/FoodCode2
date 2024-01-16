package com.example.FoodCode

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.foodcode.R

class CreditActivity : AppCompatActivity() {

    private lateinit var userVersionTextView: TextView
    private lateinit var appDescriptionTextView: TextView
    private lateinit var contactButton: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credit)

        userVersionTextView = findViewById(R.id.userVersionTextView)
        appDescriptionTextView = findViewById(R.id.appDescriptionTextView)
        contactButton = findViewById(R.id.contactButton)

        val userName = intent.getStringExtra("NAME")
        val appVersion = "1.0"
        val userDeveloper = "José Miguel Gutiérrez Hernández"

        userVersionTextView.text = "$userName, esta es la versión $appVersion de la aplicación"
        appDescriptionTextView.text = "Esta aplicación ha sido desarrollada por $userDeveloper"

        contactButton.setOnClickListener {
            developerMessage(userName ?: "", "FoodCode")
        }
    }


    //Función para enviar un correo electrónico al desarrollador.
    private fun developerMessage(userName: String, subject: String) {
        //abre gmail para enviar un mensaje cone el correo del desarrollador.
        val email = "josemgh166@gmail.com"
        val subject = "Consulta de la app $subject"
        val message = "Hola $userName, me gustaría hacerte la siguiente consulta:"

        // Crea un Intent para enviar el correo
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
        }

        // Verifica si hay aplicaciones que puedan manejar este Intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {

            //Si no hay aplicaciones que puedan manejar el Intent, muestra un mensaje en el logcat y en la pantalla.
            Log.println(Log.ERROR, "Error", "No hay aplicaciones que puedan manejar este intent")
            //sale una ventana emergente con el mensaje de error.
            Toast.makeText(
                this,
                "No hay aplicaciones que puedan manejar este intent",
                Toast.LENGTH_LONG
            ).show()

        }
    }


}