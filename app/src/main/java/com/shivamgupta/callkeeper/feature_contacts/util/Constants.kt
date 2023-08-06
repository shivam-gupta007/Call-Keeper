package com.shivamgupta.callkeeper.feature_contacts.util

import com.shivamgupta.callkeeper.R

object Constants {

    val tabsName = listOf("Home", "History", "Settings")
    val tabsIconsRes = listOf(R.drawable.ic_home, R.drawable.ic_history, R.drawable.ic_settings)
    val profileImgColorRes = listOf(R.color.blue, R.color.orange, R.color.yellow, R.color.cyan, R.color.pink)

    //Regular Expressions
    val TEXT_REGEX = Regex("[A-Za-z ]+")
    val PHONE_NUMBER_REGEX = Regex("^[6789][0-9]{9}\$")

    const val CONTACTS_DATABASE = "contacts_database"
}