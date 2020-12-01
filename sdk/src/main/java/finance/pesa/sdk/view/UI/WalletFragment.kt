package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.Model.WalletData
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.Pesa
import finance.pesa.sdk.utils.*
import finance.pesa.sdk.view.adapter.CoinSpinnerAdapter
import finance.pesa.sdk.view.adapter.WalletAdapter
import jnr.ffi.annotations.In
import org.web3j.contracts.eip20.generated.ERC20
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.http.HttpService
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger

class WalletFragment : Fragment() {
    private var depositView: LinearLayout? = null
    private var epnView: LinearLayout? = null
    private var sendView: LinearLayout? = null
    private var receiveView: LinearLayout? = null
    private var exchangeView: LinearLayout? = null
    private var walletDetails: LinearLayout? = null
    private var mintView: LinearLayout? = null
    private var totalWallet: TextView? = null
    private var epnNumber: TextView? = null
    var walletRecyclerView: RecyclerView? = null
    var mShimmerViewContainer: ShimmerFrameLayout? = null
    var shimmerTotalWallet: ShimmerFrameLayout? = null
    var isViewGenerated: Boolean? = false
    var web3j: Web3j? = null
    var walletAdapter: WalletAdapter? = null
    var mintWalletUpdate: MintWalletUpdate? = null

    companion object {
        var walletFragment = WalletFragment()
        fun newInstance(): WalletFragment {
            return walletFragment!!
        }
    }

    fun onTabSelected() {
        if (!isViewGenerated!!) {
            UserInterface.changeStatusBar(activity!!, R.color.app_green)
            isViewGenerated = true
            getMarketBalance()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        isViewGenerated = false
    }

    override fun onStop() {
        super.onStop()
        isViewGenerated = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (DashboardFragment.dashboardFragment!!.viewPager!!.currentItem == 0) {
                onTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.wallet_main, container, false)
        depositView = view.findViewById(R.id.deposit_view)
        sendView = view.findViewById(R.id.send_view)
        epnView = view.findViewById(R.id.epn_view)
        receiveView = view.findViewById(R.id.receive_view)
        exchangeView = view.findViewById(R.id.exchange_view)
        mintView = view.findViewById(R.id.mint_view)
        epnNumber = view.findViewById(R.id.epn_number)
        walletDetails = view.findViewById(R.id.wallet_details)
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container)
        shimmerTotalWallet = view.findViewById(R.id.shimmer_total_wallet)
        totalWallet = view.findViewById(R.id.total_wallet)
        walletRecyclerView = view.findViewById(R.id.wallet_recycler)
        web3j = Web3j.build(HttpService(Constants.kovan_url))
        walletFragment = this
        if (Constants.getisEPNEnabled()) {
            epnNumber?.visibility = View.VISIBLE
            epnNumber?.text = "EPN: " + Constants.getEPNNumber()
        }
        /*sendView!!.setOnClickListener {
            val bottomSheetDialog = SendBottomsheet.newInstance(walletDetails!!.height)
            bottomSheetDialog.isCancelable = false
            bottomSheetDialog.show(childFragmentManager, null)
        }
        exchangeView!!.setOnClickListener {
            val bottomSheetDialog = ExchangeBottomSheet.newInstance(walletDetails!!.height)
            bottomSheetDialog.isCancelable = false
            bottomSheetDialog.show(childFragmentManager, null)
        }*/
        mintView!!.setOnClickListener {
            showMintDialog()
        }

        epnView!!.setOnClickListener {

        }

        return view
    }

