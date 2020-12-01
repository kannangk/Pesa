package finance.pesa.sdk.utils

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.*
import finance.pesa.sdk.PesaApplication
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.contracts.eip20.generated.ERC20
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.gas.DefaultGasProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FetchDataBackgroungService : IntentService("FetchDataBackgroungService") {
    var isStarted = false
    internal var mContext: Context? = null

    override fun onHandleIntent(intent: Intent?) {
    }

    override fun onDestroy() {
        super.onDestroy()
        isStarted = false
    }

    override fun onCreate() {
        super.onCreate()
        isStarted = true
    }

    fun getRefreshWallet(context: Context, isForce: Boolean) {
        this.mContext = context
        if (isForce) {
            LoadSupplyAndWalletTask(Constants.getAllMarkets()!!).execute()
        }
    }

    /*
        //get ETH to USD
        private fun getUSDFromETH(retryCount: Int, isForce: Boolean) {
            var call: Call<ETHTOUSD>? =
                ConversionApi.build()?.getUSDFromETH()
            call?.enqueue(object : Callback<ETHTOUSD> {
                override fun onFailure(call: Call<ETHTOUSD>, t: Throwable) {
                    if (retryCount < 3) {
                        val retryCountNew = retryCount + 1
                        getUSDFromETH(retryCountNew, isForce)
                    }
                }

                override fun onResponse(
                    call: Call<ETHTOUSD>,
                    response: Response<ETHTOUSD>
                ) {
                    response?.body()?.let {
                        if (response.isSuccessful) {
                            Constants.setUSDValueFromETH(it.USD)
                            if (isForce) {
                                LoadSupplyAndWalletTask(Constants.getAllMarkets()!!).execute()
                            }
                        }
                    } ?: run {
                        if (response.body() == null) {
                            if (retryCount < 3) {
                                val retryCountNew = retryCount + 1
                                getUSDFromETH(retryCountNew, isForce)
                            }
                        }
                    }
                }

            })

        }*/
//get account details
    fun loadFirstAllMarkets(retryCount: Int, context: Context) {
        var call: Call<BasicMarkets>? =
            ApiClient.build()?.getAllMarkets(Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<BasicMarkets> {
            override fun onFailure(call: Call<BasicMarkets>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    loadFirstAllMarkets(retryCountNew, context)
                }
            }

            override fun onResponse(
                call: Call<BasicMarkets>,
                response: Response<BasicMarkets>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null)
                            Constants.setAllMarkets(it.markets)
                        try {
                            var marketDataValues: HashMap<String, MarketValues>? = hashMapOf()
                            for (data in it.markets) {
                                if (data.underlyingSymbol == "ETH") {
                                    LoadETHTask(data).execute()
                                }
                                marketDataValues!![data.underlyingSymbol] =
                                    MarketValues(
                                        data,
                                        data.balance.toString(),
                                        data.isUnderlyingApproved
                                    )
                                Constants.setMarketValues(marketDataValues!!)
                            }
                            if (Constants.getRefreshWalletListeners().isNotEmpty()) {
                                for (listener in Constants.getRefreshWalletListeners()) {
                                    listener!!.onMarketLoaded()
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        // UserInterface.loadWalletDatas(context, true)
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            loadFirstAllMarkets(retryCountNew, context)
                        }
                    }
                }
            }

        })

    }

    //get account details
    fun loadAllMarkets(retryCount: Int, context: Context) {
        var call: Call<BasicMarkets>? =
            ApiClient.build()?.getAllMarkets(Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<BasicMarkets> {
            override fun onFailure(call: Call<BasicMarkets>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    loadAllMarkets(retryCountNew, context)
                }
            }

            override fun onResponse(
                call: Call<BasicMarkets>,
                response: Response<BasicMarkets>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null)
                            Constants.setAllMarkets(it.markets)
                        try {
                            var marketDataValues: HashMap<String, MarketValues>? = hashMapOf()
                            for (data in it.markets) {
                                if (data.underlyingSymbol == "ETH") {
                                    LoadETHTask(data).execute()
                                }
                                marketDataValues!![data.underlyingSymbol] =
                                    MarketValues(
                                        data,
                                        data.balance.toString(),
                                        data.isUnderlyingApproved
                                    )
                                Constants.setMarketValues(marketDataValues!!)
                            }
                            if (Constants.getRefreshWalletListeners().isNotEmpty()) {
                                for (listener in Constants.getRefreshWalletListeners()) {
                                    listener!!.onRefreshWallet()
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        // UserInterface.loadWalletDatas(context, true)
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            loadAllMarkets(retryCountNew, context)
                        }
                    }
                }
            }

        })

    }

    //load wallet task
    private inner class LoadETHTask(investData: InvestData) :
        AsyncTask<String, String, String>() {
        var ethObj: InvestData? = null

        init {
            this.ethObj = investData
        }

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg args: String): String? {
            try {
                val walletBalance = getCoinWallet(
                    Constants.getAccountAddress(),
                    ethObj!!.underlyingDecimals
                )
                var marketDataValues: HashMap<String, MarketValues>? = Constants.getMarketValues()
                marketDataValues!![ethObj!!.underlyingSymbol] =
                    MarketValues(
                        ethObj!!,
                        walletBalance,
                        ethObj!!.isUnderlyingApproved
                    )
                Constants.setMarketValues(marketDataValues!!)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: String?) {
            if (Constants.getRefreshWalletListeners().isNotEmpty()) {
                for (listener in Constants.getRefreshWalletListeners()) {
                    listener!!.onRefreshWallet()
                }
            }
        }
    }


    //get account details
    fun loadAccountSummary(retryCount: Int, address: String, context: Context) {
        val params = JSONObject()
        try {
            params.put("address", address)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<InvestDash>? =
            ApiClient.build()?.getAccountSummary(body, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<InvestDash> {
            override fun onFailure(call: Call<InvestDash>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    loadAccountSummary(retryCountNew, address, context)
                }
            }

            override fun onResponse(
                call: Call<InvestDash>,
                response: Response<InvestDash>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null) {
                            try {
                                Constants.setInvestDashboardValue(it)
                                if (it.markets != null)
                                    Constants.setAllMarkets(it.markets)
                                if (Constants.getRefreshWalletListeners().isNotEmpty()) {
                                    for (listener in Constants.getRefreshWalletListeners()) {
                                        listener!!.onMarketLoaded()
                                    }
                                }
                                UserInterface.loadWalletDatas(context, true)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            loadAccountSummary(retryCountNew, address, context)
                        }
                    }
                }
            }

        })

    }

    //create transaction
    fun createTransaction(retryCount: Int, body: RequestBody, context: Context) {
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.createTransaction(body, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ResponseStatus> {
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    createTransaction(retryCountNew, body, context)
                }
            }

            override fun onResponse(
                call: Call<ResponseStatus>,
                response: Response<ResponseStatus>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it.status) {
                            UserInterface.activitiesRefresh()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            createTransaction(retryCountNew, body, context)
                        }
                    }
                }
            }

        })

    }


    //load wallet task
    private inner class LoadSupplyAndWalletTask(investDatas: List<InvestData>) :
        AsyncTask<String, String, String>() {
        var tokens: List<InvestData>? = emptyList()

        init {
            this.tokens = investDatas
        }

        override fun onPreExecute() {

        }

        override fun doInBackground(vararg args: String): String? {
            loadTokenValues(tokens!!)
            return null
        }

        override fun onPostExecute(result: String?) {
            if (Constants.getRefreshWalletListeners().isNotEmpty()) {
                for (listener in Constants.getRefreshWalletListeners()) {
                    listener!!.onRefreshWallet()
                }
            }
            if (Constants.getRefreshWalletListeners().isNotEmpty()) {
                for (listener in Constants.getRefreshWalletListeners()) {
                    listener!!.onRefreshInvest()
                }
            }
        }
    }

    //load token values
    private fun loadTokenValues(marketDatas: List<InvestData>) {
        try {
            val credentials = Constants.getAccountCredential()
            var marketDataValues: HashMap<String, MarketValues>? = hashMapOf()
            for (marketData in marketDatas) {
                var walletBalance = "0"
                if (marketData.underlyingAddress != Constants.ETH_UnderlyingAddress) {
                    try {
                        walletBalance = getTokenWallet(
                            marketData.underlyingAddress,
                            credentials!!,
                            marketData.underlyingDecimals
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    marketDataValues!![marketData.underlyingSymbol] =
                        MarketValues(marketData, walletBalance, marketData.isUnderlyingApproved)
                } else {
                    try {
                        walletBalance = getCoinWallet(
                            Constants.getAccountAddress(),
                            marketData.underlyingDecimals
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    marketDataValues!![marketData.underlyingSymbol] =
                        MarketValues(marketData, walletBalance, marketData.isUnderlyingApproved)
                }
            }
            Constants.setMarketValues(marketDataValues!!)

            // allowance check
            /* for (marketData in marketDatas) {
                 var isAllowanceValue = false
                 if (marketData.underlyingAddress != Constants.ETH_UnderlyingAddress) {
                     try {
                         isAllowanceValue =
                             isAllowance(
                                 marketData.underlyingAddress,
                                 marketData.id,
                                 credentials!!
                             )
                     } catch (e: Exception) {
                         e.printStackTrace()
                     }
                     if (Constants.getMarketValues()!!.containsKey(marketData.underlyingSymbol)) {
                         Constants.getMarketValues()!![marketData.underlyingSymbol]!!.isAllowance=isAllowanceValue
                     }
                 }
             }*/


        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //getCoinWallet
    private fun getCoinWallet(
        address: String,
        underlyingDecimals: Int
    ): String {
        try {
            val walletBalance = UserInterface.UnitDivide(
                (PesaApplication.getWeb3j()
                    .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .sendAsync()
                    .get()).balance.toBigDecimal(), underlyingDecimals
            )
            return UserInterface.round(walletBalance.toDouble())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0"
    }


    //getTokenWallet
    private fun getTokenWallet(
        address: String,
        credentials: Credentials,
        underlyingDecimals: Int
    ): String {
        try {
            val erc20Token =
                ERC20.load(address, PesaApplication.getWeb3j(), credentials, DefaultGasProvider())
            val walletBalance = UserInterface.UnitDivide(
                erc20Token.balanceOf(Keys.toChecksumAddress(credentials.address)).sendAsync().get().toBigDecimal(),
                underlyingDecimals
            )
            return walletBalance.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0"
    }

    // get allowance balance is enable or disable
    private fun isAllowance(
        address: String,
        marketAddress: String,
        credentials: Credentials
    ): Boolean {
        try {
            val erc20Token =
                ERC20.load(address, PesaApplication.getWeb3j(), credentials, DefaultGasProvider())
            val allowanceBalance =
                erc20Token.allowance(Keys.toChecksumAddress(credentials.address), marketAddress)
                    .sendAsync().get()
            return allowanceBalance.toString() != "0"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

}