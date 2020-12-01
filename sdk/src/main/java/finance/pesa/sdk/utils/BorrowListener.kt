package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.MarketData

interface BorrowListener {
    fun borrowToken(borrowData: MarketData, values:String)
}