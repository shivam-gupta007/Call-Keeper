package com.shivamgupta.callkeeper.history.presentation

import com.shivamgupta.callkeeper.history.domain.call_log.CallLog

data class HistoryUiState(
    val callLogs: List<CallLog> = emptyList(),
    val userMessage: String? = null,
    val isLoading: Boolean = false
)