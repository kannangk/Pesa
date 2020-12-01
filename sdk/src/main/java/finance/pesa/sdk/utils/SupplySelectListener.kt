package finance.pesa.sdk.utils

import android.widget.Switch
import finance.pesa.sdk.Model.MarketData

interface SupplySelectListener {
    fun onSupplyItemSelected(suppliedData: MarketData)
    fun collateralClick(isEnable: Boolean,switchCollateral:Switch,suppliedData: MarketData)
}