package com.shivamgupta.callkeeper.feature_contacts.domain.repository

import android.app.Application
import android.net.Uri
import android.provider.ContactsContract
import com.shivamgupta.callkeeper.feature_contacts.data.data_source.local.ContactsDatabase
import com.shivamgupta.callkeeper.feature_contacts.data.repository.ContactsRepository
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.util.printToLog
import javax.inject.Inject

class ContactsRepositoryImpl @Inject constructor(
    val app: Application,
    database: ContactsDatabase
) : ContactsRepository {

    private val contactsDao = database.getDao()

    override fun extractContactDetails(contactUri: Uri): Contact? {
        val contentResolver = app.contentResolver
        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
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
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                contact = Contact(
                    name = name,
                    phoneNumber = phoneNumber,
                    smsMessage = "",
                    isContactSelected = false
                )

                contact.toString().printToLog()
            }

            cursor.close()

            contact
        } else {
            null
        }
    }


    override suspend fun insertContact(contactEntity: ContactEntity) {
        contactsDao.insertContact(contactEntity)
    }

    override suspend fun getContacts(): List<ContactEntity> {
        return contactsDao.getContacts()
    }
}