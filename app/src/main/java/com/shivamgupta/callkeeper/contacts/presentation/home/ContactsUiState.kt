package com.shivamgupta.callkeeper.contacts.presentation.home

import com.shivamgupta.callkeeper.contacts.domain.contact.Contact

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val userMessage: String? = null,
    val isLoading: Boolean = false,
)