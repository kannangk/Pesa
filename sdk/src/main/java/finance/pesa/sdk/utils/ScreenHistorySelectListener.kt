package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.ScreenSMS

interface ScreenHistorySelectListener {
    fun onSelectedMessage(messageDetails: finance.pesa.sdk.Model.ScreenSMS)
    fun onAccept(messageDetails: finance.pesa.sdk.Model.ScreenSMS, position: Int)
    fun onDecline(messageDetails: finance.pesa.sdk.Model.ScreenSMS, position: Int)
    fun onRemovedAll()
}