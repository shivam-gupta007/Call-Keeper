package com.shivamgupta.autocallblocker.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shivamgupta.autocallblocker.R
import com.shivamgupta.autocallblocker.databinding.FragmentHomeBinding
import com.shivamgupta.autocallblocker.feature_contacts.domain.model.Contact

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        val items = (0..50).map {
            Contact(
                name = "Person $it",
                phoneNumber = "+91 969005329$it",
                defaultPhotoColor = Contact.getRandomColor()
            )
        }

        contactsRv.adapter = ContactsAdapter(
            items = items,
            onItemClick = {
                AddContactSheet.newInstance(customMessageMode = true)
                    .show(childFragmentManager,AddContactSheet.TAG)
            }
        )

        addContactFab.setOnClickListener {
            AddContactSheet().show(childFragmentManager, AddContactSheet.TAG)
        }
    }
}