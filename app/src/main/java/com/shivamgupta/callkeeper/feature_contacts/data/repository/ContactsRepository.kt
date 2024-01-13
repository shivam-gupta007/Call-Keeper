package com.shivamgupta.callkeeper.feature_contacts.data.repository

import android.net.Uri
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    suspend fun extractContactDetails(contactUri: Uri): Contact?

    suspend fun insertContact(contactEntity: ContactEntity)

    suspend fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Int)

    suspend fun updateContactSelectStatus(isSelected: Boolean, id: Int)

    suspend fun updateSelectStatusOfAllContacts(isSelected: Boolean)

    suspend fun fetchContactByPhoneNumber(phoneNumber: String): ContactEntity?

    suspend fun deleteContact(phoneNumber: String)

    suspend fun updateDefaultMsgStatus(shouldUseDefaultMsg: Boolean)

    fun fetchContacts(): Flow<List<ContactEntity>>

}