package com.example.foodcode2.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.foodcode2.data.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepositories(
    private val userDataStore: DataStore<Preferences>
) {

    fun getUserPrefs(): Flow<UserPreferences> {
        return userDataStore.data.map { userPreferences ->
            val name = userPreferences[UserPreferences.USER_NAME] ?: UserPreferences.ANONYMOUS
            val showViewPage = userPreferences[UserPreferences.SHOW_VIEWPAGE] ?: true
            UserPreferences(name = name, showViewPage = showViewPage)
        }
    }

    suspend fun saveSettings(name: String, showViewPage: Boolean) {
        userDataStore.edit { userPreferences ->
            userPreferences[UserPreferences.USER_NAME] = name
            userPreferences[UserPreferences.SHOW_VIEWPAGE] = showViewPage
        }
    }

    /**
     * Funcion que obtiene el nombre del usuario almacenado en el DataStore y lo retorna como un Flow
     */
    fun getUserName(): Flow<String> {
        // Se obtiene el valor almacenado en el DataStore y se retorna como un Flow
        return userDataStore.data.map { userPreferences ->
            userPreferences[UserPreferences.USER_NAME] ?: UserPreferences.ANONYMOUS
        }
    }

    suspend fun getShowViewPage(): Boolean {
        return userDataStore.data.map { userPreferences ->
            userPreferences[UserPreferences.SHOW_VIEWPAGE] ?: true
        }.toString().toBoolean()
    }

    suspend fun saveUserName(name: String) {
        userDataStore.edit { userPreferences ->
            userPreferences[UserPreferences.USER_NAME] = name
        }
    }
}