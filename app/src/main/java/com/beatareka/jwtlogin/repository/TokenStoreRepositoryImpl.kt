package com.beatareka.jwtlogin.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class TokenStoreRepositoryImpl @Inject constructor(
    private val context: Context
) : TokenStoreRepository {
    companion object {
        private const val PREFERENCES_NAME: String = "userdata"
        private const val TOKEN_KEY = "token"
        private const val REFRESH_TOKEN_KEY = "refresh_token"
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    override fun saveToken(value: String) {
        saveString(TOKEN_KEY, value)
    }

    override fun saveRefreshToken(value: String) {
        saveString(REFRESH_TOKEN_KEY, value)
    }


    override suspend fun getToken(): String? {
        return getValue(TOKEN_KEY)
    }

    override suspend fun getRefreshToken(): String? {
        return getValue(REFRESH_TOKEN_KEY)
    }

    private fun saveString(key: String, value: String) {
        val preferencesKey = stringPreferencesKey(key)
        runBlocking {
            context.dataStore.edit { preferences ->
                preferences[preferencesKey] = value
            }
        }
    }

    private suspend fun getValue(key: String): String? {
        val preferencesKey = stringPreferencesKey(key)
        val preferences = context.dataStore.data.first()
        return preferences[preferencesKey]
    }

}