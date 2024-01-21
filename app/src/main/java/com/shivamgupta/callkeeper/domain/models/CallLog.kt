package com.shivamgupta.callkeeper.domain.models

data class CallLog(
    val name: String,
    val phoneNumber: String,
    val smsMessage: String,
    val timestamp: Long,
    val defaultPhotoColor: Int,
)
