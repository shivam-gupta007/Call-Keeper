package com.shivamgupta.callkeeper.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.models.ContactEntity

@Database(
    entities = [ContactEntity::class, CallLogEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ContactsDatabase : RoomDatabase() {

    abstract fun getContactsDao(): ContactsDao
    abstract fun getCallLogsDao(): CallLogsDao
}