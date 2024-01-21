package com.shivamgupta.callkeeper

import android.app.Application
import com.shivamgupta.callkeeper.util.ResourceProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CallKeeperApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ResourceProvider.initialize(this)
    }
}