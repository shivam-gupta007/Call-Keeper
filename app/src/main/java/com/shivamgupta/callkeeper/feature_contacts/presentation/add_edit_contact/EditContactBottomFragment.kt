package com.shivamgupta.callkeeper.feature_contacts.presentation.add_edit_contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.shivamgupta.callkeeper.databinding.FragmentEditContactBottomBinding


class EditContactBottomFragment : Fragment() {

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
    }

    companion object {
        const val TAG = "EDIT_CONTACT_SHEET"
    }
}