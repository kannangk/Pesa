package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.MarketData

interface SupplyListener {
    fun supplyTokens(supplyData: MarketData ,values:String)
}