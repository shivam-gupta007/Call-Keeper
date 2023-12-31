package com.shivamgupta.callkeeper.feature_contacts.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shivamgupta.callkeeper.R
import com.shivamgupta.callkeeper.feature_contacts.domain.model.CallLogEntity
import com.shivamgupta.callkeeper.feature_contacts.domain.repository.CallLogsRepositoryImpl
import com.shivamgupta.callkeeper.feature_contacts.util.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CallLogsViewModel @Inject constructor(
    val repository: CallLogsRepositoryImpl
) : ViewModel() {

    private val _moduleError = MutableStateFlow<String?>(null)
    val moduleError: StateFlow<String?> get() = _moduleError

    private val _callLogs = MutableStateFlow<List<CallLogEntity>>(emptyList())
    val callLogs: StateFlow<List<CallLogEntity>> get() = _callLogs

    fun getCallLogs() {
        viewModelScope.launch {
            repository.fetchCallLogs()
                .flowOn(Dispatchers.IO)
                .catch {
                    _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
                }
                .collect {
                    _callLogs.emit(it)
                }
        }
    }

    fun insertCallLog(callLogEntity: CallLogEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.insertCallLog(callLogEntity)
            } catch (exception: Exception) {
                _moduleError.emit(ResourceProvider.getString(R.string.unexpected_error_msg))
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