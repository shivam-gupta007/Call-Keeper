package com.shivamgupta.callkeeper.history.domain.call_log

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_log_entity")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    @ColumnInfo("phone_number") val phoneNumber: String,
    @ColumnInfo("sms_message") val smsMessage: String,
    @ColumnInfo("profile_photo_color") val profilePhotoColor: Int,
    val timestamp: Long
)