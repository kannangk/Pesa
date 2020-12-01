package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.MarketData

interface RepayListener {
    fun repayTokens(borrowData: MarketData, values:String)
}