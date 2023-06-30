package com.shivamgupta.autocallblocker.feature_contacts.util

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources

object ResourceProvider {
    private lateinit var applicationContext: Context

    fun initialize(application: Application) {
        applicationContext = application.applicationContext
    }

    fun getColor(@ColorRes colorResId: Int): Int {
        return applicationContext.getColor(colorResId)
    }

    fun getDrawable(@DrawableRes drawableResId: Int): Drawable? {
        return AppCompatResources.getDrawable(applicationContext, drawableResId)
    }
}