package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.ActivitiesData
import finance.pesa.sdk.Model.ActivityDetails
import finance.pesa.sdk.Model.ResponseStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.EPN
import finance.pesa.sdk.generator.PaymentGateway
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.CustomGasProvider
import finance.pesa.sdk.utils.FetchDataBackgroungService
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.Interface.ActivitiesUpdateListener
import finance.pesa.sdk.view.Interface.RequestActivityListerner
import finance.pesa.sdk.view.UI.ActivitiesFragment.Companion.activitiesFragment
import finance.pesa.sdk.view.adapter.AllActivityAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.tx.gas.StaticGasProvider
import org.web3j.utils.Convert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class AllActivitiesFragment : Fragment() {

    private var activityRecycler: RecyclerView? = null
    private var mShimmerViewContainer: ShimmerFrameLayout? = null
    private var title: String = "all"


    companion object {
        fun newInstance(title: String): AllActivitiesFragment {
            val allActivitiesFragment = AllActivitiesFragment()
            allActivitiesFragment.title = title
            return allActivitiesFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.all_activity_main, container, false)
        activityRecycler = view.findViewById(R.id.activity_recycler)
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container)
        Constants.setActivitiesUpdateListener(activitiesUpdateListener)
        onUIUpdate()
        return view
    }

    private val activitiesUpdateListener = object : ActivitiesUpdateListener {
        override fun onActivityUpdate(activitiesData: ActivitiesData) {
            onUIUpdate()
        }

    }

    private fun onUIUpdate() {
        try {
            activityRecycler?.visibility = View.GONE
            mShimmerViewContainer?.visibility = View.VISIBLE
            mShimmerViewContainer?.startShimmerAnimation()
            if (activitiesFragment != null) {
                if (activitiesFragment!!.activitiesData != null) {
                    mShimmerViewContainer?.stopShimmerAnimation()
                    activityRecycler?.visibility = View.VISIBLE
                    mShimmerViewContainer?.visibility = View.GONE
                    val data = activitiesFragment!!.activitiesData
                    var activityValues = data!!.all
                    when (title) {
                        Constants.ALL -> activityValues = data!!.all
                        Constants.SENT -> activityValues = data!!.send
                        Constants.REQUEST -> activityValues = data!!.request
                        Constants.RECEIVED -> activityValues = data!!.received
                        Constants.ESCROW -> activityValues = data!!.escrow
                    }
                    if (activityValues.isNullOrEmpty()) {

                    } else {
                        val adapter: AllActivityAdapter? =
                            AllActivityAdapter(context!!, activityValues, requestActivityListerner)
                        activityRecycler?.layoutManager = LinearLayoutManager(activity)
                        activityRecycler?.adapter = adapter
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val requestActivityListerner = object : RequestActivityListerner {
        override fun onRequestClicked(activityDetails: ActivityDetails) {
            CheckIsRegistered(activityDetails).execute()
        }

        override fun onCliamClicked(activityDetails: ActivityDetails) {
            claimMoneyDialog(activityDetails)
        }

    }

    // Transaction confirmation for Claim Money
    private fun claimMoneyDialog(
        activityDetails: ActivityDetails
    ) {
        try {
            val claimDialog = Dialog(activity!!, R.style.CustomDialog)
            claimDialog.setContentView(R.layout.claim_money)
            val d = ColorDrawable(Color.TRANSPARENT)
            claimDialog.window!!.setBackgroundDrawable(d)
            claimDialog.setCancelable(false)
            val tvAmount = claimDialog.findViewById<TextView>(R.id.usd_value)
            val tvSendTo = claimDialog.findViewById<TextView>(R.id.tv_send_to)
            val brandLogo = claimDialog.findViewById<ImageView>(R.id.brand_logo)
            val tvGasFee = claimDialog.findViewById<TextView>(R.id.tv_gas_fee)
            val tvCurrency = claimDialog.findViewById<TextView>(R.id.currency_value)
            val tvNote = claimDialog.findViewById<TextView>(R.id.tv_note)
            val btnClaim = claimDialog.findViewById<Button>(R.id.btn_claim)
            val btnCancel = claimDialog.findViewById<Button>(R.id.btn_cancel)
            val noteView = claimDialog.findViewById<LinearLayout>(R.id.note_view)
            try {
                if (activityDetails.notes.isNullOrEmpty())
                    noteView.visibility = View.GONE
                tvNote?.text = activityDetails.notes
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val gasFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toDouble()
                tvGasFee.text = "GAS fee: " + UserInterface.round(gasFee) + " ETH"
                tvSendTo.text = "+" + activityDetails.fromEPN
                val perUSD = Constants.getUSDValueFromCurrency(activityDetails.cryptoSymbol)
                val coinValue = activityDetails.amount!!
                val usdValue = coinValue * perUSD
                brandLogo.setImageDrawable(
                    context!!.getDrawable(
                        UserInterface.getCoinIcon(
                            activityDetails.cryptoSymbol
                        )
                    )
                )

                tvAmount.text =
                    "$" + UserInterface.roundTwo(usdValue)
                tvCurrency.text =
                    UserInterface.round(coinValue) + " " + activityDetails.cryptoSymbol
            } catch (e: Exception) {
                e.printStackTrace()
            }


            btnClaim?.setOnClickListener {
                claimDialog?.dismiss()
                if (activityDetails.isExpired)
                    RefundMoneyTask(activityDetails).execute()
                else
                    ClaimMoneyTask(activityDetails).execute()
            }

            btnCancel?.setOnClickListener {
                claimDialog?.dismiss()
            }
            claimDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //RefundMoney task
    private inner class RefundMoneyTask(
        activityDetails: ActivityDetails
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var activityDetails: ActivityDetails? = null
        private var errorMsg = "Your transaction has been failed..."
        private var transactionHash = ""

        init {
            this.activityDetails = activityDetails
        }

        override fun onPreExecute() {
            UserInterface.showTransactionProgress(
                "Claiming money",
                "Transaction is being proceeded",
                context
            )
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val paymentGateway = PaymentGateway.load(
                    Constants.PAYMENT_Address,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                val isETH = activityDetails!!.cryptoSymbol == "ETH"
                if (isETH) {
                    transactionHash = paymentGateway.refundEthAmount(
                        activityDetails!!.from.toBigInteger(),
                        activityDetails!!.to.toBigInteger()
                    ).sendAsync().get().transactionHash
                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("RefundMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                } else {
                    val marketData =
                        Constants.getMarketFromCurrencySymbol(activityDetails!!.cryptoSymbol)
                    transactionHash = paymentGateway.refundErc20Token(
                        activityDetails!!.from.toBigInteger(),
                        activityDetails!!.to.toBigInteger(),
                        marketData!!.underlyingAddress
                    ).sendAsync().get().transactionHash
                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("RefundMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e.message!!.contains("Insufficient funds")) {
                    this.errorMsg = "You have insufficient funds..."
                }
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                try {
                    updateTransaction(1, activityDetails!!, transactionHash!!, "claimed", false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.hideProgress(context)
                UserInterface.showTransactionFailedDialog(errorMsg, context!!)
            }
        }
    }

    //ClaimMoney task
    private inner class ClaimMoneyTask(
        activityDetails: ActivityDetails
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var activityDetails: ActivityDetails? = null
        private var errorMsg = "Your transaction has been failed..."
        private var transactionHash = ""

        init {
            this.activityDetails = activityDetails
        }

        override fun onPreExecute() {
            UserInterface.showTransactionProgress(
                "Claiming money",
                "Transaction is being proceeded",
                context
            )
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val paymentGateway = PaymentGateway.load(
                    Constants.PAYMENT_Address,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                val isETH = activityDetails!!.cryptoSymbol == "ETH"
                if (isETH) {
                    transactionHash = paymentGateway.claimEthAmount(
                        activityDetails!!.from.toBigInteger(),
                        activityDetails!!.to.toBigInteger()
                    ).sendAsync().get().transactionHash
                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("ClaimMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                } else {
                    val marketData =
                        Constants.getMarketFromCurrencySymbol(activityDetails!!.cryptoSymbol)
                    transactionHash = paymentGateway.claimErc20Token(
                        activityDetails!!.from.toBigInteger(),
                        activityDetails!!.to.toBigInteger(),
                        marketData!!.underlyingAddress
                    ).sendAsync().get().transactionHash
                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("ClaimMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e.message!!.contains("Insufficient funds")) {
                    this.errorMsg = "You have insufficient funds..."
                }
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                try {
                    updateTransaction(1, activityDetails!!, transactionHash!!, "claimed", false)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.hideProgress(context)
                UserInterface.showTransactionFailedDialog(errorMsg, context!!)
            }
        }
    }


    private inner class CheckIsRegistered(
        activityDetails: ActivityDetails
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var activityDetails: ActivityDetails? = null
        private var isRegister: Boolean = false

        init {
            this.activityDetails = activityDetails
        }

        override fun onPreExecute() {
            super.onPreExecute()
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val epn = EPN.load(
                    Constants.EPNAddress,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                val transactionReceipt = epn.checkEPNAuthorizedStatus(
                    Constants.getAccountAddress(),
                    activityDetails!!.fromEPN.toBigInteger()
                ).sendAsync().get()
                val statusCode = transactionReceipt.component3().toString()
                val isAvailable = transactionReceipt.component1()
                isRegister = false
                if (statusCode == "1" && !isAvailable)
                    isRegister = true
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            UserInterface.hideProgress(context)
            if (result!!) {
                requestMoneyPayDialog(activityDetails!!, isRegister)
            }
        }

    }

    // Transaction confirmation for Request Money
    private fun requestMoneyPayDialog(
        activityDetails: ActivityDetails,
        isRegister: Boolean
    ) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog.setContentView(R.layout.request_mony_pay)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog.window!!.setBackgroundDrawable(d)
            cfmDialog.setCancelable(false)
            val tvAmount = cfmDialog.findViewById<TextView>(R.id.usd_value)
            val tvSendTo = cfmDialog.findViewById<TextView>(R.id.tv_send_to)
            val brandLogo = cfmDialog.findViewById<ImageView>(R.id.brand_logo)
            val tvGasFee = cfmDialog.findViewById<TextView>(R.id.tv_gas_fee)
            val tvCurrency = cfmDialog.findViewById<TextView>(R.id.currency_value)
            val tvNote = cfmDialog.findViewById<TextView>(R.id.tv_note)
            val errorMessage = cfmDialog.findViewById<TextView>(R.id.error_message)
            val btnPay = cfmDialog.findViewById<Button>(R.id.btn_pay)
            val btnDecline = cfmDialog.findViewById<Button>(R.id.btn_decline)
            val ivClose = cfmDialog.findViewById<ImageView>(R.id.iv_close)
            val warningLay = cfmDialog.findViewById<LinearLayout>(R.id.warning_lay)
            val noteView = cfmDialog.findViewById<LinearLayout>(R.id.note_view)

            try {
                if (activityDetails.notes.isNullOrEmpty())
                    noteView.visibility = View.GONE
                else
                    tvNote?.text = activityDetails.notes
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val gasFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toDouble()
                tvGasFee.text = "GAS fee: " + UserInterface.round(gasFee) + " ETH"
                tvSendTo.text = "+" + activityDetails.fromEPN
                val perUSD = Constants.getUSDValueFromCurrency(activityDetails.cryptoSymbol)
                val coinValue = activityDetails.amount!!
                val usdValue = coinValue * perUSD
                brandLogo.setImageDrawable(
                    context!!.getDrawable(
                        UserInterface.getCoinIcon(
                            activityDetails.cryptoSymbol
                        )
                    )
                )

                tvAmount.text =
                    "$" + UserInterface.roundTwo(usdValue)
                tvCurrency.text =
                    UserInterface.round(coinValue) + " " + activityDetails.cryptoSymbol
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (!isRegister) {
                    warningLay.visibility = View.VISIBLE
                    setTextViewHTML(
                        errorMessage,
                        "This is not an EPN, sender must register on PESA & enable EPN. <a href=learn_more>Learn more.</a>"
                    )
                } else
                    warningLay.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btnPay?.setOnClickListener {
                cfmDialog?.dismiss()
                SendMoneyTask(
                    activityDetails
                ).execute()
            }

            btnDecline.setOnClickListener {
                cfmDialog?.dismiss()
                updateDeclineTransaction(1, activityDetails)
            }

            ivClose?.setOnClickListener {
                cfmDialog?.dismiss()
            }
            cfmDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //SendMoney task
    private inner class SendMoneyTask(
        activityDetails: ActivityDetails
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var activityDetails: ActivityDetails? = null
        private var transactionHash: String? = ""
        private var transactionFee: String? = "0.00"
        private var usdValue: Double? = 0.00
        private var errorMsg = "Your transaction has been failed..."

        init {
            this.activityDetails = activityDetails
        }

        override fun onPreExecute() {
            UserInterface.showTransactionProgress(
                "Sending money",
                "Transaction is being proceeded",
                context
            )
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val paymentGateway = PaymentGateway.load(
                    Constants.PAYMENT_Address,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                val isETH = activityDetails!!.cryptoSymbol == "ETH"
                val marketData =
                    Constants.getMarketFromCurrencySymbol(activityDetails!!.cryptoSymbol)
                usdValue = activityDetails!!.amount * marketData!!.underlyingPriceUSD
                if (isETH) {
                    val coinValue = activityDetails!!.amount
                    transactionHash = paymentGateway.sendEthAmount(
                        Convert.toWei(
                            coinValue!!.toBigDecimal(),
                            Convert.Unit.ETHER
                        ).toBigInteger(),
                        Constants.getEPNId().toBigInteger(),
                        activityDetails!!.from.toBigInteger()
                    ).sendAsync().get().transactionHash

                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("SendMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                } else {
                    val coinValue = activityDetails!!.amount
                    transactionHash = paymentGateway.sendErc20Token(
                        Constants.getEPNId().toBigInteger(),
                        activityDetails!!.from.toBigInteger(),
                        marketData!!.underlyingAddress,
                        UserInterface.UnitMultiply(
                            coinValue!!.toBigDecimal(),
                            marketData!!.underlyingDecimals
                        ).toBigInteger()
                    ).sendAsync().get().transactionHash

                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("SendMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                if (e.message!!.contains("Insufficient funds")) {
                    this.errorMsg = "You have insufficient funds..."
                }
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                try {
                    updateTransaction(1, activityDetails!!, transactionHash!!, "approved", true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                val params = JSONObject()
                try {
                    params.put("type", "sent")
                    params.put("status", "success")
                    params.put("txHash", transactionHash)
                    params.put("to", activityDetails!!.from)
                    params.put("toAddress", activityDetails!!.fromEPN)
                    params.put("from", Constants.getEPNId())
                    params.put("fromEPN", Constants.getEPNNumber().replace("+", ""))
                    params.put("isToken", activityDetails!!.isToken)
                    params.put("cryptoSymbol", activityDetails!!.cryptoSymbol)
                    params.put("amount", activityDetails!!.amount)
                    params.put("usdAmount", usdValue)
                    params.put("txnFee", transactionFee)
                    params.put("isSendToEPN", activityDetails!!.isSendToEPN)
                    params.put("isEscrow", false)
                    params.put("notes", "")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                var body = params.toString().toRequestBody(JSON)
                var basicDetailsService = FetchDataBackgroungService()
                basicDetailsService.createTransaction(1, body, context!!)
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.hideProgress(context)
                UserInterface.showTransactionFailedDialog(errorMsg, context!!)
            }
        }
    }

    //update Decline
    fun updateDeclineTransaction(
        retryCount: Int,
        activityDetails: ActivityDetails
    ) {
        UserInterface.showProgress("Loading...", context)
        val params = JSONObject()
        try {
            params.put("id", activityDetails.id)
            params.put("status", "declined")
            params.put("txHash", "")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.updateTransaction(body, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ResponseStatus> {
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    updateDeclineTransaction(retryCountNew, activityDetails)
                } else
                    UserInterface.hideProgress(context)
            }

            override fun onResponse(
                call: Call<ResponseStatus>,
                response: Response<ResponseStatus>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null) {
                            if (it.status) {
                                UserInterface.hideProgress(context)
                                refreshActivities()
                                requestMoneyDeclineSuccessDialog(
                                    "Request money",
                                    "Your have successfully declined this request..."
                                )
                            }
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            updateDeclineTransaction(retryCountNew, activityDetails)
                        } else
                            UserInterface.hideProgress(context)
                    }
                }
            }

        })

    }

    //update transaction
    fun updateTransaction(
        retryCount: Int,
        activityDetails: ActivityDetails,
        txHash: String,
        status: String,
        isSend: Boolean
    ) {
        val params = JSONObject()
        try {
            params.put("id", activityDetails.id)
            params.put("status", status)
            params.put("txHash", txHash)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.updateTransaction(body, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ResponseStatus> {
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    updateTransaction(retryCountNew, activityDetails, txHash, status, isSend)
                } else
                    UserInterface.hideProgress(context)
            }

            override fun onResponse(
                call: Call<ResponseStatus>,
                response: Response<ResponseStatus>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it != null) {
                            if (it.status) {
                                UserInterface.hideProgress(context)
                                refreshActivities()
                                if (isSend)
                                    requestMoneyPaySuccessDialog(
                                        "Request money",
                                        "Your transaction has been successful..."
                                    )
                                else
                                    requestMoneyPaySuccessDialog(
                                        "Claim money",
                                        "Your transaction has been successful..."
                                    )
                            }
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            updateTransaction(
                                retryCountNew,
                                activityDetails,
                                txHash,
                                status,
                                isSend
                            )
                        } else
                            UserInterface.hideProgress(context)
                    }
                }
            }

        })

    }

    private fun refreshActivities() {
        try {
            if (activitiesFragment != null)
                activitiesFragment!!.loadAllActivities(1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Transaction Success for Request Money
    private fun requestMoneyPaySuccessDialog(
        title: String,
        msg: String
    ) {
        try {
            val successDialog = Dialog(activity!!, R.style.CustomDialog)
            successDialog.setContentView(R.layout.success_popup)
            val d = ColorDrawable(Color.TRANSPARENT)
            successDialog.window!!.setBackgroundDrawable(d)
            successDialog.setCancelable(false)
            val tvTitle = successDialog.findViewById<TextView>(R.id.tv_title)
            val tvMsg = successDialog.findViewById<TextView>(R.id.tv_msg)
            val btnDone = successDialog.findViewById<Button>(R.id.btn_done)
            val successLogo = successDialog.findViewById<ImageView>(R.id.success_logo)
            val declineLogo = successDialog.findViewById<ImageView>(R.id.decline_logo)
            tvTitle?.text = title
            tvMsg?.text = msg
            successLogo?.visibility = View.VISIBLE
            declineLogo?.visibility = View.GONE
            btnDone.setOnClickListener { successDialog.dismiss() }
            successDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Decline for Request Money
    private fun requestMoneyDeclineSuccessDialog(
        title: String,
        msg: String
    ) {
        try {
            val declineDialog = Dialog(activity!!, R.style.CustomDialog)
            declineDialog.setContentView(R.layout.success_popup)
            val d = ColorDrawable(Color.TRANSPARENT)
            declineDialog.window!!.setBackgroundDrawable(d)
            declineDialog.setCancelable(false)
            val tvTitle = declineDialog.findViewById<TextView>(R.id.tv_title)
            val tvMsg = declineDialog.findViewById<TextView>(R.id.tv_msg)
            val btnDone = declineDialog.findViewById<Button>(R.id.btn_done)
            val successLogo = declineDialog.findViewById<ImageView>(R.id.success_logo)
            val declineLogo = declineDialog.findViewById<ImageView>(R.id.decline_logo)
            tvTitle?.text = title
            tvMsg?.text = msg
            successLogo?.visibility = View.GONE
            declineLogo?.visibility = View.VISIBLE
            btnDone.setOnClickListener { declineDialog.dismiss() }
            declineDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

}