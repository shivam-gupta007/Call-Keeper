package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentAddContactSheetBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.util.Constants.PHONE_NUMBER_REGEX
import com.shivamgupta.callkeeper.feature_contacts.util.Constants.TEXT_REGEX
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import com.shivamgupta.callkeeper.feature_contacts.util.checkContactsPermission
import com.shivamgupta.callkeeper.feature_contacts.util.gone
import com.shivamgupta.callkeeper.feature_contacts.util.launchMain
import com.shivamgupta.callkeeper.feature_contacts.util.setErrorMessage
import com.shivamgupta.callkeeper.feature_contacts.util.showSnackBar
import com.shivamgupta.callkeeper.feature_contacts.util.showToast
import com.shivamgupta.callkeeper.feature_contacts.util.textValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddContactSheetBinding
    private var customMessageMode = false
    private val viewModel: AddContactViewModel by viewModels()

    private val requestContactsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermissionGranted ->
        if (isPermissionGranted) {
            requestPickContactLauncher.launch(null)
        } else {
            showToast(text = "Contacts Permission Denied.")
        }
    }

    private val requestPickContactLauncher = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { contactUri ->

        if (contactUri != null) {
            val contact = viewModel.getContactDetails(contactUri)
            showToast("Name: ${contact?.name} PhoneNumber: ${contact?.phoneNumber}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            customMessageMode = it.getBoolean(CUSTOM_MESSAGE_MODE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_contact_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        setupCustomMessageView()
        setupContactChooser()
        setupAddContactFlow()
    }

    private fun setupAddContactFlow() = with(binding) {
        submitBtn.setOnClickListener {
            if (isInputValid()) {
                val contactName = contactNameLayout.textValue.trim()
                val phoneNumber = phoneNumberLayout.textValue.trim()
                val message = customMessageLayout.textValue.trim()

                viewModel.insertContact(
                    ContactEntity(
                        name = contactName,
                        phoneNumber = phoneNumber,
                        smsMessage = message,
                        defaultPhotoColor = Contact.getRandomColor(),
                        isContactSelected = false
                    )
                )

                dismiss()
            }
        }

        launchMain {
            viewModel.moduleError.collect {
                it?.let { errorMessage ->
                    showSnackBar(
                        text = errorMessage,
                        rootLayout = binding.root
                    )
                }
            }
        }
    }

    private fun isInputValid(): Boolean {
        val contactName = binding.contactNameLayout.textValue
        val phoneNumber = binding.phoneNumberLayout.textValue
        val customMessage = binding.customMessageLayout.textValue

        return when {
            contactName.isEmpty() -> {
                binding.contactNameLayout.setErrorMessage(
                    ResourceProvider.getString(R.string.empty_name_error_msg)
                )

                false
            }

            !contactName.matches(TEXT_REGEX) -> {
                binding.contactNameLayout.setErrorMessage(
                    ResourceProvider.getString(R.string.invalid_name_error_msg)
                )

                false
            }

            phoneNumber.isEmpty() -> {
                binding.phoneNumberLayout.setErrorMessage(
                    ResourceProvider.getString(R.string.empty_phone_number_error_msg)
                )
                false
            }

            !phoneNumber.matches(PHONE_NUMBER_REGEX) -> {
                binding.phoneNumberLayout.setErrorMessage(
                    ResourceProvider.getString(R.string.invalid_phone_number_error_msg)
                )
                false
            }

            customMessage.isNotEmpty() && !customMessage.matches(TEXT_REGEX) -> {
                binding.customMessageLayout.setErrorMessage(
                    ResourceProvider.getString(R.string.invalid_custom_message_error_msg)
                )
                false
            }

            else -> {

                true
            }

        }
    }

    private fun setupCustomMessageView() {
        if (customMessageMode) {
            with(binding) {
                submitBtn.text = getString(R.string.label_submit)
                contactNameLayout.gone()
                phoneNumberLayout.gone()
                chooseContactBtn.gone()
                orLabelTv.gone()
            }
        }
    }

    private fun setupContactChooser() {
        binding.chooseContactBtn.setOnClickListener {
            val permissionAlreadyGranted = checkContactsPermission(requireContext())
            if (permissionAlreadyGranted) {
                requestPickContactLauncher.launch(null)
            } else {
                requestContactsPermission.launch(android.Manifest.permission.READ_CONTACTS)
            }
        }
    }

    companion object {
        const val TAG = "ADD_CONTACT_SHEET"
        private const val CUSTOM_MESSAGE_MODE = "CUSTOM_MESSAGE_MODE"

        fun newInstance(customMessageMode: Boolean): AddContactSheet {
            return AddContactSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(CUSTOM_MESSAGE_MODE, customMessageMode)
                }
            }
        }
    }
}