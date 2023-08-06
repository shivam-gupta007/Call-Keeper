package com.shivamgupta.callkeeper.feature_contacts.data.data_source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Query("SELECT * FROM contactsentity")
    suspend fun getContacts() : List<ContactEntity>
}