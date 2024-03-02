package com.shivamgupta.callkeeper.data.repository

import com.shivamgupta.callkeeper.data.data_source.local.CallLogsDao
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.repository.CallLogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CallLogsRepositoryImpl @Inject constructor(
    private val callLogsDao: CallLogsDao
) : CallLogsRepository {
    override fun fetchCallLogs(): Flow<List<CallLogEntity>> {
        return callLogsDao.getCallLogs()
    }

    override suspend fun insertCallLog(callLog: CallLogEntity) {
        withContext(Dispatchers.IO){
            callLogsDao.insertCallLog(callLog)
        }
    }

    override suspend fun removeCallLog(phoneNumber: String) {
        withContext(Dispatchers.IO) {
            callLogsDao.deleteCallLog(phoneNumber)
        }
    }
}