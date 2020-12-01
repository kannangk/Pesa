package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.InvestDash
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.PController
import finance.pesa.sdk.generator.PErc20Delegator
import finance.pesa.sdk.generator.PEth
import finance.pesa.sdk.utils.*
import finance.pesa.sdk.utils.Constants.ETH_UnderlyingAddress
import finance.pesa.sdk.utils.Constants.PESA_TOKEN
import finance.pesa.sdk.utils.Constants.allowanceBalanceLimit
import finance.pesa.sdk.utils.Constants.pControllerAddress
import finance.pesa.sdk.view.Interface.EnableSupplyListener
import finance.pesa.sdk.view.adapter.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.contracts.eip20.generated.ERC20
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.roundToInt

class InvestFragment : Fragment() {

    var btnClaim: TextView? = null
    var supplyLay: LinearLayout? = null
    var suppliedLay: LinearLayout? = null
    var borrowedLay: LinearLayout? = null
    var investScroolView: NestedScrollView? = null
    var pendingTransactionLay: LinearLayout? = null
    var pendingTransactionMsg: TextView? = null
    var progressWithArrow: CircleProgressBar? = null
    var borrowLay: LinearLayout? = null
    var borrowTab: LinearLayout? = null
    var supplyTab: LinearLayout? = null
    var borrowPage: LinearLayout? = null
    var supplyPage: LinearLayout? = null
    var supplyBalance: TickerView? = null
    var supplyTxt: TextView? = null
    var borrowTxt: TextView? = null
    var borrowBalance: TickerView? = null
    var borrowLimit: TextView? = null
    var usedBorrowLimit: TextView? = null
    var netAPY: TextView? = null
    var apyProgresbar: ProgressBar? = null
    var borrowProgressBar: ProgressBar? = null
    var supplyDiv: View? = null
    var borrowDiv: View? = null
    var isViewGenerated: Boolean? = false
    var supplyRecyclerView: RecyclerView? = null
    var suppliedRecyclerView: RecyclerView? = null
    var borrowedRecyclerView: RecyclerView? = null
    var borrowRecyclerView: RecyclerView? = null
    private var supplyAndWithdrawDialog: Dialog? = null
    private var borrowAndRepayDialog: Dialog? = null
    private var supplyAdapter: SupplyAdapter? = null
    private var suppliedAdapter: SuppliedAdapter? = null
    private var borrowAdapter: BorrowAdapter? = null
    private var borrowedAdapter: BorrowedAdapter? = null
    private var supplyDatas: ArrayList<MarketData>? = ArrayList()
    private var borrowDatas: ArrayList<MarketData>? = ArrayList()
    private var remainDatas: ArrayList<MarketData>? = ArrayList()
    private var totalSuppliedBalance = 0.0
    private var totalBorrowedBalance = 0.0
    private var maxBorrowLimit = 0.0
    private var totalBorrowLimit = 0.0
    private var collateralSuppliedBalance = 0.0
    private var totalBorrowLimitValue = 0.0


    companion object {
        var investFragment: InvestFragment? = null
        fun newInstance(): InvestFragment {
            return InvestFragment()
        }
    }

