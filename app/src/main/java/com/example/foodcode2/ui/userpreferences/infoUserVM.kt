package com.example.foodcode2.ui.userpreferences

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel

class InfoUserVM(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    //Guarda el nombre del usuario en las preferencias compartidas.
    fun saveUserName(name: String) {
        sharedPreferences.edit().putString("user_name", name).apply()
    }

    fun getUserName(): String {
        return sharedPreferences.getString("user_name", "") ?: ""
        Log.d("InfoUserFragment", "El nombre es: ${sharedPreferences.getString("user_name", "")}")
    }

    fun saveCheckboxState(state: Boolean) {
        sharedPreferences.edit().putBoolean("checkbox_state", state).apply()
    }

    fun getCheckboxState(): Boolean {
        return sharedPreferences.getBoolean("checkbox_state", false)
    }

    fun saveSettings(key: String, value: Any) {
        when (value) {
            is String -> sharedPreferences.edit().putString(key, value).apply()
            is Boolean -> sharedPreferences.edit().putBoolean(key, value).apply()
        }
    }

    fun getUserPreferences(): Map<String, *> {
        return sharedPreferences.all
    }


}