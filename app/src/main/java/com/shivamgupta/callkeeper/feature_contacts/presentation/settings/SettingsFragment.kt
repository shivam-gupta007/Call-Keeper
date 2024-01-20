package com.shivamgupta.callkeeper.feature_contacts.presentation.settings

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentSettingsBinding
import com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact.ContactsViewModel
import com.shivamgupta.callkeeper.feature_contacts.util.PermissionUtils
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import com.shivamgupta.callkeeper.feature_contacts.util.showSnackBar
import com.shivamgupta.callkeeper.feature_contacts.util.textValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by activityViewModels()
    private val contactsViewModel: ContactsViewModel by activityViewModels()

    private val requestMultiplePermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ){ permissions ->
        val allPermissionsGranted = permissions.all { it.value }
        if(allPermissionsGranted){
            binding.enableFeatureSwitch.isChecked = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }
        setupViews()
    }

    private fun setupViews() {
        binding.enableFeatureSwitch.setOnCheckedChangeListener { switch, isChecked ->
            val isContactsEmpty = contactsViewModel.uiState.value.contacts.isEmpty()
            if (isContactsEmpty) {
                switch.isChecked = false
                showSnackBar(text = ResourceProvider.getString(R.string.empty_contacts_message), rootLayout = binding.rootLayout)
            } else {
                if (PermissionUtils.isAllRequiredPermissionsGranted(requireContext())) {
                    viewModel.enableOrDisableCallRejection(isChecked)
                } else {
                    switch.isChecked = false
                    requestMultiplePermissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ANSWER_PHONE_CALLS,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.SEND_SMS,
                            Manifest.permission.READ_CALL_LOG,
                        )
                    )
                }
            }
        }
        
        binding.applyDefaultMsgSwitch.setOnCheckedChangeListener{ _, isChecked ->
            viewModel.updateApplyDefaultMsgGlobally(isChecked)
        }

        binding.applyChangesButton.setOnClickListener {
            val message = binding.defaultMsgInputLayout.textValue
            viewModel.changeDefaultMessage(message)
            showSnackBar(ResourceProvider.getString(R.string.default_message), binding.rootLayout)
        }

    }
}