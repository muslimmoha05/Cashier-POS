package com.utopiatechhub.cashierpos.language

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private val Context.languageDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "language_prefs"
)

class LanguageDataStore(private val appContext: Context) {

    companion object {
        private val LANGUAGE_KEY = stringPreferencesKey("language")
        const val DEFAULT_LANGUAGE = "en"
        val SUPPORTED_LANGUAGES = setOf("en", "am")
    }

    val languageFlow = appContext.languageDataStore.data
        .map { preferences -> preferences[LANGUAGE_KEY] ?: DEFAULT_LANGUAGE }

    suspend fun saveLanguage(language: String): Boolean {
        return if (SUPPORTED_LANGUAGES.contains(language)) {
            appContext.languageDataStore.edit { prefs ->
                prefs[LANGUAGE_KEY] = language
            }
            true
        } else false
    }
}