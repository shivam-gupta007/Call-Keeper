package com.shivamgupta.callkeeper.domain.models

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts_entity",
    indices = [Index(value = ["phoneNumber"], unique = true)]
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val phoneNumber: String,
    val smsMessage: String,
    val profilePhotoColor: Int = Contact.getRandomColor(),
    val isContactSelected: Boolean = false,
    val useDefaultMessage: Boolean = false
)
