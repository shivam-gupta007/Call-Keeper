package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentHomeBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toContact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import com.shivamgupta.callkeeper.feature_contacts.util.showSnackBar
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
        binding.contactsRecyclerView.adapter = ContactsAdapter(
            items = contactEntities.map { it.toContact() },
            onItemClick = { contact ->
                AddContactSheet.newInstance(
                    updateContactDetails = true,
                    phoneNumber = contact.phoneNumber
                ).show(childFragmentManager, AddContactSheet.TAG)
            },
            onItemLongPressed = { contact ->
                showDeleteContactDialog(contact)
            },
            onContactSelect = { position, isSelected ->
                val contactId = contactEntities[position].id
                viewModel.updateContactSelectionStatus(isSelected, contactId)
            }
        )
    }

    private fun showDeleteContactDialog(contact: Contact) {
        val contactInfo = "${contact.name}\n${contact.phoneNumber}"
        MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
            .setTitle(ResourceProvider.getString(R.string.delete_contact_confirmation_msg))
            .setMessage(contactInfo)
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(ResourceProvider.getString(R.string.delete_contact)) { dialog, index ->
                viewModel.deleteContact(phoneNumber = contact.phoneNumber)

                showSnackBar(text = "${contact.name} - ${contact.phoneNumber} deleted", binding.rootLayout)

                dialog.dismiss()
            }.setNegativeButton(ResourceProvider.getString(R.string.cancel_contact)) { dialog, index ->
                dialog.dismiss()
            }.show()
    }
}