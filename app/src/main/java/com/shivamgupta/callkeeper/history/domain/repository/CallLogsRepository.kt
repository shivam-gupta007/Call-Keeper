package com.shivamgupta.callkeeper.history.domain.repository

import com.shivamgupta.callkeeper.history.domain.call_log.CallLogEntity
import kotlinx.coroutines.flow.Flow

interface CallLogsRepository {
    fun fetchCallLogs(): Flow<List<CallLogEntity>>

    suspend fun insertCallLog(callLog: CallLogEntity)

    suspend fun removeCallLog(phoneNumber: String)
}