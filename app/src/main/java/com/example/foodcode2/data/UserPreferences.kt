package com.example.foodcode2.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

data class UserPreferences(
    val name: String = ANONYMOUS,
    val email: String = ANONYMOUS,
    val password: String = ANONYMOUS,
    val showViewPage: Boolean = true,
    val saveShowViewPage: Boolean = true,
    val error: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false,
    val isRegistered: Boolean = false,
    val isSucefull: Boolean = false,
    val isSucefullMessage: String = "",
    val anonymous: Boolean = false,
) {
    companion object {
        const val SETTINGS_FILE = "settings"
        const val USER_NAME = "username"
        var ANONYMOUS = ""
        const val SHOW_VIEW_PAGE = "showViewPage"
        const val SHOW_VIEW_PAGE_DEFAULT = false
        const val IS_LOGGED_IN_DEFAULT = "isLoggedIn"
        const val IS_LOGGED_IN = false
        const val EMAIL = "email"

    }
}