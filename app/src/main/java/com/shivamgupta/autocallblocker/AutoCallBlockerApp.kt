package com.shivamgupta.autocallblocker

import android.app.Application
import com.shivamgupta.autocallblocker.feature_contacts.util.ResourceProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AutoCallBlockerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ResourceProvider.initialize(this)
    }
}