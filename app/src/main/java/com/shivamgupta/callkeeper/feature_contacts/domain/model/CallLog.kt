package com.shivamgupta.callkeeper.feature_contacts.domain.model

data class CallLog(
    val name: String,
    val phoneNumber: String,
    val smsMessage: String,
    val timestamp: Long,
    val defaultPhotoColor: Int,
)
