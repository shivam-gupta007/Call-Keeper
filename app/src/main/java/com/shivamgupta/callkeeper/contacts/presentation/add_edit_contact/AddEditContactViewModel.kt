package com.shivamgupta.callkeeper.contacts.presentation.add_edit_contact

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.contacts.domain.contact.AddEditContactEvent
import com.shivamgupta.callkeeper.contacts.domain.contact.AddEditContactUiState
import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity
import com.shivamgupta.callkeeper.contacts.domain.contact.removeCountryCode
import com.shivamgupta.callkeeper.contacts.data.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.util.ExceptionUtils
import com.shivamgupta.callkeeper.util.Constants
import com.shivamgupta.callkeeper.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditContactUiState())
    val uiState get() = _uiState.asStateFlow()

    val contactName = MutableStateFlow("")
    val contactPhoneNumber = MutableStateFlow("")
    val smsMessage = MutableStateFlow("")

    fun onEvent(event: AddEditContactEvent) {
        when (event) {
            is AddEditContactEvent.AddContact -> {
                if(validateUserInput()){
                    addContact()
                }
            }

            is AddEditContactEvent.GetContact -> {
                clearState()
                getContactById()
            }

            is AddEditContactEvent.GetContactDetails -> {
                getContactDetails(event.contactUri)
            }

            is AddEditContactEvent.UpdateContact -> {
                if(validateUserInput()){
                    updateContactDetails()
                }
            }

            is AddEditContactEvent.SetContactId -> {
                _uiState.update { currentUiState ->
                    currentUiState.copy(contactId = event.id)
                }
            }
        }
    }

    private fun validateUserInput(): Boolean {
        val contactName = contactName.value
        val contactPhoneNumber = contactPhoneNumber.value
        var nameErrorMessage: String? = null
        var phoneNumberErrorMessage: String? = null

        val isInputValid = when {
            contactName.isEmpty() -> {
                nameErrorMessage = ResourceProvider.getString(R.string.empty_name_error_msg)
                false
            }

            contactPhoneNumber.isEmpty() -> {
                phoneNumberErrorMessage = ResourceProvider.getString(R.string.empty_phone_number_error_msg)
                false
            }

            !contactPhoneNumber.matches(Constants.PHONE_NUMBER_REGEX) -> {
                phoneNumberErrorMessage = ResourceProvider.getString(R.string.invalid_phone_number_error_msg)
                false
            }

            else -> {
                true
            }
        }

        _uiState.update { currentUiState ->
            currentUiState.copy(
                phoneNumberFieldErrorMessage = phoneNumberErrorMessage,
                nameFieldErrorMessage = nameErrorMessage,
            )
        }

        return isInputValid
    }

    private fun getContactById() {
        viewModelScope.launch {
            try {
                val contactId = _uiState.value.contactId
                repository.fetchContactById(contactId)?.let { contactEntity ->
                    contactName.emit(contactEntity.name)
                    contactPhoneNumber.emit(contactEntity.phoneNumber)
                    smsMessage.emit(contactEntity.smsMessage)
                }
            } catch (e: Exception) {
                updateErrorMessage(e)
            }
        }
    }


    private fun addContact() {
        viewModelScope.launch {
            try {
                val contactEntity = ContactEntity(
                    name = contactName.value,
                    phoneNumber = contactPhoneNumber.value,
                    smsMessage = smsMessage.value
                )

                repository.insertContact(contactEntity)

                _uiState.update { currentUiState ->
                    currentUiState.copy(isContactSaved = true)
                }
            } catch (e: Exception) {
                updateErrorMessage(e)
            }
        }
    }

    private fun updateContactDetails() {
        viewModelScope.launch {
            val contactId = _uiState.value.contactId
            try {
                repository.updateContact(
                    id = contactId,
                    name = contactName.value,
                    smsMessage = smsMessage.value,
                    phoneNumber = contactPhoneNumber.value,
                )

                _uiState.update { currentUiState ->
                    currentUiState.copy(isContactSaved = true)
                }
            } catch (e: Exception) {
                updateErrorMessage(e)
            }
        }
    }

    private fun getContactDetails(contactUri: Uri) {
        viewModelScope.launch {
            try {
                repository.extractContactDetails(contactUri)?.let { contact ->
                    val (name, phoneNumber) = contact
                    contactName.emit(name)
                    contactPhoneNumber.emit(phoneNumber.removeCountryCode())
                }
            } catch (e: Exception) {
                updateErrorMessage(e)
            }
        }
    }

    private fun updateErrorMessage(e: Exception) {
        viewModelScope.launch {
            _uiState.update { currentUiState ->
                currentUiState.copy(userMessage = ExceptionUtils.getUiMessageOfException(e))
            }
        }
    }

    fun clearState(){
        viewModelScope.launch {
            _uiState.update { currentUiState ->
                currentUiState.copy(isContactSaved = false)
            }
            contactName.emit("")
            contactPhoneNumber.emit("")
            smsMessage.emit("")
        }
    }

    fun userMessageShown(){
        viewModelScope.launch {
            _uiState.update { currentUiState ->
                currentUiState.copy(userMessage = null)
            }
        }
    }
}