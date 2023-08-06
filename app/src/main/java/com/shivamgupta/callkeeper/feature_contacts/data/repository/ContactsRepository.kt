package com.shivamgupta.callkeeper.feature_contacts.data.repository

import android.net.Uri
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity

interface ContactsRepository {

    fun extractContactDetails(contactUri: Uri): Contact?

    suspend fun insertContact(contactEntity: ContactEntity)

    suspend fun getContacts(): List<ContactEntity>
}