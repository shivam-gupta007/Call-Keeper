package com.shivamgupta.callkeeper.feature_contacts.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.mapper.toCallLog
import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLog
import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLogEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.CallLogsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(
    val repository: CallLogsRepositoryImpl
) : ViewModel() {

    private val _moduleError = MutableStateFlow<String?>(null)
    val moduleError: StateFlow<String?> get() = _moduleError

    private val _isLoading = MutableStateFlow(false)
    val isLoading get() = _isLoading.asStateFlow()

    private val _callLogs = MutableStateFlow<List<CallLog>?>(null)
    val callLogs = _callLogs.asStateFlow()

    fun getCallLogs() {
        viewModelScope.launch {
            _isLoading.emit(true)

            repository.fetchCallLogs()
                .flowOn(Dispatchers.IO)
                .catch {
                    _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
                }
                .collect { callLogs ->
                    _isLoading.emit(false)
                    _callLogs.emit(callLogs.map { it.toCallLog() })
                }
        }
    }

    fun deleteCallLog(phoneNumber: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.removeCallLog(phoneNumber)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
            }
        }
    }

}