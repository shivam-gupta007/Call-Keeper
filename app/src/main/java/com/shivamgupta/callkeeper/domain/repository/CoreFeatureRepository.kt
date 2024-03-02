package com.shivamgupta.callkeeper.domain.repository

import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.models.ContactEntity

interface CoreFeatureRepository {

    suspend fun fetchContactIfFeatureEnabled(
        phoneNumber: String
    ): ContactEntity?

    suspend fun fetchDefaultUserMessage(): String?

    suspend fun insertUserCallLog(callLog: CallLogEntity)
}