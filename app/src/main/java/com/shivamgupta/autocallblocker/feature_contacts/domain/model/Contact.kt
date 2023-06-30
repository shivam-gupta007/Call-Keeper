package com.shivamgupta.autocallblocker.feature_contacts.domain.model

import com.shivamgupta.autocallblocker.feature_contacts.util.Constants
import kotlin.random.Random

data class Contact(
    val name: String,
    val phoneNumber: String,
    val smsMessage: String = "",
    val defaultPhotoColor: Int
){
    companion object {
        fun getRandomColor() : Int {
            return Constants.profileImgColorRes[Random.nextInt(0, 4)]
        }
    }
}
