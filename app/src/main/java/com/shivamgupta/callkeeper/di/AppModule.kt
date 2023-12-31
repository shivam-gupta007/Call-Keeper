package com.shivamgupta.callkeeper.di

import android.app.Application
import androidx.room.Room
import com.shivamgupta.callkeeper.feature_contacts.data.data_source.local.ContactsDatabase
import com.shivamgupta.callkeeper.feature_contacts.data.data_source.preferences.AppPreferences
import com.shivamgupta.callkeeper.feature_contacts.data.repository.AppPreferencesRepository
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.CallLogsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesContactsRepository(
        app: Application,
        database: ContactsDatabase
    ): ContactsRepositoryImpl {
        return ContactsRepositoryImpl(
            app,
            database
        )
    }

    @Provides
    @Singleton
    fun providesCallLogsRepository(
        app: Application,
        database: ContactsDatabase
    ): CallLogsRepositoryImpl {
        return CallLogsRepositoryImpl(
            app,
            database
        )
    }

    @Provides
    @Singleton
    fun providesAppPreferencesRepository(
        appPreferences: AppPreferences
    ): AppPreferencesRepositoryImpl {
        return AppPreferencesRepositoryImpl(appPreferences)
    }

    @Provides
    @Singleton
    fun providesAppPreferences(app: Application): AppPreferences {
        return AppPreferences(app.applicationContext)
    }

    @Provides
    @Singleton
    fun providesContactsDatabase(app: Application): ContactsDatabase {
        return Room.databaseBuilder(
            app,
            ContactsDatabase::class.java,
            Constants.CONTACTS_DATABASE,
        ).build()
    }

}