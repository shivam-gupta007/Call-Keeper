package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toContact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactsUiState
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.domain.util.ExceptionUtils
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import com.shivamgupta.callkeeper.feature_contacts.util.truncateStringWithDots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl
) : ViewModel() {

    companion object {
        const val TAG = "ContactsViewModel"
    }

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState get() = _uiState.asStateFlow()

    private var contactsJob: Job? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    fun updateContactSelectionStatus(isSelected: Boolean, id: Long) {
        viewModelScope.launch {
            try {
                repository.updateContactSelectStatus(isSelected, id)
            } catch (e: Exception) {
                updateUserMessageByException(e)
            }
        }
    }

    fun removeContact(contact: Contact) {
        viewModelScope.launch {
            try {
                repository.deleteContact(contact.id)
                updateUserMessage(
                    message = ResourceProvider.getString(
                        stringResId = R.string.delete_contact_message,
                        contact.name.truncateStringWithDots(), contact.phoneNumber
                    )
                )
            } catch (e: Exception) {
                Log.d(TAG, "removeContact: e: $e")
                updateUserMessageByException(e)
            }
        }
    }

    fun getContacts() {
        contactsJob?.cancel()
        contactsJob = viewModelScope.launch {
            _isLoading.emit(true)
            repository.fetchContacts().catch {
                _isLoading.emit(false)
                updateUserMessage(ResourceProvider.getString(R.string.unexpected_error_msg))
            }.collect { contactEntities ->
                val contacts = contactEntities.map { it.toContact() }
                _uiState.update { currentUiState ->
                    currentUiState.copy(contacts = contacts)
                }
                _isLoading.emit(false)
            }
        }
    }

    private fun updateUserMessageByException(e: Exception) {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = ExceptionUtils.getUiMessageOfException(e))
        }
    }

    private fun updateUserMessage(message: String) {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = message)
        }
    }
}