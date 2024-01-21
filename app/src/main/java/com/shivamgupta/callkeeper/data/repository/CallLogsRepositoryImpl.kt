package com.shivamgupta.callkeeper.data.repository

import android.app.Application
import com.shivamgupta.callkeeper.data.data_source.local.ContactsDatabase
import com.shivamgupta.callkeeper.domain.repository.CallLogsRepository
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CallLogsRepositoryImpl @Inject constructor(
    val app: Application, database: ContactsDatabase
) : CallLogsRepository {
    private val callLogsDao = database.getCallLogsDao()

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