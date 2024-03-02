package com.shivamgupta.callkeeper.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import com.shivamgupta.callkeeper.domain.mapper.toCallLogEntity
import com.shivamgupta.callkeeper.domain.models.CallLogEntity
import com.shivamgupta.callkeeper.domain.models.ContactEntity
import com.shivamgupta.callkeeper.domain.repository.CoreFeatureRepository
import com.shivamgupta.callkeeper.util.Constants
import com.shivamgupta.callkeeper.util.PermissionUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@AndroidEntryPoint
class IncomingCallReceiver : BroadcastReceiver() {

    @Inject
    lateinit var coreFeatureRepository: CoreFeatureRepository

    private var job: Job? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == Constants.PHONE_STATE_ACTION) {
            val phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingPhoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
            val isRinging = (phoneState == TelephonyManager.EXTRA_STATE_RINGING)

            if (isRinging && incomingPhoneNumber != null && context != null) {
                handleIncomingCall(incomingPhoneNumber, context)
            }
        }
    }

    private fun handleIncomingCall(userPhoneNumber: String, context: Context) {
        job = CoroutineScope(Dispatchers.IO).launch {
            val contact: ContactEntity? = coreFeatureRepository.fetchContactIfFeatureEnabled(
                phoneNumber = userPhoneNumber.substringAfter(Constants.COUNTRY_CODE)
            )

            if (contact != null) {
                val defaultSmsMessage = coreFeatureRepository.fetchDefaultUserMessage()

                val message: String? = if (contact.useDefaultMessage) {
                    defaultSmsMessage
                } else if (contact.smsMessage.isEmpty()) {
                    defaultSmsMessage
                } else {
                    contact.smsMessage
                }

                withContext(Dispatchers.Main) {
                    message?.let {
                        checkPermissionAndSendMessage(
                            context = context, contactDetails = contact, phoneNumber = userPhoneNumber, smsMessage = it
                        )
                    }
                }
            } else {
                job?.cancel()
            }
        }
    }

    private suspend fun checkPermissionAndSendMessage(
        context: Context, contactDetails: ContactEntity, phoneNumber: String, smsMessage: String
    ) {
        try {
            val telecomManager = context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
            val smsManager = SmsManager.getDefault()
            if (PermissionUtils.isPhoneCallAndSmsPermissionGranted(context)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    telecomManager.endCall()
                }

                smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null)

                val userCallLog = contactDetails.toCallLogEntity(smsMessage)
                addCallLog(userCallLog)
            }
        } catch (e: SecurityException) {
            //TODO("Handle phone call permission.")
        } finally {
            job?.cancel()
        }
    }

    private suspend fun addCallLog(callLogEntity: CallLogEntity) {
        withContext(Dispatchers.IO) {
            coreFeatureRepository.insertUserCallLog(callLogEntity)
        }
    }

    fun clearJob() {
        job?.cancel()
    }
}