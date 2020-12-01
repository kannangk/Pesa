package finance.pesa.sdk.view.Interface

import finance.pesa.sdk.Model.MarketData

interface EnableSupplyListener {
     fun onClickedEnable(usdcAddress:String,usdcMarketAddress:String,marketData: MarketData)
}