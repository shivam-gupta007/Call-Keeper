package com.shivamgupta.callkeeper.feature_contacts.util

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun checkContactsPermission(context: Context): Boolean {
    return (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
}