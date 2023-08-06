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
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.util.launchMain
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: AddContactViewModel by viewModels()

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

        lifecycleScope.launch(Dispatchers.Main) {
            val contacts = viewModel.getContacts().map {
                it.toContact()
            }
            setupContacts(contacts)
        }

        addContactFab.setOnClickListener {
            AddContactSheet().show(childFragmentManager, AddContactSheet.TAG)
        }
    }

    private fun setupContacts(contacts: List<Contact>) {
        binding.contactsRv.adapter = ContactsAdapter(
            items = contacts,
            onItemClick = {
                AddContactSheet.newInstance(customMessageMode = true)
                    .show(childFragmentManager, AddContactSheet.TAG)
            }
        )
    }
}