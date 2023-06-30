package com.shivamgupta.autocallblocker.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shivamgupta.autocallblocker.R
import com.shivamgupta.autocallblocker.databinding.FragmentAddContactSheetBinding

class AddContactSheet : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddContactSheetBinding
    private var customMessageMode = false

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
        if (customMessageMode) {
            submitBtn.text = "Submit"
            contactNameLayout.visibility = View.GONE
            phoneNumberLayout.visibility = View.GONE
            chooseContactBtn.visibility = View.GONE
            orLabelTv.visibility = View.GONE
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