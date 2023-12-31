package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl, private val appPreferencesRepository: AppPreferencesRepositoryImpl
) : ViewModel() {

    private val _moduleError = MutableStateFlow<String?>(null)
    val moduleError: StateFlow<String?> get() = _moduleError

    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> get() = _contacts

    private val _pickedContact: MutableStateFlow<Contact?> = MutableStateFlow(null)
    val pickedContact: StateFlow<Contact?> get() = _pickedContact

    var defaultMessage: String? = null

    init {
        getDefaultMessage()
    }

    private fun getDefaultMessage() {
        viewModelScope.launch {
            defaultMessage = appPreferencesRepository.fetchDefaultMessage().firstOrNull()
        }
    }

    fun getContactDetails(contactUri: Uri) {
        viewModelScope.launch {
            val contact = repository.extractContactDetails(contactUri)
            _pickedContact.emit(contact)
        }
    }

    fun insertContact(contactEntity: ContactEntity) {
        viewModelScope.launch {
            try {
                repository.addContact(
                    if (contactEntity.smsMessage.isEmpty()) {
                        contactEntity.copy(smsMessage = defaultMessage.orEmpty())
                    } else {
                        contactEntity
                    }
                )
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Int) {
        viewModelScope.launch {
            try {
                repository.updateContact(name, smsMessage, phoneNumber, id)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun updateContactSelectionStatus(isSelected: Boolean, id: Int) {
        viewModelScope.launch {
            try {
                repository.updateContactSelectStatus(isSelected, id)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun updateSelectStatusOfAllContacts(isSelected: Boolean) {
        viewModelScope.launch {
            try {
                repository.updateSelectStatusOfAllContacts(isSelected)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun deleteContact(phoneNumber: String) {
        viewModelScope.launch {
            try {
                repository.deleteContact(phoneNumber)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun getContact(phoneNumber: String) = flow {
        emit(repository.fetchContact(phoneNumber))
    }

    suspend fun checkIfPhoneNumberExists(phoneNumber: String): Boolean {
        val contact = repository.fetchContact(phoneNumber)
        return contact != null
    }

    fun getContacts() {
        viewModelScope.launch {
            repository.fetchContacts().flowOn(Dispatchers.IO).catch {
                    _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
                }.collect {
                    _contacts.emit(it)
                }
        }
    }
}