package com.shivamgupta.callkeeper.feature_contacts.domain.repository

import android.app.Application
import android.net.Uri
import android.provider.ContactsContract
import com.shivamgupta.callkeeper.feature_contacts.data.data_source.local.ContactsDatabase
import com.shivamgupta.callkeeper.feature_contacts.data.repository.ContactsRepository
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    val app: Application,
    database: ContactsDatabase
) : ContactsRepository {

    private val contactsDao = database.getDao()

    override fun extractContactDetails(contactUri: Uri): Contact? {
        val contentResolver = app.contentResolver
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER
        )

        val cursor = contentResolver.query(
            contactUri,
            projection,
            null,
            null,
            null
        )


        return if (cursor != null) {
            var contact: Contact? = null
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER))

                if (hasPhoneNumber > 0) {
                    val phoneNumberUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val phoneNumberSelection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id
                    val phoneNumberProjection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val phoneNumberCursor = contentResolver.query(
                        phoneNumberUri,
                        phoneNumberProjection,
                        phoneNumberSelection,
                        null,
                        null
                    )

                    if (phoneNumberCursor != null) {

                        while (phoneNumberCursor.moveToNext()) {
                            val phoneNumber = phoneNumberCursor.getString(phoneNumberCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            contact = Contact(
                                name = name,
                                phoneNumber = phoneNumber,
                                smsMessage = "",
                                isContactSelected = false
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

    override suspend fun addContact(contactEntity: ContactEntity) {
        contactsDao.insertContact(contactEntity)
    }

    override fun fetchContacts(): Flow<List<ContactEntity>> {
        return contactsDao.getContacts()
    }

    override suspend fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Int) {
        contactsDao.updateContact(name, smsMessage, phoneNumber, id)
    }

    override suspend fun updateContactSelectStatus(isSelected: Boolean, id: Int) {
        contactsDao.updateContactSelectStatus(isSelected, id)
    }

    override suspend fun fetchContact(phoneNumber: String): ContactEntity? {
        return contactsDao.getContact(phoneNumber)
    }
}