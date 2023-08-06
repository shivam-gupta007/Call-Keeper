package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddContactViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl
) : ViewModel() {

    val customMessageMode = MutableStateFlow<Boolean?>(null)
    private val _moduleError = MutableStateFlow<String?>(null)
    val moduleError: StateFlow<String?> get() = _moduleError

    fun getContactDetails(contactUri: Uri): Contact? {
        return repository.extractContactDetails(contactUri)
    }

    fun insertContact(contactEntity: ContactEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertContact(contactEntity)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

    suspend fun getContacts(): List<ContactEntity> {
        return repository.getContacts()
    }


}