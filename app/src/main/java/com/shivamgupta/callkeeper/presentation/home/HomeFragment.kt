package com.shivamgupta.callkeeper.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentHomeBinding
import com.shivamgupta.callkeeper.domain.models.AddEditContactEvent
import com.shivamgupta.callkeeper.domain.models.Contact
import com.shivamgupta.callkeeper.presentation.add_edit_contact.AddContactBottomSheet
import com.shivamgupta.callkeeper.presentation.add_edit_contact.AddEditContactViewModel
import com.shivamgupta.callkeeper.presentation.add_edit_contact.EditContactBottomFragment
import com.shivamgupta.callkeeper.util.ResourceProvider
import com.shivamgupta.callkeeper.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    private val viewModel: ContactsViewModel by activityViewModels()
    private val addEditContactViewModel: AddEditContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getContacts()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.flowWithLifecycle(lifecycle).collect { currentUiState ->
                setupContacts(contacts = currentUiState.contacts)

                binding.apply {
                    contactsRecyclerView.isVisible = !currentUiState.isLoading
                    progressBar.isVisible = currentUiState.isLoading
                    addContactFab.isVisible = !currentUiState.isLoading
                }

                currentUiState.userMessage?.let { message ->
                    showSnackBar(text = message, rootLayout = binding.rootLayout)
                }
            }
        }

        binding.addContactFab.setOnClickListener {
            openAddContactSheet()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openAddContactSheet() {
        addEditContactViewModel.clearState()
        AddContactBottomSheet().show(parentFragmentManager, AddContactBottomSheet.TAG)
    }

    private fun setupContacts(contacts: List<Contact>) {
        val adapter = ContactsAdapter(
            items = contacts,
            onContactClicked = { contact ->
                openEditContactSheet(contactId = contact.id)
            },
            onItemLongPressed = { contact ->
                showDeleteContactDialog(contact)
            }, onContactChecked = { position, isSelected ->
                toggleContactSelection(
                    contactId = contacts[position].id, isChecked = isSelected
                )
            }
        )

        binding.contactsRecyclerView.adapter = adapter
    }

    private fun toggleContactSelection(contactId: Long, isChecked: Boolean) {
        viewModel.updateContactSelectionStatus(isChecked, contactId)
    }

    private fun openEditContactSheet(contactId: Long) {
        addEditContactViewModel.onEvent(AddEditContactEvent.SetContactId(contactId))
        EditContactBottomFragment().show(parentFragmentManager, AddContactBottomSheet.TAG)
    }

    private fun showDeleteContactDialog(contact: Contact) {
        MaterialAlertDialogBuilder(
                requireContext(),
                com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
            .setTitle(ResourceProvider.getString(R.string.delete_contact_confirmation_msg))
            .setMessage(getString(R.string.delete_contact_info, contact.name, contact.phoneNumber))
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(ResourceProvider.getString(R.string.delete_contact)) { dialog, _ ->
                viewModel.removeContact(contact)
                dialog.dismiss()
            }.setNegativeButton(ResourceProvider.getString(R.string.cancel_contact)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}