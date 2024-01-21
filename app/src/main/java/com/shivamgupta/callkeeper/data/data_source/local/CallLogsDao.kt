package com.shivamgupta.callkeeper.data.data_source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CallLogsDao {

    @Query("SELECT * FROM call_log_entity ORDER BY timestamp DESC")
    fun getCallLogs(): Flow<List<CallLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCallLog(callLog: CallLogEntity)

    @Query("DELETE FROM call_log_entity WHERE phoneNumber = :phoneNumber")
    suspend fun deleteCallLog(phoneNumber: String)
}