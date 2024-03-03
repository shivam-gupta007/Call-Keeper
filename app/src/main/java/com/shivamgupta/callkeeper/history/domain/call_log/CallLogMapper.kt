package com.shivamgupta.callkeeper.history.domain.call_log

import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity

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
