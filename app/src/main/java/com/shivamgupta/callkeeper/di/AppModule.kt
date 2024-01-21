package com.shivamgupta.callkeeper.di

import android.app.Application
import androidx.room.Room
import com.shivamgupta.callkeeper.data.data_source.local.ContactsDatabase
import com.shivamgupta.callkeeper.data.data_source.preferences.AppPreferences
import com.shivamgupta.callkeeper.data.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.data.repository.CallLogsRepositoryImpl
import com.shivamgupta.callkeeper.data.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.domain.repository.AppPreferencesRepository
import com.shivamgupta.callkeeper.domain.repository.CallLogsRepository
import com.shivamgupta.callkeeper.domain.repository.ContactsRepository
import com.shivamgupta.callkeeper.util.Constants
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
    ): ContactsRepository {
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
    ): CallLogsRepository {
        return CallLogsRepositoryImpl(
            app,
            database
        )
    }

    @Provides
    @Singleton
    fun providesAppPreferencesRepository(
        appPreferences: AppPreferences
    ): AppPreferencesRepository {
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