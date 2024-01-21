package com.shivamgupta.callkeeper.data.repository

import com.shivamgupta.callkeeper.data.data_source.preferences.AppPreferences
import com.shivamgupta.callkeeper.domain.repository.AppPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppPreferencesRepositoryImpl(
    private val appPreferences: AppPreferences
) : AppPreferencesRepository {

    override suspend fun updateDefaultMessage(message: String) {
        withContext(Dispatchers.IO){
            appPreferences.updateDefaultMessage(message)
        }
    }

    override suspend fun toggleCallRejectionFeature(isEnabled: Boolean) {
        withContext(Dispatchers.IO) {
            appPreferences.toggleCallRejectionFeature(isEnabled)
        }
    }


    override fun fetchDefaultMessage(): Flow<String> {
        return appPreferences.defaultMessageFlow
    }

    override fun isCallRejectionEnabled(): Flow<Boolean> {
        return appPreferences.enableCallRejectionFlow
    }


    override suspend fun updateApplyDefaultMsgGlobally(isEnabled: Boolean) {
        withContext(Dispatchers.IO){
            appPreferences.updateApplyDefaultMsgGlobally(isEnabled)
        }
    }

    override fun isDefaultMsgAppliedGlobally(): Flow<Boolean> {
        return appPreferences.applyDefaultMsgGloballyFlow
    }
}