package com.shivamgupta.callkeeper.history.domain.call_log

data class CallLog(
    val name: String,
    val phoneNumber: String,
    val smsMessage: String,
    val timestamp: Long,
    val defaultPhotoColor: Int,
)
