package com.shivamgupta.callkeeper.feature_contacts.data.repository

import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLogEntity
import kotlinx.coroutines.flow.Flow

interface CallLogsRepository {
    fun fetchCallLogs(): Flow<List<CallLogEntity>>

    suspend fun insertCallLog(callLog: CallLogEntity)

    suspend fun removeCallLog(phoneNumber: String)
}