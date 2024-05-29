package com.example.foodcode2.repositories

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodcode2.data.UserPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class UserRepositories(
    private val userDataStore: DataStore<Preferences>,

    ) {
    private val data = FirebaseDatabase.getInstance()
    private val auth = FirebaseAuth.getInstance()

    //Obtener las preferencias del usuario.
    fun getUserPrefs(): Flow<UserPreferences> {
        return userDataStore.data.map { userPreferences ->
            val name = userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)]
                ?: UserPreferences.ANONYMOUS
            val checked = userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)]
                ?: UserPreferences.SHOW_VIEW_PAGE_DEFAULT
            val checked2 =
                userPreferences[booleanPreferencesKey(UserPreferences.IS_LOGGED_IN_DEFAULT)]
                    ?: UserPreferences.IS_LOGGED_IN
            return@map UserPreferences(
                name = name,
                showViewPage = checked,
                isLoggedIn = checked2
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun obtenerUsuario() {
        val user = auth.currentUser
        val uid = user?.uid
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        GlobalScope.launch {
            if (user!!.displayName != null) {
                // Si el nombre de usuario de la cuenta de Google no es nulo, úsalo
                val name = user.displayName.toString()
                saveUserPreferences(name, userEmail.toString())
                UserPreferences.ANONYMOUS = userEmail.toString()
                Log.d("User", "User: $name y email: $userEmail")
                Log.d("User", "Estoy dentro de obtenerUsuario()")
                if (user.displayName!!.isBlank()) {
                    data.reference.child("Users").child(uid!!)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val name = snapshot.child("name").value.toString()
                                val email = snapshot.child("email").value.toString()
                                UserPreferences.ANONYMOUS = email
                                saveUserPreferences(name, email)
                                Log.d("User", "User: $name y email: $userEmail")
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e("Error", error.message)
                            }
                        })
                }
            } else {
                // Si el nombre de usuario de la cuenta de Google es nulo, obtén el nombre del usuario de la base de datos en tiempo real de Firebase
                data.reference.child("Users").child(uid!!)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            val name = snapshot.child("name").value.toString()
                            val email = snapshot.child("email").value.toString()
                            UserPreferences.ANONYMOUS = email
                            saveUserPreferences(name, email)
                            Log.d("User", "User: $name y email: $userEmail")
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e("Error", error.message)
                        }
                    })
            }
        }
    }

    fun saveUserPreferences(name: String, email: String) {
        GlobalScope.launch {
            // Guardar los datos del usuario en las preferencias.
            userDataStore.edit { userPreferences ->
                userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)] = name
                userPreferences[stringPreferencesKey(UserPreferences.EMAIL)] = email
            }
        }
    }


    //Guardar las preferencias del usuario.
    suspend fun saveSettings(name: String, checked: Boolean, saveChecked: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)] = name
            userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)] = checked
        }
    }


    //Guardar la preferencia de mostrar la vista de la página.
    suspend fun saveSettingsViewPage(checked: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)] = checked
        }
    }


}