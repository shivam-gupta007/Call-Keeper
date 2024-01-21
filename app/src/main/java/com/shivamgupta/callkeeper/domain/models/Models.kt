package com.shivamgupta.callkeeper.domain.models

data class ContactsUiState(
    val contacts: List<Contact> = emptyList(),
    val userMessage: String? = null
)