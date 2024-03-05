package com.shivamgupta.callkeeper.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.contacts.data.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.core.domain.repository.AppPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val appPreferencesRepository: AppPreferencesRepository,
    private val contactsRepository: ContactsRepositoryImpl
) : ViewModel() {

    private val _isCallRejectionEnabled = MutableStateFlow(false)
    val isCallRejectionEnabled get() = _isCallRejectionEnabled.asStateFlow()

    private val _defaultSmsMessage = MutableStateFlow("")
    val defaultSmsMessage get() = _defaultSmsMessage.asStateFlow()

    private val _applyDefaultMsg = MutableStateFlow(false)
    val applyDefaultMsg get() = _applyDefaultMsg.asStateFlow()

    init {
        checkDefaultMessage()
        checkIsCallRejectionEnabled()
        checkIsDefaultMsgAppliedGlobally()
    }

    fun changeDefaultMessage(message: String) {
        viewModelScope.launch {
            appPreferencesRepository.updateDefaultMessage(message)
        }
    }

    fun enableOrDisableCallRejection(isEnabled: Boolean) {
        viewModelScope.launch {
            appPreferencesRepository.toggleCallRejectionFeature(isEnabled)
        }
    }


    private fun checkDefaultMessage() {
        viewModelScope.launch {
            appPreferencesRepository.fetchDefaultMessage().collectLatest { message ->
                _defaultSmsMessage.update {
                    message
                }
            }
        }
    }

    private fun checkIsCallRejectionEnabled() {
        viewModelScope.launch {
            appPreferencesRepository.isCallRejectionEnabled().collectLatest { isEnabled ->
                _isCallRejectionEnabled.update {
                    isEnabled
                }
            }
        }
    }


    fun updateApplyDefaultMsgGlobally(isEnabled: Boolean) {
        viewModelScope.launch {
            contactsRepository.updateDefaultMsgStatus(shouldUseDefaultMsg = isEnabled)
            appPreferencesRepository.updateApplyDefaultMsgGlobally(isEnabled)
        }
    }

    private fun checkIsDefaultMsgAppliedGlobally() {
        viewModelScope.launch {
            appPreferencesRepository.isDefaultMsgAppliedGlobally().collectLatest {
                _applyDefaultMsg.emit(it)
            }
        }
    }
}