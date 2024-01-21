package com.shivamgupta.callkeeper.core

import android.net.Uri
import android.os.Build
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.Connection
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.Q)
class IncomingCallScreeningService : CallScreeningService() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onScreenCall(callDetails: Call.Details) {
        val isIncoming = callDetails.callDirection == Call.Details.DIRECTION_INCOMING
        if (isIncoming) {
            val handle: Uri = callDetails.handle

            when (callDetails.callerNumberVerificationStatus) {
                Connection.VERIFICATION_STATUS_PASSED -> {
                    val incomingNumber = handle.schemeSpecificPart
                }

                else -> {
                    //Phone number is not verified.
                }
            }

        }
    }
}