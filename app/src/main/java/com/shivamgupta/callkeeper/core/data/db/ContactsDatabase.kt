package com.shivamgupta.callkeeper.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shivamgupta.callkeeper.history.data.dao.CallLogsDao
import com.shivamgupta.callkeeper.history.domain.call_log.CallLogEntity
import com.shivamgupta.callkeeper.contacts.data.dao.ContactsDao
import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity

@Database(
    entities = [ContactEntity::class, CallLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactsDatabase : RoomDatabase() {

    abstract fun getContactsDao(): ContactsDao
    abstract fun getCallLogsDao(): CallLogsDao
}