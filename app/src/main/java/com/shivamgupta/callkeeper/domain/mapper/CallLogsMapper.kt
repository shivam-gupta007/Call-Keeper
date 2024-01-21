package com.shivamgupta.callkeeper.domain.mapper

import com.shivamgupta.callkeeper.domain.models.CallLog
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.models.ContactEntity

fun CallLogEntity.toCallLog(): CallLog {
    return CallLog(
        name = name,
        phoneNumber = phoneNumber,
        smsMessage = smsMessage,
        timestamp = timestamp,
        defaultPhotoColor = profilePhotoColor
    )
}

fun ContactEntity.toCallLogEntity(message: String): CallLogEntity {
    return CallLogEntity(
        name = name,
        phoneNumber = phoneNumber,
        smsMessage = message,
        profilePhotoColor = profilePhotoColor,
        timestamp = System.currentTimeMillis()
    )
}
