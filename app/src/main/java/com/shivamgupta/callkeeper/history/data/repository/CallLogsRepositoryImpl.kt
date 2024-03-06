package com.shivamgupta.callkeeper.history.data.repository

import com.shivamgupta.callkeeper.history.data.dao.CallLogsDao
import com.shivamgupta.callkeeper.history.domain.call_log.CallLogEntity
import com.shivamgupta.callkeeper.history.domain.repository.CallLogsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CallLogsRepositoryImpl @Inject constructor(
    private val callLogsDao: CallLogsDao
) : CallLogsRepository {
    override fun fetchCallLogs(): Flow<List<CallLogEntity>> {
        return callLogsDao.getCallLogs().flowOn(Dispatchers.IO)
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