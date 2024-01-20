package com.shivamgupta.callkeeper.feature_contacts.util

import com.shivamgupta.callkeeper.R

object Constants {

    const val PHONE_STATE_ACTION = "android.intent.action.PHONE_STATE"
    const val PHONE_NUMBER_LENGTH = 10

    val tabsName = listOf("Home", "History", "Settings")
    val tabsIconsRes = listOf(R.drawable.ic_home, R.drawable.ic_history, R.drawable.ic_settings)
    val profileImgColorRes = listOf(R.color.blue, R.color.orange, R.color.yellow, R.color.cyan, R.color.pink)

    //Regular Expressions
    val PHONE_NUMBER_REGEX = Regex("^[6789][0-9]{9}\$")

    const val VISIBLE_TEXT_LENGTH = 18

    const val CONTACTS_DATABASE = "contacts_database"

    const val TIMESTAMP_DATE_FORMAT = "yyyy-MM-ddHH:mm:ss.SSS"
    const val CALL_LOG_DATE_FORMAT = "dd MMM yyyy hh:mm a"
}