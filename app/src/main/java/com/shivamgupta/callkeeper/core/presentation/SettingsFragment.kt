package com.shivamgupta.callkeeper.core.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.databinding.FragmentSettingsBinding
import com.shivamgupta.callkeeper.contacts.presentation.home.ContactsViewModel
import com.shivamgupta.callkeeper.util.PermissionUtils
import com.shivamgupta.callkeeper.util.ResourceProvider
import com.shivamgupta.callkeeper.util.showSnackBar
import com.shivamgupta.callkeeper.util.editTextValue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    val binding get() = _binding!!

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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
                when{
                    PermissionUtils.isAllRequiredPermissionsGranted(requireContext()) -> {
                        viewModel.enableOrDisableCallRejection(isChecked)
                    }

                    !shouldShowPermissionInfoDialog() -> {

                    }

                    else -> {
                        switch.isChecked = false
                        requestMultiplePermissionLauncher.launch(PermissionUtils.requiredPermissions.toTypedArray())
                    }
                }
            }
        }
        
        binding.applyDefaultMsgSwitch.setOnCheckedChangeListener{ _, isChecked ->
            viewModel.updateApplyDefaultMsgGlobally(isChecked)
        }

        setupDefaultMsgInputBox()
    }

    private fun shouldShowPermissionInfoDialog(): Boolean {
        return PermissionUtils.requiredPermissions.any { permission ->
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                permission
            )
        }
    }

    private fun setupDefaultMsgInputBox() {
        binding.applyChangesButton.setOnClickListener {
            val message = binding.defaultMsgInputLayout.editTextValue
            if (message.isEmpty()) {
                showSnackBar(ResourceProvider.getString(R.string.empty_default_msg_error), binding.rootLayout)
            } else {
                viewModel.changeDefaultMessage(message)
                showSnackBar(ResourceProvider.getString(R.string.default_message_updated), binding.rootLayout)
            }
        }
    }

    private fun showPermissionDialog(){
        MaterialAlertDialogBuilder(
            requireContext(),
            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
        ).apply {
            setTitle("Allow access")
            setMessage("To automatically decline calls and send sms message, the app requires these permissions")
            setIcon(R.drawable.ic_shield_fill_check)
            setPositiveButton("Allow") { dialog, index ->

            }
            setNegativeButton("Deny") { dialog, index ->

            }
        }.show()
    }
}