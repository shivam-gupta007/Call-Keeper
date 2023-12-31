package com.shivamgupta.callkeeper.feature_contacts.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.shivamgupta.callkeeper.databinding.FragmentHistoryBinding
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toCallLog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: CallLogsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.getCallLogs()
        setupViews()
    }

    private fun setupViews() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.callLogs.collect { callLogEntities ->
                binding.callLogsRv.adapter = CallLogsAdapter(callLogEntities.map { it.toCallLog() })
            }
        }
    }
}