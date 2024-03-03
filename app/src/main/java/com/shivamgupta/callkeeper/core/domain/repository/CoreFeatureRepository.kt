package com.shivamgupta.callkeeper.core.domain.repository

import com.shivamgupta.callkeeper.history.domain.call_log.CallLogEntity
import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity

interface CoreFeatureRepository {

    suspend fun fetchContactIfFeatureEnabled(
        phoneNumber: String
    ): ContactEntity?

    suspend fun fetchDefaultUserMessage(): String?

    suspend fun insertUserCallLog(callLog: CallLogEntity)
}