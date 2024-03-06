package com.shivamgupta.callkeeper.history.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.history.domain.call_log.CallLogEntity
import com.shivamgupta.callkeeper.history.domain.call_log.toCallLog
import com.shivamgupta.callkeeper.history.domain.repository.CallLogsRepository
import com.shivamgupta.callkeeper.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    val repository: CallLogsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState get() = _uiState.asStateFlow()

    fun getCallLogs() {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true) }

            repository.fetchCallLogs().catch {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = false,
                            userMessage = ResourceProvider.getString(R.string.unexpected_error_msg)
                        )
                    }
                }.collect {
                    _uiState.update { currentUiState ->
                        currentUiState.copy(
                            isLoading = false,
                            callLogs = it.map(CallLogEntity::toCallLog)
                        )
                    }
                }
        }
    }

}