    fun onTabSelected() {
        if (!isViewGenerated!!) {
            isViewGenerated = true
            UserInterface.changeStatusBar(activity!!, R.color.black)
            loadTokenValues(Constants.getInvestDashboardValue()!!)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (DashboardFragment.dashboardFragment!!.viewPager!!.currentItem == 1) {
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
        val view = inflater.inflate(R.layout.invest_main, container, false)
        investFragment = this
        btnClaim = view.findViewById(R.id.btn_claim)
        supplyLay = view.findViewById(R.id.supply_lay)
        suppliedLay = view.findViewById(R.id.supplied_lay)
        borrowedLay = view.findViewById(R.id.borrowed_lay)
        pendingTransactionLay = view.findViewById(R.id.pending_transaction_lay)
        progressWithArrow = view.findViewById(R.id.pending_trans_pb)
        investScroolView = view.findViewById(R.id.invest_scroll)
        borrowLay = view.findViewById(R.id.borrow_lay)
        borrowTab = view.findViewById(R.id.borrow_tab)
        supplyTab = view.findViewById(R.id.supply_tab)
        borrowLimit = view.findViewById(R.id.borrow_limit)
        netAPY = view.findViewById(R.id.net_apy)
        apyProgresbar = view.findViewById(R.id.apy_pb)
        borrowProgressBar = view.findViewById(R.id.borrow_progress_bar)
        usedBorrowLimit = view.findViewById(R.id.used_borrow_limit)
        supplyBalance = view.findViewById(R.id.supply_balance)
        borrowPage = view.findViewById(R.id.borrow_page)
        supplyPage = view.findViewById(R.id.supply_page)
        supplyTxt = view.findViewById(R.id.supply_txt)
        borrowTxt = view.findViewById(R.id.borrow_txt)
        supplyDiv = view.findViewById(R.id.supply_div)
        borrowDiv = view.findViewById(R.id.borrow_div)
        borrowBalance = view.findViewById(R.id.borrow_balance)
        supplyRecyclerView = view.findViewById(R.id.supply_recycler)
        suppliedRecyclerView = view.findViewById(R.id.supplied_recycler)
        borrowedRecyclerView = view.findViewById(R.id.borrowed_recycler)
        pendingTransactionMsg = view.findViewById(R.id.pending_transaction_msg)
        borrowRecyclerView = view.findViewById(R.id.borrow_recycler)
        borrowBalance!!.setCharacterLists(TickerUtils.provideNumberList())
        supplyBalance!!.setCharacterLists(TickerUtils.provideNumberList())
        supplyTab!!.setOnClickListener {
            supplyTxt!!.setTextColor(context!!.getColor(R.color.tab_select_text))
            borrowTxt!!.setTextColor(context!!.getColor(R.color.tab_unselect_text))
            supplyDiv!!.visibility = View.VISIBLE
            borrowDiv!!.visibility = View.GONE
            supplyPage!!.visibility = View.VISIBLE
            borrowPage!!.visibility = View.GONE
            borrowedLay!!.visibility = View.GONE
            suppliedLay!!.visibility = View.VISIBLE
        }
        borrowTab!!.setOnClickListener {
            supplyTxt!!.setTextColor(context!!.getColor(R.color.tab_unselect_text))
            borrowTxt!!.setTextColor(context!!.getColor(R.color.tab_select_text))
            supplyDiv!!.visibility = View.GONE
            borrowDiv!!.visibility = View.VISIBLE
            supplyPage!!.visibility = View.GONE
            borrowPage!!.visibility = View.VISIBLE
            borrowedLay!!.visibility = View.VISIBLE
            suppliedLay!!.visibility = View.GONE
        }
        refreshAccountSummary(1, Constants.getAccountCredential()!!.address)
        progressWithArrow?.setColorSchemeResources(android.R.color.white)
        return view
    }


    //get account details
    private fun refreshAccountSummary(retryCount: Int, address: String) {
        val params = JSONObject()
        try {
            params.put("address", address)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<InvestDash>? =
            ApiClient.build()?.getAccountSummary(requestBody,Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<InvestDash> {
            override fun onFailure(call: Call<InvestDash>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    refreshAccountSummary(retryCountNew, address)
                }
            }

            override fun onResponse(
                call: Call<InvestDash>,
                response: Response<InvestDash>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null) {
                            Constants.setInvestDashboardValue(it)
                            if (it.markets != null)
                                Constants.setAllMarkets(it.markets)
                            onUpdateSupplyBorrowBalance(it!!)
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            refreshAccountSummary(retryCountNew, address)
                        }
                    }
                }
            }

        })

    }

    private fun onUpdateSupplyBorrowBalance(investDash: InvestDash) {
        try {
            if (activity == null)
                return
            totalBorrowedBalance = 0.0
            totalBorrowLimitValue = 0.0
            collateralSuppliedBalance = 0.0

            val allInvestDatas: List<InvestData> =
                investDash.markets.sortedBy { investData -> investData.underlyingSymbol }

            for (marketData in allInvestDatas) {
                when {
                    marketData.totalUnderlyingSupplied != 0.0 -> {
                        try {
                            val suppliedBal = marketData.totalUnderlyingSuppliedinUSD
                            if (marketData.enteredMarket) {
                                collateralSuppliedBalance += suppliedBal
                                totalBorrowLimitValue += suppliedBal * marketData.collateralFactor
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    marketData.storedBorrowBalance != 0.0 -> {
                        totalBorrowedBalance += marketData.totalstoredBorrowBalanceinUSD
                    }
                }
            }
            activity!!.runOnUiThread {
                try {
                    totalSuppliedBalance = collateralSuppliedBalance
                    var usedBorrowLimitPer = (totalBorrowedBalance / totalBorrowLimitValue) * 100
                    /* totalSuppliedBalance = investDash.overAllSupplied
                     totalBorrowedBalance = investDash.overAllBorrowed
                     totalBorrowLimitValue = investDash.borrowedLimit
                     var usedBorrowLimitPer = investDash.borrowedInPercentage*/
                    borrowLimit!!.text = "$" + UserInterface.round(totalBorrowLimitValue)
                    usedBorrowLimit!!.text = (usedBorrowLimitPer.toInt()).toString() + "%"
                    borrowProgressBar!!.progress = usedBorrowLimitPer.toInt()
                    totalBorrowLimit = (totalBorrowLimitValue - totalBorrowedBalance)
                    maxBorrowLimit = (totalBorrowLimitValue - totalBorrowedBalance) * 0.80
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                supplyBalance!!.text = "$" + UserInterface.roundEight(totalSuppliedBalance)
                borrowBalance!!.text = "$" + UserInterface.roundEight(totalBorrowedBalance)
                netAPY!!.text = UserInterface.round(investDash.netAPY) + "%"
                apyProgresbar!!.progress = investDash.borrowBarValue.toInt()

            }
            Handler().postDelayed({
                refreshAccountSummary(1, Constants.getAccountCredential()!!.address)
            }, 15000)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //get account details
    private fun loadAccountSummary(retryCount: Int, address: String) {
        /*mShimmerViewContainer!!.startShimmerAnimation()
        borrowShimmerViewContainer!!.startShimmerAnimation()
        supplyLay!!.visibility = View.GONE
        borrowLay!!.visibility = View.GONE
        mShimmerViewContainer!!.visibility = View.VISIBLE
        borrowShimmerViewContainer!!.visibility = View.VISIBLE*/
        val params = JSONObject()
        try {
            params.put("address", address)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<InvestDash>? =
            ApiClient.build()?.getAccountSummary(requestBody,Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<InvestDash> {
            override fun onFailure(call: Call<InvestDash>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    loadAccountSummary(retryCountNew, address)
                }
            }

            override fun onResponse(
                call: Call<InvestDash>,
                response: Response<InvestDash>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null) {
                            Constants.setInvestDashboardValue(it)
                            if (it.markets != null)
                                Constants.setAllMarkets(it.markets)
                            loadTokenValues(it!!)
                        } else {
                            /* supplyBalance!!.text = "$0"
                             borrowBalance!!.text = "$0"
                             loadTokenValues(emptyList())*/
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            loadAccountSummary(retryCountNew, address)
                        }
                    }
                }
            }

        })

    }

    //check collateral
    private fun checkMarkets(address: String): Boolean {
        try {
            val credentials = Constants.getAccountCredential()
            val pController = PController.load(
                pControllerAddress,
                PesaApplication.getWeb3j(),
                credentials,
                CustomGasProvider(),
                context
            )
            return pController.checkMembership(credentials!!.address, address).sendAsync().get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    /* private fun checkAllowance(address: String, marketAddress: String, isSupply: Boolean) {
         val credentials = Constants.getAccountCredential()
         val ercToken = ERC20.load(address, PesaApplication.getWeb3j(), credentials, CustomGasProvider())
         val allowanceBalance =
             ercToken.allowance(Keys.toChecksumAddress(credentials.address), marketAddress)
                 .sendAsync().get()
         Log.d("Allowance_Balance-->", allowanceBalance.toString())
         if (allowanceBalance.toString() == "0") {
             if (isSupply) {
                 val pErc20Delegator =
                     PErc20Delegator.load(marketAddress, PesaApplication.getWeb3j(), credentials, CustomGasProvider())
                 var suppliedBalance = BigInteger("0")
                 try {
                     val suppliedBalanceValue =
                         pErc20Delegator.balanceOf(Keys.toChecksumAddress(credentials.address)).sendAsync().get()
                     if (suppliedBalanceValue.toString() != "0") {
                         suppliedBalance =
                             UserInterface.UnitDivide(suppliedBalanceValue, 8).div(BigInteger("50"))
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                 }
                 Log.d("Supplied_Balance-->", suppliedBalance.toString())
                 showSupplyAndWithdrawDialog(
                     address,
                     marketAddress,
                     false,
                     suppliedBalance.toString()
                 )
             } else {
                 val pController = PController.load(
                     pControllerAddress,
                     PesaApplication.getWeb3j(),
                     credentials,
                     CustomGasProvider(),
                     context
                 )
                 val borrowLimit =
                     pController.getAccountLiquidity(Keys.toChecksumAddress(credentials.address)).sendAsync().get()
                 Log.d(
                     "borrowLimit-->", Convert.fromWei(
                         borrowLimit.component2().toString(),
                         Convert.Unit.ETHER
                     ).toString()
                 )
                 val borrowTotalValue =
                     UserInterface.UnitDivide(borrowLimit.component2(), 18).toString()
                 showBorrowAndRepayDialog(address, marketAddress, false, borrowTotalValue)
             }
         } else {
             if (isSupply) {
                 val pErc20Delegator =
                     PErc20Delegator.load(marketAddress, PesaApplication.getWeb3j(), credentials, CustomGasProvider())
                 var suppliedBalance = BigInteger("0")
                 try {
                     val suppliedBalanceValue =
                         pErc20Delegator.balanceOf(Keys.toChecksumAddress(credentials.address)).sendAsync().get()
                     if (suppliedBalanceValue.toString() != "0") {
                         suppliedBalance =
                             UserInterface.UnitDivide(suppliedBalanceValue, 8).div(BigInteger("50"))
                     }
                 } catch (e: Exception) {
                     e.printStackTrace()
                 }
                 Log.d("Supplied_Balance-->", suppliedBalance.toString())
                 showSupplyAndWithdrawDialog(
                     address,
                     marketAddress,
                     true,
                     suppliedBalance.toString()
                 )
             } else {
                 val pController = PController.load(
                     pControllerAddress,
                     PesaApplication.getWeb3j(),
                     credentials,
                     CustomGasProvider(),
                     context
                 )
                 val borrowLimit =
                     pController.getAccountLiquidity(Keys.toChecksumAddress(credentials.address)).sendAsync().get()
                 Log.d(
                     "borrowLimit-->", Convert.fromWei(
                         borrowLimit.component2().toString(),
                         Convert.Unit.ETHER
                     ).toString()
                 )
                 val borrowTotalValue =
                     UserInterface.UnitDivide(borrowLimit.component2(), 18).toString()
                 showBorrowAndRepayDialog(address, marketAddress, true, borrowTotalValue)
             }
         }
     }*/

    //show enable collateral
    private fun showEnableCollateralDialog(
        address: String, pControllerAddress: String, switch: Switch, suppliedData: MarketData
    ) {
        val enableCollateralDialog = Dialog(context!!, R.style.CustomDialog)
        enableCollateralDialog!!.setContentView(R.layout.collateral_enable)
        val btnEnable = enableCollateralDialog!!.findViewById<TextView>(R.id.btn_enable)
        val ivClose = enableCollateralDialog!!.findViewById<ImageView>(R.id.iv_close)
        val availability = enableCollateralDialog.findViewById<TextView>(R.id.availability)
        val tokenAvailable = enableCollateralDialog.findViewById<TextView>(R.id.token_available)
        val msg = enableCollateralDialog.findViewById<TextView>(R.id.msg)
        val borrowLimitPbCollateral =
            enableCollateralDialog.findViewById<ProgressBar>(R.id.borrow_limit_pb)
        val maxBorrowLimitCollateral =
            enableCollateralDialog.findViewById<TextView>(R.id.max_borrow_limit)
        val changeMaxBorrowLimitCollateral =
            enableCollateralDialog.findViewById<TextView>(R.id.change_max_borrow_limit)
        val maxBorrowLimitPerCollateral =
            enableCollateralDialog.findViewById<TextView>(R.id.max_borrow_limit_per)
        val changeMaxBorrowLimitPerCollateral =
            enableCollateralDialog.findViewById<TextView>(R.id.change_max_borrow_limit_per)
        val borrowLimitTableCollateral =
            enableCollateralDialog.findViewById<TableRow>(R.id.borrow_limit)
        val borrowLimitPerTableCollateral =
            enableCollateralDialog.findViewById<TableRow>(R.id.borrow_limit_per)
        val d = ColorDrawable(Color.TRANSPARENT)
        enableCollateralDialog!!.window!!.setBackgroundDrawable(d)
        enableCollateralDialog!!.setCancelable(false)
        setTextViewHTML(
            msg,
            "Each asset used as collateral increases your borrowing limit. Be careful, this can subject the asset to being seized in liquidation. <a href=learn_more>Learn more</a>."
        )
        try {
            val supplyBal = suppliedData.markets.totalUnderlyingSuppliedinUSD
            maxBorrowLimitCollateral.text = "$" + UserInterface.round(totalBorrowLimitValue, 2)
            var usedBorrowLimitValue: Double? = 0.0
            if (totalBorrowLimitValue != 0.0)
                usedBorrowLimitValue = totalBorrowedBalance / totalBorrowLimitValue * 100
            maxBorrowLimitPerCollateral.text =
                UserInterface.round(usedBorrowLimitValue!!, 2).toString() + "%"
            val changedVal =
                (totalBorrowLimitValue + (supplyBal * suppliedData.markets.collateralFactor))
            val maxBorrow = (totalBorrowedBalance / changedVal) * 100
            changeMaxBorrowLimitCollateral.text =
                "$" + UserInterface.round(
                    changedVal,
                    2
                )
            changeMaxBorrowLimitPerCollateral.text =
                UserInterface.round(maxBorrow, 2).toString() + "%"
            borrowLimitPbCollateral.progress = maxBorrow.roundToInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        btnEnable.text = "Use " + suppliedData.markets.underlyingSymbol + " as Collateral"
        try {
            availability.text = suppliedData.markets.underlyingSymbol + " available"
            val walletBalance = getWalletBalance(suppliedData)
            val tokenValues =
                walletBalance.toDouble() * suppliedData.markets.exchangeRate
            tokenAvailable.text = walletBalance + "=$" + UserInterface.round(tokenValues!!, 2)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ivClose.setOnClickListener {
            switch.isChecked = false
            enableCollateralDialog!!.dismiss()
        }
        btnEnable.setOnClickListener {
            enableCollateralDialog.dismiss()
            transactionEnterExitDialog(switch, address, pControllerAddress, suppliedData, "enter")
        }
        enableCollateralDialog.show()
    }


    //show enable collateral
    private fun showDisableCollateralDialog(
        address: String,
        pControllerAddress: String,
        switch: Switch,
        suppliedData: MarketData,
        isAllowToDisable: Boolean
    ) {
        val disableCollateralDialog = Dialog(context!!, R.style.CustomDialog)
        disableCollateralDialog!!.setContentView(R.layout.collateral_disable)
        val btnDisable = disableCollateralDialog!!.findViewById<TextView>(R.id.btn_disable)
        val ivClose = disableCollateralDialog!!.findViewById<ImageView>(R.id.iv_close)
        val availability = disableCollateralDialog.findViewById<TextView>(R.id.availability)
        val tokenAvailable = disableCollateralDialog.findViewById<TextView>(R.id.token_available)
        val msg = disableCollateralDialog.findViewById<TextView>(R.id.msg)
        val borrowLimitPbCollateral =
            disableCollateralDialog.findViewById<ProgressBar>(R.id.borrow_limit_pb)
        val maxBorrowLimitCollateral =
            disableCollateralDialog.findViewById<TextView>(R.id.max_borrow_limit)
        val changeMaxBorrowLimitCollateral =
            disableCollateralDialog.findViewById<TextView>(R.id.change_max_borrow_limit)
        val maxBorrowLimitPerCollateral =
            disableCollateralDialog.findViewById<TextView>(R.id.max_borrow_limit_per)
        val changeMaxBorrowLimitPerCollateral =
            disableCollateralDialog.findViewById<TextView>(R.id.change_max_borrow_limit_per)
        val borrowLimitTableCollateral =
            disableCollateralDialog.findViewById<TableRow>(R.id.borrow_limit)
        val borrowLimitPerTableCollateral =
            disableCollateralDialog.findViewById<TableRow>(R.id.borrow_limit_per)
        val d = ColorDrawable(Color.TRANSPARENT)
        disableCollateralDialog!!.window!!.setBackgroundDrawable(d)
        disableCollateralDialog!!.setCancelable(false)
        if (isAllowToDisable) {
            setTextViewHTML(
                msg,
                "This asset will no longer be used towards your borrowing limit, and can’t be seized in liquidation. <a href=learn_more>Learn more</a>."
            )
            btnDisable.text = "Disable " + suppliedData.markets.underlyingSymbol
            try {
                val suppliedValue = getSuppliedUSDExceptCurrent(suppliedData)
                maxBorrowLimitCollateral.text = "$" + UserInterface.round(totalBorrowLimitValue, 2)
                var usedBorrowLimitValue: Double? = 0.0
                if (totalBorrowLimitValue != 0.0)
                    usedBorrowLimitValue = totalBorrowedBalance / totalBorrowLimitValue * 100
                maxBorrowLimitPerCollateral.text =
                    UserInterface.round(usedBorrowLimitValue!!, 2).toString() + "%"
                val maxBorrow = (totalBorrowedBalance / (suppliedValue * 0.75)) * 100
                changeMaxBorrowLimitCollateral.text =
                    "$" + UserInterface.round((suppliedValue * 0.75), 2)
                changeMaxBorrowLimitPerCollateral.text =
                    UserInterface.round(maxBorrow, 2).toString() + "%"
                borrowLimitPbCollateral.progress = maxBorrow.roundToInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            setTextViewHTML(
                msg,
                "This asset is required to support your borrowed assets. Either repay borrowed assets, or supply another asset as collateral. <a href=learn_more>Learn more</a>."
            )
            btnDisable.text = "Dismiss"
            try {
                val suppliedValue = getSuppliedUSDExceptCurrent(suppliedData)
                maxBorrowLimitCollateral.text = "$" + UserInterface.round(totalBorrowLimitValue, 2)
                var usedBorrowLimitValue: Double? = 0.0
                if (totalBorrowLimitValue != 0.0)
                    usedBorrowLimitValue = totalBorrowedBalance / totalBorrowLimitValue * 100
                maxBorrowLimitPerCollateral.text = UserInterface.round(
                    usedBorrowLimitValue!!,
                    2
                ).toString() + "%"
                borrowLimitPbCollateral.progress = 100
                changeMaxBorrowLimitPerCollateral.text = "Liquidation"
                changeMaxBorrowLimitCollateral.text = "$" + UserInterface.round(suppliedValue, 2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        try {
            availability.text = suppliedData.markets.underlyingSymbol + " available"
            val walletBalance = getWalletBalance(suppliedData)
            val tokenValues =
                walletBalance.toDouble() * suppliedData.markets.exchangeRate
            tokenAvailable.text = walletBalance + "=$" + UserInterface.round(tokenValues!!, 2)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        ivClose.setOnClickListener {
            switch.isChecked = true
            disableCollateralDialog!!.dismiss()
        }
        btnDisable.setOnClickListener {
            disableCollateralDialog.dismiss()
            if (isAllowToDisable) {
                transactionEnterExitDialog(
                    switch,
                    address,
                    pControllerAddress,
                    suppliedData,
                    "exit"
                )
            } else {
                switch.isChecked = true
            }
        }
        disableCollateralDialog.show()
    }

    private fun setTextViewHTML(text: TextView, html: String) {
        val sequence = Html.fromHtml(html)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        text.text = strBuilder
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                try {
                    if (span.url == "learn_more") {

                    } else {
                        // Do something with span.getURL() to handle the link click...
                        val localIntent = Intent()
                        localIntent.action = "android.intent.action.VIEW"
                        localIntent.data = Uri.parse(span.url)
                        startActivity(localIntent)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }


    //show supplyAndWithdraw
    private fun showSupplyAndWithdrawDialog(
        address: String,
        marketAddress: String,
        isAllowanceEnable: Boolean,
        wallet: String,
        suppliedData: MarketData
    ) {
        if (supplyAndWithdrawDialog != null)
            if (supplyAndWithdrawDialog!!.isShowing)
                return
        supplyAndWithdrawDialog = Dialog(context!!, R.style.CustomDialog)
        supplyAndWithdrawDialog!!.setContentView(R.layout.supply_withdraw_main)
        val mPager = supplyAndWithdrawDialog!!.findViewById<ViewPager>(R.id.viewPager)
        val ivClose = supplyAndWithdrawDialog!!.findViewById<ImageView>(R.id.iv_close)
        val brandLogo = supplyAndWithdrawDialog!!.findViewById<ImageView>(R.id.brand_logo)
        val pesaLogo = supplyAndWithdrawDialog!!.findViewById<LinearLayout>(R.id.pesa_logo)
        val supplyTab = supplyAndWithdrawDialog!!.findViewById<LinearLayout>(R.id.supply_tab)
        val withdrawTab = supplyAndWithdrawDialog!!.findViewById<LinearLayout>(R.id.withdraw_tab)
        val supplyTxt = supplyAndWithdrawDialog!!.findViewById<TextView>(R.id.supply_txt)
        val withdrawTxt = supplyAndWithdrawDialog!!.findViewById<TextView>(R.id.withdraw_txt)
        val supplyDiv = supplyAndWithdrawDialog!!.findViewById<View>(R.id.supply_div)
        val withdrawDiv = supplyAndWithdrawDialog!!.findViewById<View>(R.id.withdraw_div)
        val d = ColorDrawable(Color.TRANSPARENT)
        supplyAndWithdrawDialog!!.window!!.setBackgroundDrawable(d)
        supplyAndWithdrawDialog!!.setCancelable(false)
        val adapter =
            MyPageAdapter(
                activity,
                suppliedData,
                enableAllowance,
                supplyListener,
                withdrawListener,
                isAllowanceEnable,
                address,
                marketAddress,
                wallet,
                totalBorrowedBalance,
                totalBorrowLimitValue,
                collateralSuppliedBalance
            )
        if (isAllowanceEnable)
            pesaLogo.visibility = View.GONE
        else {
            pesaLogo.visibility = View.VISIBLE
            brandLogo.setImageDrawable(
                context!!.getDrawable(
                    UserInterface.getCoinIcon(
                        suppliedData.markets.underlyingSymbol
                    )
                )
            )
        }
        mPager.adapter = adapter
        mPager.currentItem = 0
        ivClose.setOnClickListener {
            supplyAndWithdrawDialog!!.dismiss()
        }
        mPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        supplyTab!!.performClick()
                    }
                    1 -> {
                        withdrawTab!!.performClick()
                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        supplyTab!!.setOnClickListener {
            mPager!!.currentItem = 0
            supplyTxt.setTextColor(context!!.getColor(R.color.tab_select_text))
            withdrawTxt.setTextColor(context!!.getColor(R.color.tab_unselect_text))
            supplyDiv.visibility = View.VISIBLE
            withdrawDiv.visibility = View.GONE
        }
        withdrawTab!!.setOnClickListener {
            mPager!!.currentItem = 1
            supplyTxt.setTextColor(context!!.getColor(R.color.tab_unselect_text))
            withdrawTxt.setTextColor(context!!.getColor(R.color.tab_select_text))
            supplyDiv.visibility = View.GONE
            withdrawDiv.visibility = View.VISIBLE
        }
        supplyAndWithdrawDialog!!.show()
    }

    //clicked enable Repay Allowance
    private val repayAllowance = object : EnableSupplyListener {
        override fun onClickedEnable(
            address: String,
            marketAddress: String,
            marketData: MarketData
        ) {
            try {
                if (borrowAndRepayDialog != null)
                    if (borrowAndRepayDialog!!.isShowing)
                        borrowAndRepayDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            transactionApproveDialog(true, address, marketAddress, marketData)
        }

    }
    //clicked enable Supply Allowance
    private val enableAllowance = object : EnableSupplyListener {
        override fun onClickedEnable(
            address: String,
            marketAddress: String,
            marketData: MarketData
        ) {
            try {
                if (supplyAndWithdrawDialog != null)
                    if (supplyAndWithdrawDialog!!.isShowing)
                        supplyAndWithdrawDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            transactionApproveDialog(false, address, marketAddress, marketData)
        }

    }

    //clicked supply
    private val supplyListener = object : SupplyListener {
        override fun supplyTokens(supplyData: MarketData, values: String) {
            supplyAndWithdrawDialog!!.dismiss()
            transactionCfmDialog(
                supplyData.markets.id,
                values,
                supplyData.markets.underlyingDecimals,
                supplyData,
                "supply"
            )
        }

    }

    //clicked withdraw
    private val withdrawListener = object : WithdrawListener {
        override fun withdrawTokens(supplyData: MarketData, values: String) {
            supplyAndWithdrawDialog!!.dismiss()
            transactionCfmDialog(
                supplyData.markets.id,
                values,
                supplyData.markets.underlyingDecimals,
                supplyData,
                "withdraw"
            )
        }

    }

    //clicked repay
    private val repayListener = object : RepayListener {
        override fun repayTokens(borrowData: MarketData, values: String) {
            borrowAndRepayDialog!!.dismiss()
            transactionCfmDialog(
                borrowData.markets.id,
                values,
                borrowData.markets.underlyingDecimals,
                borrowData,
                "repay"
            )
        }
    }

    //clicked borrow
    private val borrowListener = object : BorrowListener {
        override fun borrowToken(borrowData: MarketData, values: String) {
            borrowAndRepayDialog!!.dismiss()
            transactionCfmDialog(
                borrowData.markets.id,
                values,
                borrowData.markets.underlyingDecimals,
                borrowData,
                "borrow"
            )
        }
    }

    //show borrowAndRepay
    private fun showBorrowAndRepayDialog(
        borrowData: MarketData
    ) {
        if (borrowAndRepayDialog != null)
            if (borrowAndRepayDialog!!.isShowing)
                return
        borrowAndRepayDialog = Dialog(context!!, R.style.CustomDialog)
        borrowAndRepayDialog!!.setContentView(R.layout.borrow_repay_main)
        val mPager = borrowAndRepayDialog!!.findViewById<ViewPager>(R.id.viewPager)
        val ivClose = borrowAndRepayDialog!!.findViewById<ImageView>(R.id.iv_close)
        val brandLogo = borrowAndRepayDialog!!.findViewById<ImageView>(R.id.brand_logo)
        val pesaLogo = borrowAndRepayDialog!!.findViewById<LinearLayout>(R.id.pesa_logo)
        val borrowTab = borrowAndRepayDialog!!.findViewById<LinearLayout>(R.id.borrow_tab)
        val rePayTab = borrowAndRepayDialog!!.findViewById<LinearLayout>(R.id.repay_tab)
        val borrowTxt = borrowAndRepayDialog!!.findViewById<TextView>(R.id.borrow_txt)
        val rePayTxt = borrowAndRepayDialog!!.findViewById<TextView>(R.id.repay_txt)
        val brandName = borrowAndRepayDialog!!.findViewById<TextView>(R.id.brand_name)
        val borrowDiv = borrowAndRepayDialog!!.findViewById<View>(R.id.borrow_div)
        val repayDiv = borrowAndRepayDialog!!.findViewById<View>(R.id.repay_div)
        val d = ColorDrawable(Color.TRANSPARENT)
        borrowAndRepayDialog!!.window!!.setBackgroundDrawable(d)
        borrowAndRepayDialog!!.setCancelable(false)
        val isAllowance = borrowData.markets.isUnderlyingApproved
        val walletBalance = getWalletBalance(borrowData)
        val adapter = finance.pesa.sdk.view.adapter.BorrowRepayPagerAdapter(
            activity,
            repayAllowance,
            borrowListener,
            repayListener,
            isAllowance,
            walletBalance,
            borrowData.markets.underlyingAddress,
            borrowData.markets.id,
            maxBorrowLimit,
            totalBorrowLimit,
            borrowData,
            totalBorrowedBalance,
            totalBorrowLimitValue,
            totalSuppliedBalance
        )
        pesaLogo.visibility = View.GONE

        mPager.adapter = adapter
        mPager.currentItem = 0
        ivClose.setOnClickListener {
            borrowAndRepayDialog!!.dismiss()
        }
        mPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        pesaLogo.visibility = View.GONE
                        borrowTab!!.performClick()
                    }
                    1 -> {
                        if (isAllowance)
                            pesaLogo.visibility = View.GONE
                        else {
                            brandName.text = borrowData.markets.underlyingName
                            brandLogo.setImageDrawable(
                                context!!.getDrawable(
                                    UserInterface.getCoinIcon(
                                        borrowData.markets.underlyingSymbol
                                    )
                                )
                            )
                            pesaLogo.visibility = View.VISIBLE
                        }
                        rePayTab!!.performClick()
                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        borrowTab!!.setOnClickListener {
            mPager!!.currentItem = 0
            borrowTxt.setTextColor(context!!.getColor(R.color.tab_select_text))
            rePayTxt.setTextColor(context!!.getColor(R.color.tab_unselect_text))
            borrowDiv.visibility = View.VISIBLE
            repayDiv.visibility = View.GONE
        }
        rePayTab!!.setOnClickListener {
            mPager!!.currentItem = 1
            borrowTxt.setTextColor(context!!.getColor(R.color.tab_unselect_text))
            rePayTxt.setTextColor(context!!.getColor(R.color.tab_select_text))
            borrowDiv.visibility = View.GONE
            repayDiv.visibility = View.VISIBLE
        }
        borrowAndRepayDialog!!.show()
    }

    //load borrow and supply wallet task
    /* private inner class LoadSupplyAndWalletTask(investDatas: List<InvestData>) :
         AsyncTask<String, String, String>() {
         var tokens: List<InvestData>? = emptyList()

         init {
             this.tokens = investDatas
         }

         override fun onPreExecute() {
             mShimmerViewContainer!!.startShimmerAnimation()
             borrowShimmerViewContainer!!.startShimmerAnimation()
             supplyLay!!.visibility = View.GONE
             borrowLay!!.visibility = View.GONE
             mShimmerViewContainer!!.visibility = View.VISIBLE
             borrowShimmerViewContainer!!.visibility = View.VISIBLE
         }

         override fun doInBackground(vararg args: String): String? {
             loadTokenValues(tokens!!)
             return null
         }

         override fun onPostExecute(result: String?) {

         }
     }*/


    // enable allowance balance task
    private inner class EnableTask(
        isRepay: Boolean,
        address: String,
        marketAddress: String,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        internal var marketAddress: String? = null
        internal var address: String? = null
        internal var isRepay: Boolean? = false
        internal var marketData: MarketData

        init {
            this.marketAddress = marketAddress
            this.address = address
            this.isRepay = isRepay
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return approveToken(address!!, marketAddress!!, marketData!!, isRepay!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {
                    if (supplyAndWithdrawDialog != null)
                        if (supplyAndWithdrawDialog!!.isShowing)
                            supplyAndWithdrawDialog!!.dismiss()
                    if (borrowAndRepayDialog != null)
                        if (borrowAndRepayDialog!!.isShowing)
                            borrowAndRepayDialog!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
                loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            } else {
                UserInterface.showToast(context, "Failed to enable...")
            }
        }
    }


    //supply token task
    private inner class supplyTokensTask(
        marketAddress: String,
        supplyValue: String,
        factor: Int,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        var supplyValue: String? = null
        var marketAddress: String? = null
        var factor: Int? = null
        var marketData: MarketData

        init {
            this.supplyValue = supplyValue
            this.marketAddress = marketAddress
            this.factor = factor
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return supplyToken(marketAddress!!, supplyValue!!, factor!!, marketData!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {
                    if (supplyAndWithdrawDialog != null)
                        if (supplyAndWithdrawDialog!!.isShowing)
                            supplyAndWithdrawDialog!!.dismiss()
                    if (borrowAndRepayDialog != null)
                        if (borrowAndRepayDialog!!.isShowing)
                            borrowAndRepayDialog!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
                loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            } else {
                UserInterface.showToast(context, "Failed to supply...")
            }
        }
    }


    //withdraw token task
    private inner class withdrawTokensTask(
        marketAddress: String,
        supplyValue: String,
        factor: Int,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        var withdrawValue: String? = null
        var marketAddress: String? = null
        var factor: Int? = null
        var marketData: MarketData

        init {
            this.withdrawValue = supplyValue
            this.marketAddress = marketAddress
            this.factor = factor
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return withdrawToken(marketAddress!!, withdrawValue!!, factor!!, marketData!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {
                    if (supplyAndWithdrawDialog != null)
                        if (supplyAndWithdrawDialog!!.isShowing)
                            supplyAndWithdrawDialog!!.dismiss()
                    if (borrowAndRepayDialog != null)
                        if (borrowAndRepayDialog!!.isShowing)
                            borrowAndRepayDialog!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
                loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            } else {
                UserInterface.showToast(context, "Failed to withdraw...")
            }
        }
    }


    //enable collateral token task
    private inner class EnableCollateralTask(
        marketAddress: String,
        pControllerAddress: String,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        var marketAddress: String? = null
        var pControllerAddress: String? = null
        var marketData: MarketData

        init {
            this.marketAddress = marketAddress
            this.pControllerAddress = pControllerAddress
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return enterMarkets(marketAddress!!, pControllerAddress!!, marketData!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            if (result!!) {
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.showToast(context, "Failed to enable...")
            }
        }
    }


    //disable collateral token task
    private inner class DisableCollateralTask(
        marketAddress: String,
        pControllerAddress: String,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        var marketAddress: String? = null
        var pControllerAddress: String? = null
        var marketData: MarketData

        init {
            this.marketAddress = marketAddress
            this.pControllerAddress = pControllerAddress
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return exitMarket(marketAddress!!, pControllerAddress!!, marketData!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            if (result!!) {
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.showToast(context, "Failed to disable...")
            }
        }
    }


    //borrow token task
    private inner class borrowTokensTask(
        marketAddress: String,
        values: String,
        factor: Int,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        var values: String? = null
        var marketAddress: String? = null
        var factor: Int? = null
        var marketData: MarketData

        init {
            this.values = values
            this.marketAddress = marketAddress
            this.factor = factor
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return borrowToken(marketAddress!!, values!!, factor!!, marketData!!)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {
                    if (supplyAndWithdrawDialog != null)
                        if (supplyAndWithdrawDialog!!.isShowing)
                            supplyAndWithdrawDialog!!.dismiss()
                    if (borrowAndRepayDialog != null)
                        if (borrowAndRepayDialog!!.isShowing)
                            borrowAndRepayDialog!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
                loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            } else {
                UserInterface.showToast(context, "borrow failed...")
            }
        }
    }

    //repay token task
    private inner class rePayTokensTask(
        marketAddress: String,
        values: String,
        factor: Int,
        marketData: MarketData
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        var values: String? = null
        var marketAddress: String? = null
        var factor: Int? = null
        var marketData: MarketData

        init {
            this.values = values
            this.marketAddress = marketAddress
            this.factor = factor
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            return rePayToken(marketAddress!!, values!!, factor!!, marketData)
        }

        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                try {
                    if (supplyAndWithdrawDialog != null)
                        if (supplyAndWithdrawDialog!!.isShowing)
                            supplyAndWithdrawDialog!!.dismiss()
                    if (borrowAndRepayDialog != null)
                        if (borrowAndRepayDialog!!.isShowing)
                            borrowAndRepayDialog!!.dismiss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
                loadAccountSummary(1, Constants.getAccountCredential()!!.address)
            } else {
                UserInterface.showToast(context, "repay failed...")
            }
        }
    }


    //load token values
    private fun loadTokenValues(investDash: InvestDash) {
        try {
            /* activity!!.runOnUiThread {
                 mShimmerViewContainer!!.startShimmerAnimation()
                 borrowShimmerViewContainer!!.startShimmerAnimation()
                 supplyLay!!.visibility = View.GONE
                 suppliedLay!!.visibility = View.GONE
                 borrowLay!!.visibility = View.GONE
                 mShimmerViewContainer!!.visibility = View.VISIBLE
                 borrowShimmerViewContainer!!.visibility = View.VISIBLE
             }*/
            supplyDatas = ArrayList()
            borrowDatas = ArrayList()
            remainDatas = ArrayList()
            totalSuppliedBalance = 0.0
            totalBorrowedBalance = 0.0
            maxBorrowLimit = 0.0
            totalBorrowLimit = 0.0
            totalBorrowLimitValue = 0.0
            collateralSuppliedBalance = 0.0

            val allInvestDatas: List<InvestData> =
                investDash.markets.sortedBy { investData -> investData.underlyingSymbol }

            for (marketData in allInvestDatas) {
                when {
                    marketData.totalUnderlyingSupplied != 0.0 -> {
                        try {
                            val suppliedBal = marketData.totalUnderlyingSuppliedinUSD

                            if (marketData.enteredMarket) {
                                collateralSuppliedBalance += suppliedBal
                                totalBorrowLimitValue += suppliedBal * marketData.collateralFactor
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        val supplyData = MarketData(
                            marketData, true, false
                        )
                        supplyDatas!!.add(supplyData)
                    }
                    marketData.storedBorrowBalance != 0.0 -> {
                        val borrowData =
                            MarketData(marketData, false, true)
                        borrowDatas!!.add(borrowData)
                        totalBorrowedBalance += borrowData.markets.totalstoredBorrowBalanceinUSD
                    }
                    else -> {
                        val supplyData = MarketData(
                            marketData, false, false
                        )
                        remainDatas!!.add(supplyData)
                    }
                }
            }

            activity!!.runOnUiThread {
                try {
                    totalSuppliedBalance = collateralSuppliedBalance
                    var usedBorrowLimitPer = (totalBorrowedBalance / totalBorrowLimitValue) * 100
                    /*totalSuppliedBalance = investDash.overAllSupplied
                    totalBorrowedBalance = investDash.overAllBorrowed
                    totalBorrowLimitValue = investDash.borrowedLimit
                    var usedBorrowLimitPer = investDash.borrowedInPercentage*/
                    borrowLimit!!.text = "$" + UserInterface.round(totalBorrowLimitValue)
                    usedBorrowLimit!!.text = (usedBorrowLimitPer.toInt()).toString() + "%"
                    borrowProgressBar!!.progress = usedBorrowLimitPer.toInt()
                    totalBorrowLimit = (totalBorrowLimitValue - totalBorrowedBalance)
                    maxBorrowLimit = (totalBorrowLimitValue - totalBorrowedBalance) * 0.80
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                /* mShimmerViewContainer!!.stopShimmerAnimation()
                 borrowShimmerViewContainer!!.stopShimmerAnimation()
                 mShimmerViewContainer!!.visibility = View.GONE
                 borrowShimmerViewContainer!!.visibility = View.GONE*/
                supplyBalance!!.text = "$" + UserInterface.roundEight(totalSuppliedBalance)
                borrowBalance!!.text = "$" + UserInterface.roundEight(totalBorrowedBalance)
                netAPY!!.text = UserInterface.round(investDash.netAPY) + "%"
                //apyProgresbar!!.max=investDash.supplyBarValue.toInt()
                apyProgresbar!!.progress = investDash.borrowBarValue.toInt()
                if (supplyDatas!!.isNotEmpty()) {
                    // suppliedLay!!.visibility = View.VISIBLE
                    suppliedAdapter = SuppliedAdapter(
                        context!!,
                        supplyDatas!!,
                        supplySelectListener
                    )
                    suppliedRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    suppliedRecyclerView!!.adapter = suppliedAdapter
                }/*else{
                    suppliedLay!!.visibility = View.GONE
                }*/
                if (borrowDatas!!.isNotEmpty()) {
                    // borrowedLay!!.visibility = View.VISIBLE
                    borrowedAdapter = BorrowedAdapter(
                        context!!,
                        borrowDatas!!,
                        borrowSelectListener,
                        totalBorrowLimitValue
                    )
                    borrowedRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    borrowedRecyclerView!!.adapter = borrowedAdapter
                }/*else{
                    borrowedLay!!.visibility = View.GONE
                }*/
                if (remainDatas!!.isNotEmpty()) {
                    supplyLay!!.visibility = View.VISIBLE
                    borrowLay!!.visibility = View.VISIBLE
                    supplyAdapter = SupplyAdapter(
                        context!!,
                        remainDatas!!,
                        supplySelectListener
                    )
                    supplyRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    supplyRecyclerView!!.adapter = supplyAdapter

                    borrowAdapter = BorrowAdapter(
                        context!!,
                        remainDatas!!,
                        borrowSelectListener
                    )
                    borrowRecyclerView!!.layoutManager = LinearLayoutManager(activity)
                    borrowRecyclerView!!.adapter = borrowAdapter
                }

            }
            Constants.setRefreshWalletListener(refreshWalletListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //refresh wallet balance
    private val refreshWalletListener = object : RefreshWalletListener {
        override fun onMarketLoaded() {
            loadTokenValues(Constants.getInvestDashboardValue()!!)
        }

        override fun onRefreshInvest() {
            PesaApplication.currentActivity!!.runOnUiThread {
                try {
                    if (supplyAdapter != null) {
                        if (supplyAdapter!!.itemCount > 0) {
                            supplyAdapter!!.notifyDataSetChanged()
                        }
                    }
                    if (suppliedAdapter != null) {
                        if (suppliedAdapter!!.itemCount > 0) {
                            suppliedAdapter!!.notifyDataSetChanged()
                        }
                    }
                    if (borrowAdapter != null) {
                        if (borrowAdapter!!.itemCount > 0) {
                            borrowAdapter!!.notifyDataSetChanged()
                        }
                    }
                    if (borrowAdapter != null) {
                        if (borrowAdapter!!.itemCount > 0) {
                            borrowAdapter!!.notifyDataSetChanged()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onRefreshWallet() {

        }
    }


    //supply onItem select Listener
    private val supplySelectListener = object : SupplySelectListener {
        override fun collateralClick(
            isEnable: Boolean,
            switchCollateral: Switch,
            suppliedData: MarketData
        ) {
            if (isEnable) {
                showEnableCollateralDialog(
                    suppliedData.markets.id,
                    pControllerAddress,
                    switchCollateral!!,
                    suppliedData
                )
            } else {
                showDisableCollateralDialog(
                    suppliedData.markets.id,
                    pControllerAddress,
                    switchCollateral!!,
                    suppliedData,
                    checkAllowDisable(suppliedData)
                )
            }
        }

        override fun onSupplyItemSelected(suppliedData: MarketData) {
            val isAllowance = suppliedData.markets.isUnderlyingApproved
            val walletBalance = getWalletBalance(suppliedData)
            showSupplyAndWithdrawDialog(
                suppliedData.markets.underlyingAddress,
                suppliedData.markets.id,
                isAllowance,
                walletBalance,
                suppliedData
            )
        }

    }

    // find allow to disable collateral
    private fun checkAllowDisable(suppliedData: MarketData): Boolean {
        var isAllowed = false
        try {
            if (suppliedData.markets.totalUnderlyingSuppliedinUSD <= 0)
                return true
            var suppliedBal = 0.0
            for (suppliedvalue in supplyDatas!!) {
                if (suppliedvalue.markets.enteredMarket && suppliedvalue.markets.totalUnderlyingSuppliedinUSD > 0 && suppliedvalue.markets.underlyingAddress != suppliedData.markets.underlyingAddress) {
                    suppliedBal += (suppliedvalue.markets.totalUnderlyingSuppliedinUSD * suppliedData.markets.collateralFactor)
                }
            }
            if (suppliedBal >= totalBorrowedBalance) {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isAllowed
    }

    // get supplied token for all except current
    private fun getSuppliedUSDExceptCurrent(suppliedData: MarketData): Double {
        var suppliedBal = 0.0
        try {
            for (suppliedvalue in supplyDatas!!) {
                if (suppliedvalue.markets.enteredMarket && suppliedvalue.markets.underlyingAddress != suppliedData.markets.underlyingAddress) {
                    suppliedBal += suppliedvalue.markets.totalUnderlyingSuppliedinUSD
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return suppliedBal
    }


    //get wallet balance from local
    private fun getWalletBalance(marketData: MarketData): String {
        val walletBalance = "0.0000"
        try {
            if (Constants.getMarketValues()!!.containsKey(marketData.markets.underlyingSymbol)) {
                return Constants.getMarketValues()!![marketData.markets.underlyingSymbol]!!.walletBalance
            } else {
                return if (marketData.markets.underlyingAddress != ETH_UnderlyingAddress) {
                    getTokenWallet(
                        marketData.markets.underlyingAddress,
                        Constants.getAccountCredential()!!,
                        marketData.markets.underlyingDecimals
                    )
                } else {
                    getCoinWallet(
                        marketData.markets.underlyingAddress,
                        marketData.markets.underlyingDecimals
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return walletBalance
    }

    //get is allowance enable from local
    private fun getIsAllowance(marketData: MarketData): Boolean {
        val isAllowance = false
        try {
            return if (Constants.getMarketValues()!!.containsKey(marketData.markets.underlyingSymbol)) {
                Constants.getMarketValues()!![marketData.markets.underlyingSymbol]!!.isAllowance
            } else {
                isAllowance(
                    marketData.markets.underlyingAddress,
                    marketData.markets.id,
                    Constants.getAccountCredential()!!
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isAllowance
    }

    //borrow onItem select listener
    private val borrowSelectListener = object : BorrowSelectListener {
        override fun onSBorrowItemSelected(borrowData: MarketData) {
            showBorrowAndRepayDialog(borrowData)
        }
    }

    // get allowance balance is enable or disable
    private fun isAllowance(
        address: String,
        marketAddress: String,
        credentials: Credentials
    ): Boolean {
        try {
            val erc20Token =
                ERC20.load(address, PesaApplication.getWeb3j(), credentials, CustomGasProvider())
            val allowanceBalance =
                erc20Token.allowance(Keys.toChecksumAddress(credentials.address), marketAddress)
                    .sendAsync().get()
            return allowanceBalance.toString() != "0"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    // get supplied balance
    private fun getSuppliedBalance(
        marketAddress: String,
        credentials: Credentials,
        underlyingDecimals: Int
    ): String {
        try {
            val pErc20Delegator =
                PErc20Delegator.load(
                    marketAddress,
                    PesaApplication.getWeb3j(),
                    credentials,
                    CustomGasProvider()
                )
            val suppliedBalanceValue =
                pErc20Delegator.balanceOf(Keys.toChecksumAddress(credentials.address)).sendAsync()
                    .get()
            val suppliedBalance =
                suppliedBalanceValue
                    .divide(BigInteger(PESA_TOKEN))
            return suppliedBalance.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0"
    }

    //get borrow limit
    private fun getBorrowLimit(
        credentials: Credentials,
        underlyingDecimals: Int
    ): String {
        try {
            val pController = PController.load(
                pControllerAddress,
                PesaApplication.getWeb3j(),
                credentials,
                CustomGasProvider(),
                context
            )
            val borrowLimit =
                pController.getAccountLiquidity(Keys.toChecksumAddress(credentials.address))
                    .sendAsync().get()

            return UserInterface.UnitDivide(borrowLimit.component2(), underlyingDecimals).toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "0"
    }

    //getCoinWallet
    private fun getCoinWallet(
        address: String,
        underlyingDecimals: Int
    ): String {
        try {
            val walletBalance = UserInterface.UnitDivide(
                (PesaApplication.getWeb3j()!!
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
                ERC20.load(address, PesaApplication.getWeb3j(), credentials, CustomGasProvider())
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

    // Transaction confirmation for approve
    private fun transactionApproveDialog(
        isRepay: Boolean,
        address: String,
        marketAddress: String,
        marketData: MarketData
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
                    isRepay,
                    address,
                    marketAddress,
                    marketData
                ).execute()
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

    // Transaction confirmation for enter and exit market
    private fun transactionEnterExitDialog(
        switch: Switch,
        address: String,
        pControllerAddress: String,
        marketData: MarketData,
        functionName: String
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
                when (functionName) {
                    "enter" -> EnableCollateralTask(
                        address,
                        pControllerAddress,
                        marketData
                    ).execute()
                    "exit" -> DisableCollateralTask(
                        address,
                        pControllerAddress,
                        marketData
                    ).execute()
                }

            }
            btnReject?.setOnClickListener {
                cfmDialog?.dismiss()
                when (functionName) {
                    "enter" -> switch.isChecked = false
                    "exit" -> switch.isChecked = true
                }
            }
            btnClose?.setOnClickListener {
                cfmDialog?.dismiss()
                when (functionName) {
                    "enter" -> switch.isChecked = false
                    "exit" -> switch.isChecked = true
                }
            }
            cfmDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Transaction confirmation
    private fun transactionCfmDialog(
        marketAddress: String,
        token: String,
        factor: Int,
        marketData: MarketData,
        functionName: String
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
                when (functionName) {
                    "supply" -> supplyTokensTask(marketAddress, token, factor, marketData).execute()
                    "borrow" -> borrowTokensTask(marketAddress, token, factor, marketData).execute()
                    "withdraw" -> withdrawTokensTask(
                        marketAddress,
                        token,
                        factor,
                        marketData
                    ).execute()
                    "repay" -> rePayTokensTask(marketAddress, token, factor, marketData).execute()
                }

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

    //supply token
    private fun supplyToken(
        marketAddress: String,
        token: String,
        factor: Int,
        marketData: MarketData
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text =
                    "Supply " + token + " " + marketData.markets.underlyingSymbol
                investScroolView?.scrollTo(0, 0)
            }
            //Using Market key
            val credentials = Constants.getAccountCredential()
            var transactionHash = ""
            if (marketData.markets.underlyingAddress == ETH_UnderlyingAddress) {
                val pth =
                    PEth.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                val tokenVal =
                    Convert.toWei(token.toBigDecimal(), Convert.Unit.ETHER).toBigInteger()
                transactionHash =
                    pth.mint(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            } else {
                val pErc20Delegator =
                    PErc20Delegator.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                var tokenVal = UserInterface.UnitMultiply(
                    BigDecimal(token),
                    factor
                ).toBigInteger()
                transactionHash =
                    pErc20Delegator.mint(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            }

            Log.d("supply_Receipt_Hash-->", transactionHash)

            val transactionReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

    //withdraw token
    private fun withdrawToken(
        marketAddress: String,
        token: String,
        factor: Int,
        marketData: MarketData
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text =
                    "Withdraw " + token + " " + marketData.markets.underlyingSymbol
                investScroolView?.scrollTo(0, 0)
            }
            //Using Market key
            val credentials = Constants.getAccountCredential()
            var transactionHash = ""
            if (marketData.markets.underlyingAddress == ETH_UnderlyingAddress) {
                val pth =
                    PEth.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                val tokenVal =
                    Convert.toWei(token.toBigDecimal(), Convert.Unit.ETHER).toBigInteger()
                transactionHash =
                    pth.redeemUnderlying(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            } else {
                val pErc20Delegator =
                    PErc20Delegator.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                var tokenVal = UserInterface.UnitMultiply(
                    BigDecimal(token),
                    factor
                ).toBigInteger()
                transactionHash =
                    pErc20Delegator.redeemUnderlying(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            }
            Log.d("Redeem_Receipt_Hash-->", transactionHash)
            val transactionReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

    //borrow token
    private fun borrowToken(
        marketAddress: String,
        token: String,
        factor: Int,
        marketData: MarketData
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text =
                    "Borrow " + token + " " + marketData.markets.underlyingSymbol
                investScroolView?.scrollTo(0, 0)
            }
            val credentials = Constants.getAccountCredential()
            var transactionHash = ""
            if (marketData.markets.underlyingAddress == ETH_UnderlyingAddress) {
                val pth =
                    PEth.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                val tokenVal =
                    Convert.toWei(token.toBigDecimal(), Convert.Unit.ETHER).toBigInteger()
                transactionHash =
                    pth.borrow(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            } else {
                val pErc20Delegator =
                    PErc20Delegator.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                var tokenVal = UserInterface.UnitMultiply(
                    BigDecimal(token),
                    factor
                ).toBigInteger()
                transactionHash =
                    pErc20Delegator.borrow(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            }
            Log.d("borrow_Receipt_Hash-->", transactionHash)
            val transactionReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

    //rePay token
    private fun rePayToken(
        marketAddress: String,
        token: String,
        factor: Int,
        marketData: MarketData
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text =
                    "Repay " + token + " " + marketData.markets.underlyingSymbol
                investScroolView?.scrollTo(0, 0)
            }
            val credentials = Constants.getAccountCredential()
            var transactionHash = ""
            if (marketData.markets.underlyingAddress == ETH_UnderlyingAddress) {
                val pth =
                    PEth.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                val tokenVal =
                    Convert.toWei(token.toBigDecimal(), Convert.Unit.ETHER).toBigInteger()
                transactionHash =
                    pth.repayBorrow(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            } else {
                val pErc20Delegator =
                    PErc20Delegator.load(
                        marketAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                var tokenVal = UserInterface.UnitMultiply(
                    BigDecimal(token),
                    factor
                ).toBigInteger()
                transactionHash =
                    pErc20Delegator.repayBorrow(
                        tokenVal
                    )
                        .sendAsync().get().transactionHash
            }
            Log.d("rePay_Receipt_Hash-->", transactionHash)
            val transactionReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

    //enable Allowance
    private fun approveToken(
        address: String,
        marketAddress: String,
        marketData: MarketData,
        isRepay: Boolean
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text =
                    "Enable " + marketData.markets.underlyingSymbol
                investScroolView?.scrollTo(0, 0)
            }
            val credentials = Constants.getAccountCredential()
            val ercToken =
                ERC20.load(address, PesaApplication.getWeb3j(), credentials, CustomGasProvider())
            val transactionReceipt = ercToken.approve(
                marketAddress, BigInteger(allowanceBalanceLimit)
            )
            val receipt = transactionReceipt.sendAsync().get()
            Log.d("Approve_Allowance_Receipt_Hash-->", receipt.transactionHash)
            val marketReceipt =
                PesaApplication.getWeb3j().ethGetTransactionReceipt(receipt.transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (marketReceipt.isStatusOK && marketReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

    //enable collateral
    private fun enterMarkets(
        address: String,
        pControllerAddress: String,
        marketData: MarketData
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text = "Enable for Borrowing"
                investScroolView?.scrollTo(0, 0)
            }
            val credentials = Constants.getAccountCredential()
            val pController = PController.load(
                pControllerAddress,
                PesaApplication.getWeb3j(),
                credentials,
                CustomGasProvider(),
                context
            )
            val marketAddress = ArrayList<String>()
            marketAddress.add(address)
            val enterMarketsReceipt = pController.enterMarkets(marketAddress).sendAsync().get()
            Log.d("EnterMarket_Receipt_Hash-->", enterMarketsReceipt.transactionHash)
            val marketReceipt =
                PesaApplication.getWeb3j()
                    .ethGetTransactionReceipt(enterMarketsReceipt.transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (marketReceipt.isStatusOK && marketReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

    //disable collateral
    private fun exitMarket(
        address: String,
        pControllerAddress: String,
        marketData: MarketData
    ): Boolean {
        try {
            activity?.runOnUiThread {
                pendingTransactionLay?.visibility = View.VISIBLE
                pendingTransactionMsg?.text = "Disable for Borrowing"
                investScroolView?.scrollTo(0, 0)
            }
            val credentials = Constants.getAccountCredential()
            val pController = PController.load(
                pControllerAddress,
                PesaApplication.getWeb3j(),
                credentials,
                CustomGasProvider(),
                context
            )
            val exitMarketReceipt =
                pController.exitMarket(address).sendAsync()
                    .get()
            Log.d("ExitMarket_Receipt_Hash-->", exitMarketReceipt.transactionHash)
            val marketReceipt =
                PesaApplication.getWeb3j()
                    .ethGetTransactionReceipt(exitMarketReceipt.transactionHash)
                    .sendAsync().get().transactionReceipt.get()
            if (marketReceipt.isStatusOK && marketReceipt.logs.isNotEmpty()) {
                activity?.runOnUiThread {
                    pendingTransactionLay?.visibility = View.GONE
                }
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity?.runOnUiThread {
            pendingTransactionLay?.visibility = View.GONE
        }
        return false
    }

}