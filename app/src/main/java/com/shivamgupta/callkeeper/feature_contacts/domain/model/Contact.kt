package com.shivamgupta.callkeeper.feature_contacts.domain.model

import android.net.Uri
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.util.Constants
import kotlin.random.Random

data class Contact(
    val id: Long,
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

fun Contact.getPhoneNumberWithoutCountryCode() : String {
    return phoneNumber.removePrefix("+91")
}

fun String.removeCountryCode(): String {
    return this.removePrefix("+91")
}

sealed class AddEditContactEvent {
    object GetContact : AddEditContactEvent()
    class SetContactId(val id: Long): AddEditContactEvent()
    object AddContact : AddEditContactEvent()
    object UpdateContact : AddEditContactEvent()
    class GetContactDetails(val contactUri: Uri) : AddEditContactEvent()
}

data class ContactItemUiState(
    val name: String = "",
    val phoneNumber: String = "",
    val message: String = ""
)

data class AddEditContactUiState(
    val contactId: Long = -1L,
    val userMessage: String? = null,
    val isContactSaved: Boolean = false,
    val phoneNumberFieldErrorMessage: String? = null,
    val nameFieldErrorMessage: String? = null,
)

typealias ContactNameAndPhoneNumber = Pair<String, String>