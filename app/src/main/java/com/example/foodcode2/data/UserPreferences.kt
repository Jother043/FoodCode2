package com.example.foodcode2.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserPreferences(
    val name: String = ANONYMOUS,
    val showViewPage: Boolean = false,
    val saveShowViewPage: Boolean = false,
    val error: Boolean = false
) {
    companion object {
        const val SETTINGS_FILE = "settings"
        const val USER_NAME = "username"
        const val ANONYMOUS = ""
        const val SHOW_VIEW_PAGE = "showViewPage"
        const val SHOW_VIEW_PAGE_DEFAULT = false
    }
}