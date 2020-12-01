package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.MarketData

interface WithdrawListener {
    fun withdrawTokens(supplyData: MarketData,values:String)
}