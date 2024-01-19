package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shivamgupta.callkeeper.databinding.FragmentEditContactBottomBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.AddEditContactEvent
import com.shivamgupta.callkeeper.feature_contacts.util.showSnackBar
import com.shivamgupta.callkeeper.feature_contacts.util.toast
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class EditContactBottomFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditContactBottomBinding
    private val viewModel: AddEditContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditContactBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    private fun setupViews() {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
        }

        viewModel.onEvent(AddEditContactEvent.GetContact)

        binding.updateContactButton.setOnClickListener {
            updateContactDetails()
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

    private fun updateContactDetails() {
        viewModel.onEvent(AddEditContactEvent.UpdateContact)
    }
}