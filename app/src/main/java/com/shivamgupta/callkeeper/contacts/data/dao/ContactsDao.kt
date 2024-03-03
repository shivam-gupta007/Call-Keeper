package com.shivamgupta.callkeeper.contacts.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertContact(contact: ContactEntity)

    @Query("SELECT * FROM contacts_entity ORDER BY id DESC")
    fun getContacts(): Flow<List<ContactEntity>>

    @Query("""
            UPDATE contacts_entity 
            SET name =:name, phone_number =:phoneNumber, sms_message =:message 
            WHERE id = :id
    """)
    suspend fun updateContact(
        name: String,
        message: String,
        phoneNumber: String,
        id: Long
    )

    @Query("""
        SELECT * FROM contacts_entity 
        WHERE phone_number =:phoneNumber AND is_contact_selected =:isSelected
    """)
    suspend fun getContactIfFeatureEnabled(
        phoneNumber: String,
        isSelected: Boolean = true
    ): ContactEntity?

    @Query("SELECT * FROM contacts_entity WHERE id = :id")
    suspend fun getContactById(id: Long): ContactEntity?

    @Query("""
        UPDATE contacts_entity 
        SET is_contact_selected = :isSelected 
        WHERE id=:id
    """)
    suspend fun updateContactSelectStatus(isSelected: Boolean, id: Long)

    @Query("UPDATE contacts_entity SET is_contact_selected = :isSelected")
    suspend fun updateSelectStatusOfAllContacts(isSelected: Boolean)

    @Query("DELETE FROM contacts_entity WHERE id = :id")
    suspend fun deleteContact(id: Long)

    @Query("""
        UPDATE contacts_entity 
        SET use_default_message =:shouldUseDefaultMsg
    """)
    suspend fun updateDefaultMsgStatus(shouldUseDefaultMsg: Boolean)
}