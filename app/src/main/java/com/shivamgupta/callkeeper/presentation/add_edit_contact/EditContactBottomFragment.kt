package com.shivamgupta.callkeeper.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.shivamgupta.callkeeper.databinding.FragmentEditContactBottomBinding
import com.shivamgupta.callkeeper.domain.models.AddEditContactEvent
import com.shivamgupta.callkeeper.util.toast
import kotlinx.coroutines.launch


class EditContactBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentEditContactBottomBinding? = null
    val binding get() = _binding!!

    private val viewModel: AddEditContactViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditContactBottomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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