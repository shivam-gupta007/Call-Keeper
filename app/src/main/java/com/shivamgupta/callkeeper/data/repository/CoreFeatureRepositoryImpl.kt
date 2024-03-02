package com.shivamgupta.callkeeper.data.repository

import com.shivamgupta.callkeeper.data.data_source.local.CallLogsDao
import com.shivamgupta.callkeeper.data.data_source.local.ContactsDao
import com.shivamgupta.callkeeper.data.data_source.preferences.AppPreferences
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.models.ContactEntity
import com.shivamgupta.callkeeper.domain.repository.CoreFeatureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CoreFeatureRepositoryImpl @Inject constructor(
    private val contactsDao: ContactsDao,
    private val callLogsDao: CallLogsDao,
    private val appPreferences: AppPreferences
) : CoreFeatureRepository {
    override suspend fun fetchContactIfFeatureEnabled(phoneNumber: String): ContactEntity? {
        return withContext(Dispatchers.IO){
            contactsDao.getContactIfFeatureEnabled(phoneNumber)
        }
    }

    override suspend fun fetchDefaultUserMessage(): String? {
        return withContext(Dispatchers.IO){
            appPreferences.defaultMessageFlow.firstOrNull()
        }
    }

    override suspend fun insertUserCallLog(callLog: CallLogEntity) {
        withContext(Dispatchers.IO){
            callLogsDao.insertCallLog(callLog)
        }
    }
}