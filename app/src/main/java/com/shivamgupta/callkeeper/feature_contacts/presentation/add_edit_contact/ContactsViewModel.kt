package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import com.shivamgupta.callkeeper.feature_contacts.util.printToLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl
) : ViewModel() {

    private val _moduleError = MutableStateFlow<String?>(null)
    val moduleError: StateFlow<String?> get() = _moduleError

    private val _contacts = MutableStateFlow<List<ContactEntity>>(emptyList())
    val contacts: StateFlow<List<ContactEntity>> get() = _contacts

    private val _smsMessage = MutableStateFlow<String?>(null)
    val smsMessage: StateFlow<String?> get() = _smsMessage

    fun getContactDetails(contactUri: Uri): Contact? {
        return repository.extractContactDetails(contactUri)
    }

    fun insertContact(contactEntity: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.addContact(contactEntity)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun updateContact(name: String, smsMessage: String, phoneNumber: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateContact(name, smsMessage, phoneNumber, id)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    fun updateContactSelectStatus(isSelected: Boolean, id: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.updateContactSelectStatus(isSelected,id)
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
        contact.printToLog()
        return contact != null
    }

    fun getContacts() {
        viewModelScope.launch {
            repository.fetchContacts()
                .flowOn(Dispatchers.IO)
                .catch {
                    _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
                }
                .collect {
                    _contacts.emit(it)
                }
        }
    }
}