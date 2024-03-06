package com.shivamgupta.callkeeper.history.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shivamgupta.callkeeper.databinding.FragmentHistoryBinding
import com.shivamgupta.callkeeper.history.domain.call_log.CallLog
import com.shivamgupta.callkeeper.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    val binding get() = _binding!!

    private val viewModel: HistoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
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
        viewModel.getCallLogs()
        
        viewLifecycleOwner.lifecycleScope.launch { 
            viewModel.uiState.flowWithLifecycle(lifecycle).collect { currentUiState ->
                binding.apply { 
                    progressBar.isVisible = currentUiState.isLoading    
                    callLogsRecyclerView.isVisible = !currentUiState.isLoading
                }

                currentUiState.userMessage?.let {
                    showSnackBar(
                        text = it,
                        rootLayout = binding.root
                    )
                }

                setupCallLogsAdapter(currentUiState.callLogs)
            }    
        }   
    }

    private fun setupCallLogsAdapter(callLogs: List<CallLog>){
        binding.callLogsRecyclerView.adapter = CallLogsAdapter(callLogs)
    }
}