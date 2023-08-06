package com.shivamgupta.callkeeper.feature_contacts.data.data_source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity

@Database(entities = [ContactEntity::class], version = 1)
abstract class ContactsDatabase : RoomDatabase() {

    abstract fun getDao(): ContactsDao
}