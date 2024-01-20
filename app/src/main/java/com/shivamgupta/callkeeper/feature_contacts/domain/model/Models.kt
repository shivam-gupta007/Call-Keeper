package com.shivamgupta.callkeeper.feature_contacts.domain.model

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val userMessage: String? = null
)