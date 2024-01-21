package com.shivamgupta.callkeeper.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import com.shivamgupta.callkeeper.data.repository.AppPreferencesRepositoryImpl
import com.shivamgupta.callkeeper.domain.mapper.toCallLogEntity
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.models.ContactEntity
import com.shivamgupta.callkeeper.domain.repository.AppPreferencesRepository
import com.shivamgupta.callkeeper.domain.repository.CallLogsRepository
import com.shivamgupta.callkeeper.domain.repository.ContactsRepository
import com.shivamgupta.callkeeper.util.Constants
import com.shivamgupta.callkeeper.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class IncomingCallReceiver : BroadcastReceiver() {

    @Inject lateinit var contactsRepository: ContactsRepository
    @Inject lateinit var callLogsRepository: CallLogsRepository
    @Inject lateinit var appPreferencesRepository: AppPreferencesRepository

    private var job: Job? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == Constants.PHONE_STATE_ACTION){
           val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
           val incomingPhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

           if(state == TelephonyManager.EXTRA_STATE_RINGING){
               if(incomingPhoneNumber != null && context != null){
                   handleIncomingCall(incomingPhoneNumber,context)
               }
           }
        }
    }

    private fun handleIncomingCall(phoneNumber: String, context: Context) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val contact: ContactEntity? = contactsRepository.fetchContactByPhoneNumber(phoneNumber.substringAfter("+91"))
            val message = if(contact?.useDefaultMessage == true) appPreferencesRepository.fetchDefaultMessage().first() else contact?.smsMessage.orEmpty()

            if (contact?.isContactSelected == true) {
                withContext(Dispatchers.Main) {
                    checkPermissionAndSendMessage(context,contact,phoneNumber,message)
                }
            }
        }
    }

    private suspend fun checkPermissionAndSendMessage(
        context: Context,
        contact: ContactEntity,
        phoneNumber: String,
        smsMessage: String
    ){
        val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
        val smsManager = SmsManager.getDefault()
        if (PermissionUtils.checkAnswerPhoneCallsPermission(context) && PermissionUtils.checkSendSmsPermission(context)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                telecomManager.endCall()
            }

            smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null)

            contact.toCallLogEntity(smsMessage).also { addCallLog(it) }
        }
    }

    private suspend fun addCallLog(callLogEntity: CallLogEntity) {
        withContext(Dispatchers.IO){
            callLogsRepository.insertCallLog(callLogEntity)
        }
    }

    fun clearJob(){
        job?.cancel()
    }
}