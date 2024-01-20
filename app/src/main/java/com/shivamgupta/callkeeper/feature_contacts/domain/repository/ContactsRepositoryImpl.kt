package com.shivamgupta.callkeeper.feature_contacts.domain.repository

import android.app.Application
import android.net.Uri
import android.provider.ContactsContract
import com.shivamgupta.callkeeper.feature_contacts.data.data_source.local.ContactsDatabase
import com.shivamgupta.callkeeper.feature_contacts.data.repository.ContactsRepository
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactNameAndPhoneNumber
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    val app: Application, database: ContactsDatabase
) : ContactsRepository {

    private val contactsDao = database.getContactsDao()

    override suspend fun extractContactDetails(contactUri: Uri): ContactNameAndPhoneNumber? {
        val contentResolver = app.contentResolver
        val projection = arrayOf(
            ContactsContract.Contacts._ID, ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER
        )

        val cursor = contentResolver.query(
            contactUri, projection, null, null, null
        )

        return if (cursor != null) {
            var contact: ContactNameAndPhoneNumber? = null
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {
                    val phoneNumberUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val phoneNumberSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id
                    val phoneNumberProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phoneNumberCursor = contentResolver.query(
                        phoneNumberUri, phoneNumberProjection, phoneNumberSelection, null, null
                    )

                    if (phoneNumberCursor != null) {
                        while (phoneNumberCursor.moveToNext()) {
                            val phoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contact = ContactNameAndPhoneNumber(
                                first = name,
                                second = phoneNumber,
                            )
                        }

                        phoneNumberCursor.close()
                    }
                }
            }

            cursor.close()

            contact
        } else {
            null
        }
    }

    override suspend fun insertContact(contactEntity: ContactEntity) {
        withContext(Dispatchers.IO) {
            contactsDao.insertContact(contactEntity)
        }
    }

    override fun fetchContacts(): Flow<List<ContactEntity>> {
        return contactsDao.getContacts().flowOn(Dispatchers.IO)
    }

    override suspend fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Long) {
        withContext(Dispatchers.IO) {
            contactsDao.updateContact(name, smsMessage, phoneNumber, id)
        }
    }

    override suspend fun updateContactSelectStatus(isSelected: Boolean, id: Long) {
        withContext(Dispatchers.IO) {
            contactsDao.updateContactSelectStatus(isSelected, id)
        }
    }

    override suspend fun updateSelectStatusOfAllContacts(isSelected: Boolean) {
        withContext(Dispatchers.IO) {
            contactsDao.updateSelectStatusOfAllContacts(isSelected)
        }
    }

    override suspend fun fetchContactByPhoneNumber(phoneNumber: String): ContactEntity? {
        val contact = withContext(Dispatchers.IO) {
            contactsDao.getContactByPhoneNumber(phoneNumber)
        }
        return contact
    }

    override suspend fun deleteContact(id: Long) {
        withContext(Dispatchers.IO) {
            contactsDao.deleteContact(id)
        }
    }

    override suspend fun updateDefaultMsgStatus(shouldUseDefaultMsg: Boolean) {
        withContext(Dispatchers.IO) {
            contactsDao.updateDefaultMsgStatus(shouldUseDefaultMsg)
        }
    }

    override suspend fun fetchContactById(id: Long): ContactEntity? {
        return withContext(Dispatchers.IO){
            contactsDao.getContactById(id)
        }
    }
}