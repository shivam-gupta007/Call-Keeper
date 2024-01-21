package com.shivamgupta.callkeeper.core

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.shivamgupta.callkeeper.R

class NotificationUtil(private val context: Context) {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "com.shivamgupta.callkeeper.notification_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Call Keeper"
    }


    private fun getNotificationChannel(): NotificationChannel {
        return NotificationChannel(
            NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        )
    }

    fun getNotificationBuilder(): Notification.Builder {
        val channel = getNotificationChannel()
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        return Notification.Builder(context, NOTIFICATION_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.ic_settings)
            setContentTitle("Call keeper")
            setContentText("Activated")
        }
    }


}