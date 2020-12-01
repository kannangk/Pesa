package finance.pesa.sdk.view.Interface

import finance.pesa.sdk.Model.ActivitiesData

interface ActivitiesUpdateListener {
    fun onActivityUpdate(activitiesData: ActivitiesData)
}