package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toContact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.AddEditContactEvent
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.ContactsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val repository: ContactsRepositoryImpl
): ViewModel(){

    private val _contact = MutableStateFlow<Contact?>(null)
    val contact get() = _contact.asStateFlow()

    private val _pickedContact= MutableStateFlow(null)
    val pickedContact get() = _pickedContact.asStateFlow()

    fun onEvent(event: AddEditContactEvent){
        when(event){
            is AddEditContactEvent.AddContact -> {

            }

            is AddEditContactEvent.FindContact -> {

            }

            is AddEditContactEvent.GetContactDetails -> {

            }

            is AddEditContactEvent.UpdateContact -> {

            }
        }
    }

    fun findContactByPhoneNumber(phoneNumber: String){
        viewModelScope.launch {
            val contact = repository.fetchContactByPhoneNumber(phoneNumber)
            _contact.emit(contact?.toContact())
        }
    }

    fun addContact() {
        /*viewModelScope.launch {
            try {
                repository.insertContact(
                    if (contactEntity.smsMessage.isEmpty()) {
                        contactEntity.copy(smsMessage = defaultMessage.orEmpty())
                    } else {
                        contactEntity
                    }
                )
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }*/
    }

}