package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentAddContactSheetBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.ContactEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.model.getPhoneNumberWithoutPrefixCode
import com.shivamgupta.callkeeper.feature_contacts.util.Constants
import com.shivamgupta.callkeeper.feature_contacts.util.Constants.MESSAGE_REGEX
import com.shivamgupta.callkeeper.feature_contacts.util.Constants.PHONE_NUMBER_REGEX
import com.shivamgupta.callkeeper.feature_contacts.util.Constants.TEXT_REGEX
import com.shivamgupta.callkeeper.feature_contacts.util.PermissionUtils
import com.shivamgupta.callkeeper.feature_contacts.util.afterTextChanged
import com.shivamgupta.callkeeper.feature_contacts.util.setErrorMessage
import com.shivamgupta.callkeeper.feature_contacts.util.showSnackBar
import com.shivamgupta.callkeeper.feature_contacts.util.textValue
import com.shivamgupta.callkeeper.feature_contacts.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddContactSheetBinding
    private var updateContactDetails = false
    private var phoneNumber: String? = null
    private val contactsViewModel: ContactsViewModel by activityViewModels()
    private var contact: ContactEntity? = null
    private var isPhoneNumberAlreadyExists = false

    private val requestContactsPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermissionGranted ->
        if (isPermissionGranted) {
            pickContactLauncher.launch(null)
        } else {
            toast(textResId = R.string.contact_permission_error_msg)
        }
    }

    private val pickContactLauncher = registerForActivityResult(
        ActivityResultContracts.PickContact()
    ) { contactUri ->
        contactUri?.let {
            contactsViewModel.getContactDetails(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            updateContactDetails = it.getBoolean(ARG_UPDATE_CONTACT_DETAILS)
            phoneNumber = it.getString(ARG_PHONE_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_contact_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.shouldUpdateContactDetails = updateContactDetails
        preFillContactDetails()
        setupAddContactFlow()
        setupContactChooser()
    }

    private fun preFillContactDetails() {
        lifecycleScope.launch(Dispatchers.Main) {
            contactsViewModel.getContact(phoneNumber ?: return@launch).collect {
                contact = it
                with(binding) {
                    contactNameLayout.textValue = it?.name.orEmpty()
                    phoneNumberLayout.textValue = it?.phoneNumber.orEmpty()
                    customMessageLayout.textValue = it?.smsMessage.orEmpty()
                }
            }
        }
    }

    private fun setupContactChooser() {
        binding.chooseContactBtn.setOnClickListener {
            val permissionAlreadyGranted = PermissionUtils.checkContactsPermission(requireContext())
            if (permissionAlreadyGranted) {
                pickContactLauncher.launch(null)
            } else {
                requestContactsPermission.launch(android.Manifest.permission.READ_CONTACTS)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            contactsViewModel.pickedContact.collectLatest { pickedContact ->
                pickedContact?.let {
                    binding.apply {
                        contactNameLayout.textValue = it.name
                        phoneNumberLayout.textValue = it.getPhoneNumberWithoutPrefixCode()
                    }
                }
            }
        }
    }
    

    private fun setupAddContactFlow() = with(binding) {
        binding.phoneNumberLayout.afterTextChanged { phoneNumber ->
            if (phoneNumber.length == Constants.PHONE_NUMBER_LENGTH) {
                val preFilledPhoneNumber = contact?.phoneNumber.orEmpty()
                if (updateContactDetails && preFilledPhoneNumber == phoneNumber) {
                    isPhoneNumberAlreadyExists = false
                } else {
                    lifecycleScope.launch(Dispatchers.Main) {
                        isPhoneNumberAlreadyExists = contactsViewModel.checkIfPhoneNumberExists(phoneNumber)
                    }
                }
            }
        }

        submitBtn.setOnClickListener {
            if (isInputValid()) {
                val contactName = contactNameLayout.textValue.trim()
                val phoneNumber = phoneNumberLayout.textValue.trim()
                val message = customMessageLayout.textValue.trim()

                if (contact?.name == contactName && contact?.phoneNumber == phoneNumber && contact?.smsMessage == message) {
                    dismiss()
                } else {
                    if (updateContactDetails) {
                        contactsViewModel.updateContact(
                            name = contactName, smsMessage = message, phoneNumber = phoneNumber, id = contact?.id ?: return@setOnClickListener
                        )
                    } else {
                        contactsViewModel.insertContact(
                            ContactEntity(
                                name = contactName,
                                phoneNumber = phoneNumber,
                                smsMessage = message,
                            )
                        )
                    }

                    dismiss()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            contactsViewModel.moduleError.collect {
                it?.let { errorMessage ->
                    showSnackBar(text = errorMessage, rootLayout = binding.root)
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
                binding.contactNameLayout.setErrorMessage(R.string.empty_name_error_msg)
                false
            }

            !contactName.matches(TEXT_REGEX) -> {
                binding.contactNameLayout.setErrorMessage(R.string.invalid_name_error_msg)
                false
            }

            phoneNumber.isEmpty() -> {
                binding.phoneNumberLayout.setErrorMessage(R.string.empty_phone_number_error_msg)
                false
            }

            !phoneNumber.matches(PHONE_NUMBER_REGEX) -> {
                binding.phoneNumberLayout.setErrorMessage(R.string.invalid_phone_number_error_msg)
                false
            }

            isPhoneNumberAlreadyExists -> {
                binding.phoneNumberLayout.setErrorMessage(R.string.duplicate_phone_number_error_msg)
                false
            }

            customMessage.isNotEmpty() && !customMessage.matches(MESSAGE_REGEX) -> {
                binding.customMessageLayout.setErrorMessage(R.string.invalid_custom_message_error_msg)
                false
            }

            else -> {
                true
            }

        }
    }

    companion object {
        const val TAG = "ADD_CONTACT_SHEET"
        private const val ARG_UPDATE_CONTACT_DETAILS = "ARG_UPDATE_CONTACT_DETAILS"
        private const val ARG_PHONE_NUMBER = "ARG_PHONE_NUMBER"

        fun newInstance(updateContactDetails: Boolean, phoneNumber: String): AddContactBottomSheet {
            return AddContactBottomSheet().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_UPDATE_CONTACT_DETAILS, updateContactDetails)
                    putString(ARG_PHONE_NUMBER, phoneNumber)
                }
            }
        }
    }
}