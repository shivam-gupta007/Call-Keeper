package com.shivamgupta.callkeeper.feature_contacts.domain.mapper

import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity

fun Contact.toContactEntity(): ContactEntity{
    return ContactEntity(
        name = name,
        phoneNumber = phoneNumber,
        smsMessage = smsMessage,
        defaultPhotoColor = defaultPhotoColor,
        isContactSelected = isContactSelected
    )
}

fun ContactEntity.toContact(): Contact {
    return Contact(
        name = name,
        phoneNumber = phoneNumber,
        smsMessage = smsMessage,
        defaultPhotoColor = defaultPhotoColor,
        isContactSelected = isContactSelected
    )
}