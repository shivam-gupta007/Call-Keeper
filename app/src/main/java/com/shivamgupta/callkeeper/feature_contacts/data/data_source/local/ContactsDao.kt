package com.shivamgupta.callkeeper.feature_contacts.data.data_source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contact: ContactEntity)

    @Query("SELECT * FROM contacts_entity ORDER BY id DESC")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query("UPDATE contacts_entity SET name =:name, phoneNumber =:phoneNumber, smsMessage =:message WHERE id = :id")
    suspend fun updateContact(name: String, message: String, phoneNumber: String, id: Int)

    @Query("SELECT * FROM contacts_entity WHERE phoneNumber =:phoneNumber")
    suspend fun getContact(phoneNumber: String): ContactEntity?

    @Query("UPDATE contacts_entity SET isContactSelected = :isSelected WHERE id=:id")
    suspend fun updateContactSelectStatus(isSelected: Boolean, id: Int)

    @Query("UPDATE contacts_entity SET isContactSelected = :isSelected")
    suspend fun updateSelectStatusOfAllContacts(isSelected: Boolean)

    @Query("DELETE FROM contacts_entity WHERE phoneNumber = :phoneNumber")
    suspend fun deleteContact(phoneNumber: String)

    @Query("UPDATE contacts_entity SET useDefaultMessage =:shouldUseDefaultMsg")
    suspend fun updateDefaultMsgStatus(shouldUseDefaultMsg: Boolean)
}