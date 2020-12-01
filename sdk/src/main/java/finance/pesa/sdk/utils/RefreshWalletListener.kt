package finance.pesa.sdk.utils

interface RefreshWalletListener {
    fun onRefreshWallet()
    fun onRefreshInvest()
    fun onMarketLoaded()
}