package com.shivamgupta.callkeeper.feature_contacts.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "call_log_entity")
data class CallLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val smsMessage: String,
    val profilePhotoColor: Int,
    val timestamp: Long
)