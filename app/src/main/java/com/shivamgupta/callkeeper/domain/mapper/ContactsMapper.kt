package com.shivamgupta.callkeeper.domain.mapper

import com.shivamgupta.callkeeper.domain.models.Contact
import com.shivamgupta.callkeeper.domain.models.ContactEntity

fun ContactEntity.toContact(): Contact {
    return Contact(
        id = id,
        name = name,
        phoneNumber = phoneNumber,
        smsMessage = smsMessage,
        defaultPhotoColor = profilePhotoColor,
        isContactSelected = isContactSelected
    )
}

fun List<ContactEntity>.toContacts(): List<Contact> {
    return map {
        Contact(
            id = it.id,
            name = it.name,
            phoneNumber = it.phoneNumber,
            smsMessage = it.smsMessage,
            defaultPhotoColor = it.profilePhotoColor,
            isContactSelected = it.isContactSelected
        )
    }
}