    //show mint dialog
    private fun showMintDialog() {
        val mintDialog = Dialog(context!!, R.style.CustomDialog)
        mintDialog?.setContentView(R.layout.mint_popup)
        val btnMint = mintDialog?.findViewById<Button>(R.id.btn_mint)
        val ivClose = mintDialog?.findViewById<ImageView>(R.id.iv_close)
        val fromType = mintDialog.findViewById<Spinner>(R.id.from_type)
        val approvalSwitch = mintDialog.findViewById<Switch>(R.id.approval_switch)
        val btnMax = mintDialog.findViewById<TextView>(R.id.btn_max)
        val sendBalance = mintDialog.findViewById<TextView>(R.id.send_balance)
        val pUSDBalance = mintDialog.findViewById<TextView>(R.id.pusd_balance)
        val txtToken = mintDialog.findViewById<EditText>(R.id.txt_token)
        val d = ColorDrawable(Color.TRANSPARENT)
        mintDialog?.window!!.setBackgroundDrawable(d)
        mintDialog?.setCancelable(false)
        val allMarket = Constants.getAllMarkets()
        var mintFromMarkets: List<InvestData>? = emptyList()
        var mintToMarkets: InvestData? = null
        var isSwitchTouch = false
        var mintFromUnderlyingAddress = ""
        var mintFromUnderlyingSymbol = ""
        var mintFromMarketDecimalFactor = 0
        var sendTotalBalance = 0.0
        var marketAdapter = CoinSpinnerAdapter(
            activity!!,
            emptyList()
        )
        if (allMarket != null) {
            for (market in allMarket) {
                if (mintFromMarkets != null) {
                    if (Constants.getMintFrom().contains(market.underlyingSymbol)) {
                        mintFromMarkets += market
                    }
                    marketAdapter = CoinSpinnerAdapter(
                        activity!!,
                        mintFromMarkets
                    )
                    fromType!!.adapter = marketAdapter
                }
                if (market.underlyingSymbol == Constants.getMintTo()[0]) {
                    mintToMarkets = market
                }
            }
        }

        try {
            if (Constants.getMarketValues()!!.containsKey(mintToMarkets!!.underlyingSymbol)) {
                val pUSDBalanceValue =
                    Constants.getMarketValues()!![mintToMarkets!!.underlyingSymbol]!!.walletBalance
                pUSDBalance.text =
                    UserInterface.round(pUSDBalanceValue.toDouble())
            } else {
                pUSDBalance.text = "0.0000"
            }
        } catch (e: Exception) {
            pUSDBalance.text = "0.0000"
            e.printStackTrace()
        }

        mintWalletUpdate = object : MintWalletUpdate {
            override fun onWalletUpdated() {
                try {
                    if (Constants.getMarketValues()!!.containsKey(mintFromUnderlyingSymbol)) {
                        val walletBalance =
                            Constants.getMarketValues()!![mintFromUnderlyingSymbol]!!.walletBalance
                        sendTotalBalance = walletBalance.toDouble()
                        sendBalance.text =
                            UserInterface.round(walletBalance.toDouble())
                    } else {
                        sendBalance.text = "0.0000"
                    }
                } catch (e: Exception) {
                    sendBalance.text = "0.0000"
                    e.printStackTrace()
                }
                try {
                    if (Constants.getMarketValues()!!.containsKey(mintToMarkets!!.underlyingSymbol)) {
                        val pUSDBalanceValue =
                            Constants.getMarketValues()!![mintToMarkets!!.underlyingSymbol]!!.walletBalance
                        pUSDBalance.text =
                            UserInterface.round(pUSDBalanceValue.toDouble())
                    } else {
                        pUSDBalance.text = "0.0000"
                    }
                } catch (e: Exception) {
                    pUSDBalance.text = "0.0000"
                    e.printStackTrace()
                }
                validateMint(txtToken, approvalSwitch, btnMint, sendTotalBalance)
            }

        }

        fromType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                val selectedItem = marketAdapter.getMarket(position)
                sendTotalBalance = 0.0
                try {
                    mintFromUnderlyingAddress = selectedItem!!.underlyingAddress
                    mintFromUnderlyingSymbol = selectedItem!!.underlyingSymbol
                    mintFromMarketDecimalFactor = selectedItem!!.underlyingDecimals
                    if (mintToMarkets != null) {
                        approvalSwitch.isEnabled = false
                        approvalSwitch.isChecked = isAllowance(
                            mintToMarkets.underlyingAddress,
                            selectedItem!!.underlyingAddress,
                            Constants.getAccountCredential()!!
                        )
                        approvalSwitch.isEnabled = true
                    }
                    try {
                        if (Constants.getMarketValues()!!.containsKey(selectedItem.underlyingSymbol)) {
                            val walletBalance =
                                Constants.getMarketValues()!![selectedItem.underlyingSymbol]!!.walletBalance
                            sendTotalBalance = walletBalance.toDouble()
                            sendBalance.text =
                                UserInterface.round(walletBalance.toDouble())
                        } else {
                            sendBalance.text = "0.0000"
                        }
                    } catch (e: Exception) {
                        sendBalance.text = "0.0000"
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                validateMint(txtToken, approvalSwitch, btnMint, sendTotalBalance)
            } // to close the onItemSelected

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        approvalSwitch!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && isSwitchTouch) {
                isSwitchTouch = false
                transactionApproveDialog(
                    isChecked,
                    approvalSwitch,
                    mintToMarkets!!.underlyingAddress,
                    mintFromUnderlyingAddress
                )
            } else if (isSwitchTouch) {
                isSwitchTouch = false
                transactionApproveDialog(
                    isChecked,
                    approvalSwitch,
                    mintToMarkets!!.underlyingAddress,
                    mintFromUnderlyingAddress
                )
            }
        }
        approvalSwitch.setOnTouchListener(View.OnTouchListener { v, event ->
            isSwitchTouch = true
            false
        })

