package com.shivamgupta.callkeeper.core

import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.shivamgupta.callkeeper.util.Constants

class PhoneCallService : Service() {

    private lateinit var notificationUtil: NotificationUtil
    private lateinit var incomingCallReceiver: IncomingCallReceiver

    companion object {
        const val SERVICE_ID = 1
    }

    override fun onCreate() {
        super.onCreate()
        notificationUtil = NotificationUtil(this)
        incomingCallReceiver = IncomingCallReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        registerReceiver(incomingCallReceiver, IntentFilter(Constants.PHONE_STATE_ACTION))

        val notificationBuilder = notificationUtil.getNotificationBuilder()

        startForeground(SERVICE_ID, notificationBuilder.build())

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            incomingCallReceiver.clearJob()
            unregisterReceiver(incomingCallReceiver)
        } catch (e: Exception) {
            //TODO("Handle exception here")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}