package com.shivamgupta.callkeeper.presentation.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.shivamgupta.callkeeper.databinding.FragmentHistoryBinding
import com.shivamgupta.callkeeper.domain.mapper.toCallLog
import com.shivamgupta.callkeeper.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
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
        binding.apply {
            vm = viewModel
            lifecycleOwner = viewLifecycleOwner
        }

        viewModel.getCallLogs()
        setupViews()
    }

    private fun setupViews() {
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.callLogs.collect { callLogs ->
                callLogs?.let {
                    binding.callLogsRv.adapter = CallLogsAdapter(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            viewModel.moduleError.collectLatest { message ->
                message?.let {
                    showSnackBar(text = it, rootLayout = binding.root)
                }
            }
        }
    }
}