package com.shivamgupta.callkeeper.feature_contacts.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.shivamgupta.callkeeper.feature_contacts.util.Constants

@Entity(tableName = "contacts_entity")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val phoneNumber: String,
    val smsMessage: String,
    val profilePhotoColor: Int = Contact.getRandomColor(),
    val isContactSelected: Boolean = false,
    val useDefaultMessage: Boolean = false
)
