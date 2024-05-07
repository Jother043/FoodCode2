package com.example.foodcode2.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodcode2.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositories(
    private val userDataStore: DataStore<Preferences>
) {

    //Obtener las preferencias del usuario.
    fun getUserPrefs(): Flow<UserPreferences> {
        return userDataStore.data.map { userPreferences ->
            val name = userPreferences[stringPreferencesKey(UserPreferences.USER_NAME)]
                ?: UserPreferences.ANONYMOUS
            val checked = userPreferences[booleanPreferencesKey(UserPreferences.SHOW_VIEW_PAGE)]
                ?: UserPreferences.SHOW_VIEW_PAGE_DEFAULT
            return@map UserPreferences(
                name = name,
                showViewPage = checked,
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
}