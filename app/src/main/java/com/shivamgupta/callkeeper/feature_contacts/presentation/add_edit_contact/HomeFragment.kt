package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentHomeBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.AddEditContactEvent
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import com.shivamgupta.callkeeper.feature_contacts.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: ContactsViewModel by activityViewModels()
    private val addEditContactViewModel: AddEditContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.getContacts()

        viewLifecycleOwner.lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
               viewModel.uiState.collect{ currentUiState ->
                   setupContacts(contacts = currentUiState.contacts)
               }
            }
        }

        binding.addContactFab.setOnClickListener {
            addEditContactViewModel.clearState()
            AddContactBottomSheet().show(parentFragmentManager, AddContactBottomSheet.TAG)
        }
    }

    private fun setupContacts(contacts: List<Contact>) {
        binding.contactsRecyclerView.adapter = ContactsAdapter(
            items = contacts,
            onItemClick = { contact ->
                addEditContactViewModel.onEvent(AddEditContactEvent.SetContactId(contact.id))
                EditContactBottomFragment().show(parentFragmentManager, AddContactBottomSheet.TAG)
            },
            onItemLongPressed = { contact ->
                showDeleteContactDialog(contact)
            },
            onContactChecked = { position, isSelected ->
                val contactId = contacts[position].id
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
            .setPositiveButton(ResourceProvider.getString(R.string.delete_contact)) { dialog, _ ->
                viewModel.deleteContact(phoneNumber = contact.phoneNumber)

                showSnackBar(text = "${contact.name} - ${contact.phoneNumber} deleted", binding.rootLayout)

                dialog.dismiss()
            }.setNegativeButton(ResourceProvider.getString(R.string.cancel_contact)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}