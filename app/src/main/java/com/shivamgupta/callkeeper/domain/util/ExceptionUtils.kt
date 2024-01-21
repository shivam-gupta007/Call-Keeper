package com.shivamgupta.callkeeper.domain.util

import android.database.sqlite.SQLiteConstraintException
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.util.ResourceProvider

object ExceptionUtils {
    fun getUiMessageOfException(e: Exception): String{
        return when(e){
            is SQLiteConstraintException -> {
                ResourceProvider.getString(R.string.duplicate_phone_number_error_msg)
            }

            else -> {
                ResourceProvider.getString(R.string.unexpected_error_msg)
            }
        }
    }
}