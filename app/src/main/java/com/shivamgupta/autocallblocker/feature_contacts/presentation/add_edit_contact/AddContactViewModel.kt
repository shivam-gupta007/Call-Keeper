package com.shivamgupta.autocallblocker.feature_contacts.presentation.add_edit_contact

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class AddContactViewModel : ViewModel(){

    val customMessageMode = MutableStateFlow<Boolean?>(null)

}