package com.shivamgupta.callkeeper.core.di

import com.shivamgupta.callkeeper.core.data.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.history.data.repository.CallLogsRepositoryImpl
import com.shivamgupta.callkeeper.contacts.data.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.core.data.repository.CoreFeatureRepositoryImpl
import com.shivamgupta.callkeeper.core.domain.repository.AppPreferencesRepository
import com.shivamgupta.callkeeper.history.domain.repository.CallLogsRepository
import com.shivamgupta.callkeeper.contacts.domain.repository.ContactsRepository
import com.shivamgupta.callkeeper.core.domain.repository.CoreFeatureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    
    @Binds
    @Singleton
    abstract fun bindCoreFeatureRepository(
        coreFeatureRepository: CoreFeatureRepositoryImpl
    ): CoreFeatureRepository

    @Binds
    @Singleton
    abstract fun bindsContactsRepository(
        contactsRepository: ContactsRepositoryImpl
    ): ContactsRepository

    @Binds
    @Singleton
    abstract fun bindsCallLogsRepository(
        callLogRepository: CallLogsRepositoryImpl
    ): CallLogsRepository

    @Binds
    @Singleton
    abstract fun bindsAppPreferencesRepository(
        appPreferencesRepository: AppPreferencesRepositoryImpl
    ): AppPreferencesRepository
    
}