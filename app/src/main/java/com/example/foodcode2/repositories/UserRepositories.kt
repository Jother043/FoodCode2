package com.example.foodcode2.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodcode2.data.UserPreferences
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositories(
    private val userDataStore: DataStore<Preferences>,
    private val database: FirebaseDatabase
) {

    //Obtener las preferencias del usuario.
    fun getUserPrefs(): Flow<UserPreferences> {
        return userDataStore.data.map { userPreferences ->
            val name = userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)]
                ?: UserPreferences.ANONYMOUS
            val checked = userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)]
                ?: UserPreferences.SHOW_VIEW_PAGE_DEFAULT
            val checked2 = userPreferences[booleanPreferencesKey(UserPreferences.IS_LOGGED_IN_DEFAULT)]
                ?: UserPreferences.IS_LOGGED_IN
            return@map UserPreferences(
                name = name,
                showViewPage = checked,
                isLoggedIn = checked2
            )
        }
    }

    //Guardar las preferencias del usuario.
    suspend fun saveSettings(name: String, checked: Boolean, saveChecked: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)] = name
            userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)] = checked
        }
    }

    //Guardar el nombre del usuario.
    suspend fun saveName(name: String) {
        userDataStore.edit { userPreferences ->
            userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)] = name
        }
    }

    //Guardar la preferencia de mostrar la vista de la página.
    suspend fun saveSettingsViewPage(checked: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)] = checked
        }
    }

    //Guardar el estado de inicio de sesión.
    suspend fun saveUserPrefs(checked: Boolean) {
        database.reference.child("users").child("isLoggedIn").setValue(true)
        //guardat en dataStore
        userDataStore.edit { userPreferences ->
            userPreferences[booleanPreferencesKey(UserPreferences.IS_LOGGED_IN_DEFAULT)] = true
        }
    }

}