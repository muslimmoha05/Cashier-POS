package com.utopiatechhub.cashierpos.authentication

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.tenantDataStore: DataStore<Preferences> by preferencesDataStore(name = "tenant_prefs")

class TenantDataStore(context: Context) {
    private val dataStore = context.tenantDataStore

    companion object {
        private val TENANT_ID_KEY = longPreferencesKey("tenant_id")
    }

    val tenantIdFlow: Flow<Long?> = dataStore.data
        .map { preferences -> preferences[TENANT_ID_KEY] }

    suspend fun saveTenantId(tenantId: Long) {
        dataStore.edit { preferences ->
            preferences[TENANT_ID_KEY] = tenantId
        }
    }

    suspend fun clearTenantId() {
        dataStore.edit { preferences ->
            preferences.remove(TENANT_ID_KEY)
        }
    }
}