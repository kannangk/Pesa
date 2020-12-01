package finance.pesa.sdk.view.Interface

import finance.pesa.sdk.Model.ActivityDetails

interface RequestActivityListerner {
    fun onRequestClicked(activityDetails: ActivityDetails)
    fun onCliamClicked(activityDetails: ActivityDetails)
}