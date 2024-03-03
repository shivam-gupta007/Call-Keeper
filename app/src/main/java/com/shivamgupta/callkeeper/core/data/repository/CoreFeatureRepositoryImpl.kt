package com.shivamgupta.callkeeper.core.data.repository

import com.shivamgupta.callkeeper.history.data.dao.CallLogsDao
import com.shivamgupta.callkeeper.contacts.data.dao.ContactsDao
import com.shivamgupta.callkeeper.core.data.preferences.AppPreferences
import com.shivamgupta.callkeeper.history.domain.call_log.CallLogEntity
import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity
import com.shivamgupta.callkeeper.core.domain.repository.CoreFeatureRepository
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