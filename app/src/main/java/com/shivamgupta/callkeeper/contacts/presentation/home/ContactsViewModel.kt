package com.shivamgupta.callkeeper.contacts.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.contacts.domain.contact.toContact
import com.shivamgupta.callkeeper.contacts.domain.contact.Contact
import com.shivamgupta.callkeeper.contacts.domain.contact.ContactEntity
import com.shivamgupta.callkeeper.contacts.domain.repository.ContactsRepository
import com.shivamgupta.callkeeper.util.ExceptionUtils
import com.shivamgupta.callkeeper.util.ResourceProvider
import com.shivamgupta.callkeeper.util.truncateStringWithDots
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState get() = _uiState.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        updateUserMessageFromException(throwable)
    }

    private var contactsJob: Job? = null

    fun updateContactSelectionStatus(isSelected: Boolean, id: Long) {
        viewModelScope.launch(context = exceptionHandler) {
            repository.updateContactSelectStatus(isSelected, id)
        }
    }

    fun removeContact(contact: Contact) {
        viewModelScope.launch(context = exceptionHandler) {
            repository.deleteContact(contact.id)

            val message = ResourceProvider.getString(
                stringResId = R.string.delete_contact_message,
                contact.name.truncateStringWithDots(),
                contact.phoneNumber
            )

            _uiState.update { currentUiState ->
                currentUiState.copy(userMessage = message)
            }
        }
    }

    fun getContacts() {
        contactsJob?.cancel()
        contactsJob = viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            repository.fetchContacts().catch {
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        userMessage = ResourceProvider.getString(R.string.unexpected_error_msg),
                        isLoading = false
                    )
                }
            }.collect { contactEntities ->
                val contacts = contactEntities.map(ContactEntity::toContact)
                _uiState.update { currentUiState ->
                    currentUiState.copy(
                        contacts = contacts,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateUserMessageFromException(t: Throwable) {
        _uiState.update { currentUiState ->
            currentUiState.copy(userMessage = ExceptionUtils.getUiMessageOfException(t))
        }
    }
}