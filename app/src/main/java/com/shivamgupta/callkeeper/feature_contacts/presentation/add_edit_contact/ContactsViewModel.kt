package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toContact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactsUiState
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactsViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl, private val appPreferencesRepository: AppPreferencesRepositoryImpl
) : ViewModel() {

    private val _uiState = MutableStateFlow(ContactsUiState())
    val uiState get() = _uiState.asStateFlow()

    private var contactsJob: Job? = null

    private val _moduleError = MutableStateFlow<String?>(null)
    val moduleError: StateFlow<String?> get() = _moduleError

    private val _contacts = MutableStateFlow<List<ContactEntity>?>(null)
    val contacts get() = _contacts.asStateFlow()


    private var defaultMessage: String? = null

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    init {
        getDefaultMessage()
    }

    private fun getDefaultMessage() {
        viewModelScope.launch {
            defaultMessage = appPreferencesRepository.fetchDefaultMessage().firstOrNull()
        }
    }

    fun updateContactSelectionStatus(isSelected: Boolean, id: Long) {
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


    fun getContacts() {
        contactsJob?.cancel()
        contactsJob = viewModelScope.launch {
            _uiState.update { currentUiState ->
                currentUiState.copy(isFetchingContacts = true)
            }

            repository.fetchContacts().catch {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isFetchingContacts = false,
                            userMessage = ResourceProvider.getString(R.string.unexpected_error_msg)
                        )
                    }
                }.collect { contactEntities ->
                    val contacts = contactEntities.map { it.toContact() }
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isFetchingContacts = false,
                            contacts = contacts
                        )
                    }
                }
        }
    }
}