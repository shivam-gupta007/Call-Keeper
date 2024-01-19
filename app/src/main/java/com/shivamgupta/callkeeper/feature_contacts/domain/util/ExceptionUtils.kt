package com.shivamgupta.callkeeper.feature_contacts.domain.util

import android.database.sqlite.SQLiteConstraintException

object ExceptionUtils {
    fun getUiMessageOfException(e: Exception): String{
        return when(e){
            is SQLiteConstraintException -> {
                "Contact with this phone number already exists"
            }

            else -> {
                "Something went wrong"
            }
        }
    }
}