package com.shivamgupta.callkeeper.feature_contacts.data.repository

import android.net.Uri
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactNameAndPhoneNumber
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    suspend fun extractContactDetails(contactUri: Uri): ContactNameAndPhoneNumber?

    suspend fun insertContact(contactEntity: ContactEntity)

    suspend fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Long)

    suspend fun updateContactSelectStatus(isSelected: Boolean, id: Long)

    suspend fun updateSelectStatusOfAllContacts(isSelected: Boolean)

    suspend fun fetchContactByPhoneNumber(phoneNumber: String): ContactEntity?

    suspend fun fetchContactById(id: Long): ContactEntity?

    suspend fun deleteContact(id: Long)

    suspend fun updateDefaultMsgStatus(shouldUseDefaultMsg: Boolean)

    fun fetchContacts(): Flow<List<ContactEntity>>

}