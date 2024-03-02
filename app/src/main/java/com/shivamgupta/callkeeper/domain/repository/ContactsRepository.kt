package com.shivamgupta.callkeeper.domain.repository

import android.net.Uri
import com.shivamgupta.callkeeper.domain.models.ContactEntity
import com.shivamgupta.callkeeper.domain.models.ContactNameAndPhoneNumber
import kotlinx.coroutines.flow.Flow

interface ContactsRepository {

    suspend fun extractContactDetails(contactUri: Uri): ContactNameAndPhoneNumber?

    suspend fun insertContact(contactEntity: ContactEntity)

    suspend fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Long)

    suspend fun updateContactSelectStatus(isSelected: Boolean, id: Long)

    suspend fun updateSelectStatusOfAllContacts(isSelected: Boolean)

    suspend fun fetchContactById(id: Long): ContactEntity?

    suspend fun deleteContact(id: Long)

    suspend fun updateDefaultMsgStatus(shouldUseDefaultMsg: Boolean)

    fun fetchContacts(): Flow<List<ContactEntity>>

}