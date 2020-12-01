package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.MarketData

interface BorrowSelectListener {
    fun onSBorrowItemSelected(borrowData: MarketData)
}