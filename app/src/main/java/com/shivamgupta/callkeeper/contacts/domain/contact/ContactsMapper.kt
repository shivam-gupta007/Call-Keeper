package com.shivamgupta.callkeeper.contacts.domain.contact

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