        txtToken.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                validateMint(txtToken, approvalSwitch, btnMint, sendTotalBalance)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        btnMax.setOnClickListener {
            txtToken.setText(sendBalance.text.toString().trim())
            txtToken.setSelection(txtToken.text.length)
        }

        ivClose.setOnClickListener {
            mintDialog.dismiss()
        }

        mintDialog.setOnDismissListener { mintWalletUpdate = null }

        btnMint.setOnClickListener {
            try {
                transactionMintDialog(
                    mintToMarkets!!.underlyingAddress,
                    mintFromUnderlyingAddress,
                    txtToken.text.toString().trim(),
                    mintFromMarketDecimalFactor
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        mintDialog?.show()
    }

    // Transaction confirmation for Mint
    private fun transactionMintDialog(
        toAddress: String,
        fromAddress: String,
        token: String,
        factor: Int
    ) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog!!.setContentView(R.layout.transaction_confirmation)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog!!.window!!.setBackgroundDrawable(d)
            cfmDialog!!.setCancelable(false)
            val totalPriceTxt = cfmDialog!!.findViewById<TextView>(R.id.total_price)
            val btnConfirm = cfmDialog!!.findViewById<Button>(R.id.btn_confirm)
            val btnReject = cfmDialog!!.findViewById<Button>(R.id.btn_reject)
            val btnClose = cfmDialog!!.findViewById<ImageView>(R.id.iv_close)
            try {
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val totalFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toPlainString()
                totalPriceTxt.text = totalFee
            } catch (e: Exception) {
                e.printStackTrace()
            }
            btnConfirm?.setOnClickListener {
                cfmDialog?.dismiss()
                MintTask(toAddress, fromAddress, token, factor).execute()
            }
            btnReject?.setOnClickListener {
                cfmDialog?.dismiss()
            }
            btnClose?.setOnClickListener {
                cfmDialog?.dismiss()
            }
            cfmDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // mint task
    private inner class MintTask(
        toAddress: String,
        fromAddress: String,
        token: String,
        factor: Int
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var toAddress: String? = null
        private var fromAddress: String? = null
        private var token: String? = null
        private var factor: Int = 0

        init {
            this.toAddress = toAddress
            this.fromAddress = fromAddress
            this.token = token
            this.factor = factor
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return mintToken(toAddress!!, fromAddress!!, token!!, factor!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.showToast(context, "Failed to mint...")
            }
        }
    }


    //mint token
    private fun mintToken(
        toAddress: String,
        fromAddress: String,
        token: String,
        factor: Int
    ): Boolean {
        try {
            //Using Market key
            val credentials = Constants.getAccountCredential()
            val pPesa =
                Pesa.load(
                    toAddress,
                    PesaApplication.getWeb3j(),
                    credentials,
                    CustomGasProvider(),
                    context
                )
            val receipt =
                pPesa.buy(
                    fromAddress,
                    UserInterface.UnitMultiply(
                        BigDecimal(token),
                        factor
                    ).toBigInteger()
                )
                    .sendAsync().get()
            Log.d("mint_Receipt_Hash-->", receipt.transactionHash)
            val transactionReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(receipt.transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    // validate mint datas
    private fun validateMint(
        txtMint: EditText,
        switch: Switch,
        btnMint: Button,
        sendTotalBalance: Double
    ) {
        try {
            btnMint.isEnabled = false
            btnMint.isClickable = false
            val txtEnterValue = txtMint.text.toString().trim().toDouble()
            if (txtEnterValue > 0 && txtEnterValue <= sendTotalBalance && switch.isEnabled && switch.isChecked) {
                btnMint.isEnabled = true
                btnMint.isClickable = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // get allowance balance is enable or disable
    private fun isAllowance(
        toAddress: String,
        marketAddress: String,
        credentials: Credentials
    ): Boolean {
        try {
            val erc20Token =
                ERC20.load(
                    marketAddress,
                    PesaApplication.getWeb3j(),
                    credentials,
                    CustomGasProvider()
                )
            val allowanceBalance =
                erc20Token.allowance(Keys.toChecksumAddress(credentials.address), toAddress)
                    .sendAsync().get()
            return allowanceBalance.toString() != "0"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    //enable Allowance
    private fun approveToken(
        toAddress: String,
        marketAddress: String,
        isEnable: Boolean
    ): Boolean {
        try {
            var allowanceBalanceLimit = "0"
            if (isEnable)
                allowanceBalanceLimit = Constants.allowanceBalanceLimit
            val credentials = Constants.getAccountCredential()
            val ercToken =
                ERC20.load(
                    marketAddress,
                    PesaApplication.getWeb3j(),
                    credentials,
                    CustomGasProvider()
                )
            val transactionReceipt = ercToken.approve(
                toAddress, BigInteger(allowanceBalanceLimit)
            )
            val receipt = transactionReceipt.sendAsync().get()
            Log.d("Approve_Allowance_Receipt_Hash-->", receipt.transactionHash)
            val marketReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(receipt.transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (marketReceipt.isStatusOK && marketReceipt.logs.isNotEmpty()) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    // enable allowance balance task
    private inner class EnableTask(
        address: String,
        marketAddress: String,
        switch: Switch,
        isEnable: Boolean
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var marketAddress: String? = null
        private var address: String? = null
        private var switch: Switch? = null
        private var isEnable: Boolean = false

        init {
            this.marketAddress = marketAddress
            this.address = address
            this.switch = switch
            this.isEnable = isEnable
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return approveToken(address!!, marketAddress!!, isEnable!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {

                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                switch?.isChecked = !isEnable
                mintWalletUpdate?.onWalletUpdated()
                UserInterface.showToast(context, "Failed to enable...")
            }
        }
    }


    // Transaction confirmation for approve
    private fun transactionApproveDialog(
        isEnable: Boolean,
        switch: Switch,
        address: String,
        marketAddress: String
    ) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog!!.setContentView(R.layout.transaction_confirmation)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog!!.window!!.setBackgroundDrawable(d)
            cfmDialog!!.setCancelable(false)
            val totalPriceTxt = cfmDialog!!.findViewById<TextView>(R.id.total_price)
            val btnConfirm = cfmDialog!!.findViewById<Button>(R.id.btn_confirm)
            val btnReject = cfmDialog!!.findViewById<Button>(R.id.btn_reject)
            val btnClose = cfmDialog!!.findViewById<ImageView>(R.id.iv_close)
            try {
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val totalFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toPlainString()
                totalPriceTxt.text = totalFee
            } catch (e: Exception) {
                e.printStackTrace()
            }
            btnConfirm?.setOnClickListener {
                cfmDialog?.dismiss()
                EnableTask(
                    address,
                    marketAddress,
                    switch,
                    isEnable
                ).execute()
            }
            btnReject?.setOnClickListener {
                cfmDialog?.dismiss()
                switch?.isChecked = !isEnable
            }
            btnClose?.setOnClickListener {
                cfmDialog?.dismiss()
                switch?.isChecked = !isEnable
            }
            cfmDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //get wallet balance
    private fun getMarketBalance() {
        try {
            mShimmerViewContainer!!.startShimmerAnimation()
            shimmerTotalWallet!!.startShimmerAnimation()
            walletRecyclerView!!.visibility = View.GONE
            totalWallet!!.visibility = View.GONE
            mShimmerViewContainer!!.visibility = View.VISIBLE
            shimmerTotalWallet!!.visibility = View.VISIBLE
            var walletDatas: ArrayList<WalletData>? = ArrayList()
            val marketDatas = Constants.getAllMarkets()!!
            var totalBalance = 0.0
            for (marketData in marketDatas) {
                if (marketData.balance > 0)
                    walletDatas!!.add(WalletData(marketData, null))
            }
            for ((key, value) in Constants.getMarketValues()!!) {
                totalBalance += Constants.getMarketValues()!![key]!!.walletBalance.toDouble() * Constants.getMarketValues()!!.get(
                    key
                )!!.markets.underlyingPriceUSD
            }
            if (walletDatas!!.isNotEmpty()) {
                activity!!.runOnUiThread {
                    mShimmerViewContainer!!.stopShimmerAnimation()
                    mShimmerViewContainer!!.visibility = View.GONE
                    walletRecyclerView!!.visibility = View.VISIBLE
                    shimmerTotalWallet!!.visibility = View.GONE
                    shimmerTotalWallet!!.stopShimmerAnimation()
                    totalWallet!!.visibility = View.VISIBLE
                    totalWallet!!.text = "$" + UserInterface.roundTwo(totalBalance)
                    walletAdapter = WalletAdapter(
                        activity!!,
                        walletDatas!!
                    )
                    walletRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    walletRecyclerView!!.adapter = walletAdapter
                    Constants.setRefreshWalletListener(refreshWalletListener)
                }
            } else {
                Constants.setRefreshWalletListener(refreshWalletListener)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //refresh wallet balance
    private val refreshWalletListener = object : RefreshWalletListener {
        override fun onMarketLoaded() {
            try {
                mShimmerViewContainer!!.startShimmerAnimation()
                shimmerTotalWallet!!.startShimmerAnimation()
                walletRecyclerView!!.visibility = View.GONE
                totalWallet!!.visibility = View.GONE
                mShimmerViewContainer!!.visibility = View.VISIBLE
                shimmerTotalWallet!!.visibility = View.VISIBLE
                var walletDatas: ArrayList<WalletData>? = ArrayList()
                val marketDatas = Constants.getAllMarkets()!!
                var totalBalance = 0.0
                for (marketData in marketDatas) {
                    if (marketData.balance > 0)
                        walletDatas!!.add(WalletData(marketData, null))
                }
                for ((key, value) in Constants.getMarketValues()!!) {
                    totalBalance += Constants.getMarketValues()!![key]!!.walletBalance.toDouble() * Constants.getMarketValues()!!.get(
                        key
                    )!!.markets.underlyingPriceUSD
                }
                activity!!.runOnUiThread {
                    mShimmerViewContainer!!.stopShimmerAnimation()
                    mShimmerViewContainer!!.visibility = View.GONE
                    walletRecyclerView!!.visibility = View.VISIBLE
                    shimmerTotalWallet!!.visibility = View.GONE
                    shimmerTotalWallet!!.stopShimmerAnimation()
                    totalWallet!!.visibility = View.VISIBLE
                    totalWallet!!.text = "$" + UserInterface.roundTwo(totalBalance)
                    walletAdapter = WalletAdapter(
                        activity!!,
                        walletDatas!!
                    )
                    walletRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    walletRecyclerView!!.adapter = walletAdapter
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun onRefreshInvest() {

        }

        override fun onRefreshWallet() {
            PesaApplication.currentActivity!!.runOnUiThread {
                try {
                    var walletDatas: ArrayList<WalletData>? = ArrayList()
                    val marketDatas = Constants.getAllMarkets()!!
                    var totalBalance = 0.0
                    for (marketData in marketDatas) {
                        if (marketData.balance > 0)
                            walletDatas!!.add(WalletData(marketData, null))
                    }
                    for ((key, value) in Constants.getMarketValues()!!) {
                        totalBalance += Constants.getMarketValues()!![key]!!.walletBalance.toDouble() * Constants.getMarketValues()!!.get(
                            key
                        )!!.markets.underlyingPriceUSD
                    }
                    activity!!.runOnUiThread {
                        mShimmerViewContainer!!.visibility = View.GONE
                        walletRecyclerView!!.visibility = View.VISIBLE
                        totalWallet!!.visibility = View.VISIBLE
                        totalWallet!!.text = "$" + UserInterface.roundTwo(totalBalance)
                        walletAdapter = WalletAdapter(
                            activity!!,
                            walletDatas!!
                        )
                        walletRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                        walletRecyclerView!!.adapter = walletAdapter
                    }
                    mintWalletUpdate?.onWalletUpdated()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}