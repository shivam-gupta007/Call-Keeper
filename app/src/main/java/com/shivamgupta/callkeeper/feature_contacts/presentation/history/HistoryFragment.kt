package com.shivamgupta.callkeeper.feature_contacts.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shivamgupta.callkeeper.databinding.FragmentHistoryBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLog
import com.shivamgupta.callkeeper.feature_contacts.domain.model.Contact


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val items = (0..20).map {
            CallLog(
                name = "Person $it",
                phoneNumber = "9005080000",
                smsMessage = "Currently, I am working on a project. I will call you later.",
                date = "27 June 2023 11:35 PM",
                defaultPhotoColor = Contact.getRandomColor()
            )
        }
        binding.callLogsRv.adapter = CallLogsAdapter(items)
    }
}