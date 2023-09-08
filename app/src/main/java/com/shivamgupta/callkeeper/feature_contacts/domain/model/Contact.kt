package com.shivamgupta.callkeeper.feature_contacts.domain.model

import com.shivamgupta.callkeeper.R
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
