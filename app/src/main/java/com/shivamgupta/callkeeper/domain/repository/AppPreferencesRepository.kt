package com.shivamgupta.callkeeper.domain.repository

import kotlinx.coroutines.flow.Flow

interface AppPreferencesRepository {

    suspend fun updateDefaultMessage(message: String)

    suspend fun toggleCallRejectionFeature(isEnabled: Boolean)

    suspend fun updateApplyDefaultMsgGlobally(isEnabled: Boolean)

    fun fetchDefaultMessage(): Flow<String>

    fun isCallRejectionEnabled(): Flow<Boolean>

    fun isDefaultMsgAppliedGlobally(): Flow<Boolean>
}