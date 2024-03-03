package com.shivamgupta.callkeeper.core.di

import android.app.Application
import androidx.room.Room
import com.shivamgupta.callkeeper.history.data.dao.CallLogsDao
import com.shivamgupta.callkeeper.contacts.data.dao.ContactsDao
import com.shivamgupta.callkeeper.core.data.db.ContactsDatabase
import com.shivamgupta.callkeeper.core.data.preferences.AppPreferences
import com.shivamgupta.callkeeper.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppPreferences(app: Application): AppPreferences {
        return AppPreferences(app.applicationContext)
    }

    @Provides
    @Singleton
    fun providesContactsDatabase(app: Application) = Room.databaseBuilder(
        app,
        ContactsDatabase::class.java,
        Constants.CONTACTS_DATABASE,
    ).build()

    @Provides
    @Singleton
    fun providesContactsDao(db: ContactsDatabase): ContactsDao = db.getContactsDao()

    @Provides
    @Singleton
    fun providesCallLogsDao(db: ContactsDatabase): CallLogsDao = db.getCallLogsDao()
}