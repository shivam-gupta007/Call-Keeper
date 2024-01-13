package com.shivamgupta.callkeeper.feature_contacts.domain.model

import android.net.Uri
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact.AddEditContactViewModel
import com.shivamgupta.callkeeper.feature_contacts.util.Constants
import kotlin.random.Random

data class Contact(
    val name: String,
    val phoneNumber: String,
    val smsMessage: String = "",
    val defaultPhotoColor: Int = R.color.blue,
    val isContactSelected: Boolean = false
) {
    companion object {
        fun getRandomColor(): Int {
            return Constants.profileImgColorRes[Random.nextInt(0, 4)]
        }
    }
}

fun Contact.getPhoneNumberWithoutPrefixCode() : String {
    return phoneNumber.removePrefix("+91")
}

sealed class AddEditContactEvent {
    class FindContact(val phoneNumber: String) : AddEditContactEvent()
    object AddContact : AddEditContactEvent()
    object UpdateContact : AddEditContactEvent()
    class GetContactDetails(val contactUri: Uri) : AddEditContactEvent()
}