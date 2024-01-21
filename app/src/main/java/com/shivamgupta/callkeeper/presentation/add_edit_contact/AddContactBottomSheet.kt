package com.shivamgupta.callkeeper.presentation.add_edit_contact

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
import com.shivamgupta.callkeeper.domain.models.AddEditContactEvent
import com.shivamgupta.callkeeper.util.PermissionUtils
import com.shivamgupta.callkeeper.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactBottomSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddContactSheetBinding
    private val viewModel: AddEditContactViewModel by activityViewModels()

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
            viewModel.onEvent(AddEditContactEvent.GetContactDetails(contactUri = it))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_contact_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        setupAddContactFlow()
        setupContactChooser()
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
    }

    private fun setupAddContactFlow() {
        binding.addContactButton.setOnClickListener {
            viewModel.onEvent(AddEditContactEvent.AddContact)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                if(state.isContactSaved){
                    dismiss()
                }

                state.userMessage?.let { message ->
                    toast(message)
                    viewModel.userMessageShown()
                }
            }
        }
    }

    companion object {
        const val TAG = "ADD_CONTACT_SHEET"
    }
}