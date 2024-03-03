package com.shivamgupta.callkeeper.core.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.util.ResourceProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class AppPreferences(val context: Context) {

    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "call_keeper_preferences")

    companion object {
        val DEFAULT_MESSAGE = stringPreferencesKey(name = "default_message")
        val ENABLE_CALL_REJECTION = booleanPreferencesKey(name = "enable_call_rejection")
        val APPLY_DEFAULT_MSG_GLOBALLY = booleanPreferencesKey(name = "apply_default_msg_everywhere")
    }

    val defaultMessageFlow: Flow<String> = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[DEFAULT_MESSAGE] ?: ResourceProvider.getString(R.string.default_message)
        }

    val enableCallRejectionFlow: Flow<Boolean> = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[ENABLE_CALL_REJECTION] ?: false
        }

    val applyDefaultMsgGloballyFlow: Flow<Boolean> = context.datastore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            preferences[APPLY_DEFAULT_MSG_GLOBALLY] ?: false
        }

    suspend fun updateDefaultMessage(message: String) {
        context.datastore.edit { callKeeperPreferences ->
            callKeeperPreferences[DEFAULT_MESSAGE] = message
        }
    }

    suspend fun updateApplyDefaultMsgGlobally(isEnabled: Boolean) {
        context.datastore.edit { callKeeperPreferences ->
            callKeeperPreferences[APPLY_DEFAULT_MSG_GLOBALLY] = isEnabled
        }
    }

    suspend fun toggleCallRejectionFeature(isEnabled: Boolean) {
        context.datastore.edit { callKeeperPreferences ->
            callKeeperPreferences[ENABLE_CALL_REJECTION] = isEnabled
        }
    }
}