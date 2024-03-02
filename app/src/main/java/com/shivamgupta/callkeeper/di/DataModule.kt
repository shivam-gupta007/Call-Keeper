package com.shivamgupta.callkeeper.di

import com.shivamgupta.callkeeper.data.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.data.repository.CallLogsRepositoryImpl
import com.shivamgupta.callkeeper.data.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.data.repository.CoreFeatureRepositoryImpl
import com.shivamgupta.callkeeper.domain.repository.AppPreferencesRepository
import com.shivamgupta.callkeeper.domain.repository.CallLogsRepository
import com.shivamgupta.callkeeper.domain.repository.ContactsRepository
import com.shivamgupta.callkeeper.domain.repository.CoreFeatureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.scopes.ServiceScoped
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