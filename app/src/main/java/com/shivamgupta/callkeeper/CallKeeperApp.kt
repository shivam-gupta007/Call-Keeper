package com.shivamgupta.callkeeper

import android.app.Application
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CallKeeperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ResourceProvider.initialize(this)
    }
}