package com.shivamgupta.callkeeper.feature_contacts.domain.mapper

import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity

fun ContactEntity.toContact(): Contact {
    return Contact(
        name = name,
        phoneNumber = phoneNumber,
        smsMessage = smsMessage,
        defaultPhotoColor = profilePhotoColor,
        isContactSelected = isContactSelected
    )
}