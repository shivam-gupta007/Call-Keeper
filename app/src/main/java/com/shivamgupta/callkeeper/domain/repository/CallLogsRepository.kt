package com.shivamgupta.callkeeper.domain.repository

import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import kotlinx.coroutines.flow.Flow

interface CallLogsRepository {
    fun fetchCallLogs(): Flow<List<CallLogEntity>>

    suspend fun insertCallLog(callLog: CallLogEntity)

    suspend fun removeCallLog(phoneNumber: String)
}