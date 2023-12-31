package com.shivamgupta.callkeeper.feature_contacts.domain.mapper

import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLog
import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLogEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity

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
