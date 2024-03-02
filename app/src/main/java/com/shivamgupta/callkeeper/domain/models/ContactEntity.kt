package com.shivamgupta.callkeeper.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "contacts_entity",
    indices = [Index(value = ["phone_number"], unique = true)]
)
data class ContactEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo("phone_number") val phoneNumber: String,
    @ColumnInfo("sms_message") val smsMessage: String,
    @ColumnInfo("profile_photo_color") val profilePhotoColor: Int = Contact.getRandomColor(),
    @ColumnInfo("is_contact_selected") val isContactSelected: Boolean = false,
    @ColumnInfo("use_default_message") val useDefaultMessage: Boolean = false
)
