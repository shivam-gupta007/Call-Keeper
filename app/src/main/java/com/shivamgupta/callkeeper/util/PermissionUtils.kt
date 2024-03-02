package com.shivamgupta.callkeeper.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionUtils {
    val requiredPermissions = listOf(
        Manifest.permission.ANSWER_PHONE_CALLS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CALL_LOG
    )

    fun checkContactsPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
    }

    fun checkAnswerPhoneCallsPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED)
    }

    fun checkSendSmsPermission(context: Context): Boolean {
        return (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
    }

    fun isPhoneCallAndSmsPermissionGranted(
        context: Context
    ): Boolean {
        val permissions = listOf(
            Manifest.permission.ANSWER_PHONE_CALLS,
            Manifest.permission.SEND_SMS,
        )

        return permissions.all { ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED  }
    }

    fun isAllRequiredPermissionsGranted(context: Context): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(context,permission) == PackageManager.PERMISSION_GRANTED
        }
    }
}