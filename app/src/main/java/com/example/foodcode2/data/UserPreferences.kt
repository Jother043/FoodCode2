package com.example.foodcode2.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserPreferences(
    val name: String = ANONYMOUS,
    val showViewPage: Boolean = true
) {
    companion object {
        const val ANONYMOUS = "anonymous"
        val USER_NAME = stringPreferencesKey("user_name")
        val SHOW_VIEWPAGE = booleanPreferencesKey("show_viewpage")
    }
}