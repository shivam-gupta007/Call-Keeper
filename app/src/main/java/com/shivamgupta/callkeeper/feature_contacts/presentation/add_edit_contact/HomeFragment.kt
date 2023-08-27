package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.shivamgupta.callkeeper.databinding.FragmentHomeBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toContact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ContactsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        /*val items = (0..50).map {
            Contact(
                name = "Person $it",
                phoneNumber = "+91 969005329$it",
                defaultPhotoColor = Contact.getRandomColor(),
                isContactSelected =  true
            )
        }*/

        viewModel.getContacts()

        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.contacts.collect {
                setupContacts(it)
            }
        }

        addContactFab.setOnClickListener {
            AddContactSheet().show(childFragmentManager, AddContactSheet.TAG)
        }
    }

    private fun setupContacts(contactEntities: List<ContactEntity>) {
        binding.contactsRv.adapter = ContactsAdapter(
            items = contactEntities.map { it.toContact() },
            onItemClick = { contact ->
                AddContactSheet.newInstance(
                    updateContactDetails = true,
                    phoneNumber = contact.phoneNumber
                ).show(childFragmentManager, AddContactSheet.TAG)
            },
            onContactSelect = { position, isSelected ->
                val contactId = contactEntities[position].id
                viewModel.updateContactSelectStatus(isSelected, contactId)
            }
        )
    }
}