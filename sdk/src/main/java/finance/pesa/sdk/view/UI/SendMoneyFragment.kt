package finance.pesa.sdk.view.UI

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.text.Editable
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.EPN
import finance.pesa.sdk.generator.PaymentGateway
import finance.pesa.sdk.utils.*
import finance.pesa.sdk.utils.Constants.PAYMENT_Address
import finance.pesa.sdk.view.adapter.CurrencyAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.abi.datatypes.Bool
import org.web3j.contracts.eip20.generated.ERC20
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.tx.gas.DefaultGasProvider
import org.web3j.utils.Convert
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

class SendMoneyFragment : Fragment() {
    private var closeBtn: Button? = null
    private var btnSend: Button? = null
    private var addressField: LinearLayout? = null
    private var exchangeTypeLay: LinearLayout? = null
    private var enterLay: RelativeLayout? = null
    private var pickLay: RelativeLayout? = null
    private var ivContact: ImageButton? = null
    private var ivScanQR: ImageButton? = null
    private var ivDelete: ImageView? = null
    private var sendBalance: EditText? = null
    private var epnNumber: EditText? = null
    private var tvNote: EditText? = null
    private var dollarLabel: TextView? = null
    private var ivCurrencyValue: TextView? = null
    private var currencyUSD: TextView? = null
    private var tokenAvailable: TextView? = null
    private var availability: TextView? = null
    private var currencyName: TextView? = null
    private var ivCurrency: ImageView? = null
    private var epnNumberPick: TextView? = null
    private var sendValue: String? = null
    private var isUSDFocus: Boolean? = true
    private var fromType: RelativeLayout? = null
    private var totalAvailableBalanceUSD: Double? = 0.00
    private var totalAvailableBalanceValue: Double? = 0.00
    private var selectedItem: InvestData? = null
    private var isETHSelected: Boolean? = false
    private var lastCurrencySelectedPos: Int? = 0
    private var currencyLay: LinearLayout? = null
    private var usdLay: LinearLayout? = null
    private var ivUSDValue: TextView? = null
    private var currencyLabel: TextView? = null
    private var sendCurrencyBalance: EditText? = null
    private var enterBalanceLay: RelativeLayout? = null
    private var walletAddress: String? = ""

    companion object {

        fun newInstance(sendValue: String, walletAddress: String): SendMoneyFragment {
            val sendMoneyFragment = SendMoneyFragment()
            sendMoneyFragment.sendValue = sendValue
            sendMoneyFragment.walletAddress = walletAddress
            return sendMoneyFragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserInterface.changeStatusBar(activity!!, R.color.app_green)
        UserInterface.changeNavigationBar(activity!!, R.color.app_green)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.send, null)
        currencyLay = view.findViewById(R.id.currency_lay)
        usdLay = view.findViewById(R.id.usd_lay)
        enterBalanceLay = view.findViewById(R.id.enter_bal_lay)
        currencyLabel = view.findViewById(R.id.currency_label)
        sendCurrencyBalance = view.findViewById(R.id.send_currency_balance)
        ivUSDValue = view.findViewById(R.id.iv_usd_value)
        currencyUSD = view.findViewById(R.id.currency_usd)
        btnSend = view.findViewById(R.id.btn_send)
        closeBtn = view.findViewById(R.id.btn_cancel)
        dollarLabel = view.findViewById(R.id.dollar_label)
        ivCurrencyValue = view.findViewById(R.id.iv_currency_value)
        sendBalance = view.findViewById(R.id.send_balance)
        exchangeTypeLay = view.findViewById(R.id.exchange_type_lay)
        currencyName = view.findViewById(R.id.currency_name)
        ivCurrency = view.findViewById(R.id.iv_currency)
        tokenAvailable = view.findViewById(R.id.token_available)
        availability = view.findViewById(R.id.availability)
        fromType = view.findViewById(R.id.from_type)
        ivContact = view.findViewById(R.id.iv_contact)
        ivScanQR = view.findViewById(R.id.iv_scan_qr)
        epnNumber = view.findViewById(R.id.epn_number)
        enterLay = view.findViewById(R.id.enter_lay)
        pickLay = view.findViewById(R.id.pick_lay)
        epnNumberPick = view.findViewById(R.id.epn_number_pick)
        addressField = view.findViewById(R.id.address_field)
        ivDelete = view.findViewById(R.id.iv_close)
        tvNote = view.findViewById(R.id.tv_note)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        closeBtn!!.setOnClickListener {
            activity!!.onBackPressed()
        }
        ivDelete?.setOnClickListener {
            try {
                epnNumberPick?.text = ""
                epnNumberPick?.tag = ""
                pickLay?.visibility = View.GONE
                enterLay?.visibility = View.VISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        sendBalance?.setOnTouchListener(View.OnTouchListener { v, event ->
            isUSDFocus = true
            false
        })
        sendCurrencyBalance?.setOnTouchListener(View.OnTouchListener { v, event ->
            isUSDFocus = false
            false
        })
        sendBalance?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                onTextChangedBalance()
            }
        })
        sendCurrencyBalance?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (!isUSDFocus!!) {
                    if (s.isEmpty()) {
                        dollarLabel?.setTextColor(resources.getColor(R.color.txt_light, null))
                        ivCurrencyValue?.text = ""
                        if (selectedItem != null) {
                            ivCurrencyValue?.hint = "0.00 " + selectedItem!!.underlyingSymbol
                        }
                        ivUSDValue?.text = ""
                        sendBalance?.setText("")
                        currencyLabel?.text = ""
                        ivUSDValue?.hint = "$0.00"
                    } else {
                        try {
                            if (selectedItem != null) {
                                val currencyValues =
                                    sendCurrencyBalance?.text.toString().trim().toDouble()
                                val transferAmount =
                                    currencyValues * selectedItem!!.underlyingPriceUSD
                                currencyLabel?.text = selectedItem!!.underlyingSymbol
                                if (transferAmount > 0) {
                                    ivCurrencyValue?.text =
                                        UserInterface.round(currencyValues) + " " + selectedItem!!.underlyingSymbol
                                    sendBalance?.setText(UserInterface.round(transferAmount))
                                } else {
                                    ivCurrencyValue?.text = ""
                                    sendBalance?.setText("")
                                }
                                if (transferAmount == 0.0) {
                                    ivUSDValue?.text = ""
                                    dollarLabel?.setTextColor(
                                        resources.getColor(
                                            R.color.txt_light,
                                            null
                                        )
                                    )
                                } else {
                                    dollarLabel?.setTextColor(
                                        resources.getColor(
                                            R.color.app_green,
                                            null
                                        )
                                    )
                                    ivUSDValue?.text = "$ " + UserInterface.round(transferAmount)
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    btnSend?.isEnabled = false
                    btnSend?.isClickable = false
                    try {
                        var sendValue = sendBalance?.text.toString().trim()
                        if (sendValue == "")
                            sendValue = "0"
                        val totalSendBalanceUSD = sendValue.toDouble()
                        if (totalAvailableBalanceUSD!! > totalSendBalanceUSD!! && totalSendBalanceUSD!! > 0) {
                            btnSend?.isEnabled = true
                            btnSend?.isClickable = true
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        })
        Constants.setRefreshWalletListener(refreshWalletListener)
        sendBalance?.setText(sendValue)
        if (walletAddress!!.isNotEmpty()) {
            epnNumber?.setText("")
            epnNumberPick?.tag = walletAddress
            pickLay?.visibility = View.VISIBLE
            enterLay?.visibility = View.GONE
            epnNumber?.tag = ""
            UserInterface.showWalletAddress(epnNumberPick!!, walletAddress!!)
        }
        onUIUpdateCurrency()
        ivScanQR?.setOnClickListener { onScanWalletAddress() }
        ivContact?.setOnClickListener { onSearchEPNNumber() }
        btnSend?.setOnClickListener { sendMoney() }
        fromType?.setOnClickListener { chooseCurrencyDialog() }
        exchangeTypeLay?.setOnClickListener {
            try {
                UserInterface.hideKeyboard(context!!, view)
                if (usdLay?.isVisible!!) {
                    usdLay?.visibility = View.GONE
                    currencyLay?.visibility = View.VISIBLE
                } else {
                    usdLay?.visibility = View.VISIBLE
                    currencyLay?.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return view
    }

    private fun onTextChangedBalance() {
        try {
            if (isUSDFocus!!) {
                if (sendBalance?.text.toString().isEmpty()) {
                    dollarLabel?.setTextColor(resources.getColor(R.color.txt_light, null))
                    ivCurrencyValue?.text = ""
                    if (selectedItem != null) {
                        ivCurrencyValue?.hint = "0.00 " + selectedItem!!.underlyingSymbol
                        // sendCurrencyBalance?.hint = "0.00" + selectedItem!!.underlyingSymbol
                    }
                    ivUSDValue?.text = ""
                    sendCurrencyBalance?.setText("")
                    currencyLabel?.text = ""
                    ivUSDValue?.hint = "$0.00"
                } else {
                    dollarLabel?.setTextColor(
                        resources.getColor(
                            R.color.app_green,
                            null
                        )
                    )
                    try {
                        if (selectedItem != null) {
                            val transferAmount = sendBalance?.text.toString().trim().toDouble()
                            val currencyValues =
                                transferAmount / selectedItem!!.underlyingPriceUSD
                            if (currencyValues > 0) {
                                ivCurrencyValue?.text =
                                    UserInterface.round(currencyValues) + " " + selectedItem!!.underlyingSymbol
                                sendCurrencyBalance?.setText(UserInterface.round(currencyValues))
                                currencyLabel?.text = selectedItem!!.underlyingSymbol
                            } else {
                                ivCurrencyValue?.text = ""
                                /*sendCurrencyBalance?.hint =
                                    "0.00" + selectedItem!!.underlyingSymbol*/
                            }
                            if (transferAmount == 0.0)
                                ivUSDValue?.text = ""
                            else
                                ivUSDValue?.text = "$ " + UserInterface.round(transferAmount)

                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                btnSend?.isEnabled = false
                btnSend?.isClickable = false
                try {
                    var sendValue = sendBalance?.text.toString().trim()
                    if (sendValue == "")
                        sendValue = "0"
                    val totalSendBalanceUSD = sendValue.toDouble()
                    if (totalAvailableBalanceUSD!! > totalSendBalanceUSD!! && totalSendBalanceUSD!! > 0) {
                        btnSend?.isEnabled = true
                        btnSend?.isClickable = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //refresh wallet balance
    private val refreshWalletListener = object : RefreshWalletListener {
        override fun onRefreshWallet() {
        }

        override fun onRefreshInvest() {
        }

        override fun onMarketLoaded() {
            onUIUpdateCurrency()
        }
    }

    private fun chooseCurrencyDialog() {
        try {
            val currencyDialog = Dialog(activity!!, R.style.CustomDialog)
            currencyDialog.setContentView(R.layout.spinner_dialog)
            val d = ColorDrawable(Color.TRANSPARENT)
            currencyDialog.window!!.setBackgroundDrawable(d)
            currencyDialog.setCancelable(false)
            val currencyList = currencyDialog.findViewById<RecyclerView>(R.id.currency_list)
            val btnDone = currencyDialog.findViewById<Button>(R.id.btn_done)
            val btnClose = currencyDialog.findViewById<ImageView>(R.id.iv_close)
            val allMarket = Constants.getAllMarkets()
            val currencyAdapter = CurrencyAdapter(
                context!!,
                allMarket!!,
                lastCurrencySelectedPos!!
            )
            currencyList.layoutManager = LinearLayoutManager(activity)
            currencyList.adapter = currencyAdapter

            btnClose?.setOnClickListener {
                currencyDialog?.dismiss()
            }
            btnDone?.setOnClickListener {
                lastCurrencySelectedPos = currencyAdapter?.getLastSelectedPos()
                if (selectedItem == null) {
                    selectedItem = currencyAdapter?.getMarket()
                    updateUIData()
                } else if (selectedItem!!.underlyingSymbol != currencyAdapter?.getMarket().underlyingSymbol) {
                    selectedItem = currencyAdapter?.getMarket()
                    updateUIData()
                }
                currencyDialog?.dismiss()
            }
            currencyDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateUIData() {
        try {
            var sendValue = sendBalance?.text.toString().trim()
            if (sendValue == "")
                sendValue = "0"
            currencyName?.text = selectedItem!!.underlyingName
            ivCurrency?.setImageDrawable(
                context!!.getDrawable(
                    UserInterface.getCoinIcon(
                        selectedItem!!.underlyingSymbol
                    )
                )
            )
            currencyUSD?.text =
                "Price: 1" + selectedItem!!.underlyingSymbol + " = $" + UserInterface.roundTwo(
                    selectedItem!!.underlyingPriceUSD
                )
            isUSDFocus = true
            onTextChangedBalance()
            CalculateWalletBalance(
                selectedItem!!, sendValue.toDouble()
            ).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sendMoney() {
        try {
            var isEPN = false
            var address: String? = null
            var enteredAddress = ""
            var displaySendTo = ""
            if (enterLay?.visibility == View.VISIBLE) {
                enteredAddress = epnNumber?.text.toString().trim()
                displaySendTo = epnNumber?.text.toString().trim()
            } else {
                enteredAddress = epnNumberPick?.tag.toString().trim()
                displaySendTo = epnNumberPick?.text.toString().trim()
            }
            if (enteredAddress != "") {
                if (enteredAddress.startsWith("0x")) {
                    if (Keys.toChecksumAddress(enteredAddress) == enteredAddress) {
                        address = enteredAddress
                    }
                } else {
                    if (UserInterface.getAllowedNumber(enteredAddress)!!) {
                        isEPN = true
                        address = enteredAddress.replace("+", "")
                    }
                }
            }

            if (address != null) {
                var transferAmount = 0.0
                try {
                    transferAmount = sendBalance?.text.toString().trim().toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (transferAmount < 5) {
                    UserInterface.errorShow(enterBalanceLay, context)
                    return
                }
                btnSend?.isEnabled = false
                btnSend?.isClickable = false
                CheckIsRegistered(
                    selectedItem!!,
                    address,
                    displaySendTo,
                    transferAmount,
                    isETHSelected!!,
                    isEPN
                ).execute()
            } else {
                UserInterface.errorShow(addressField, context)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class CheckIsRegistered(
        marketData: InvestData,
        address: String,
        displaySendTo: String,
        usdValue: Double,
        isETH: Boolean,
        isEPN: Boolean
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var marketData: InvestData? = null
        private var address: String? = null
        private var displaySendTo: String? = null
        private var usdValue: Double? = null
        private var isEPN: Boolean? = null
        private var isETH: Boolean? = null
        private var isRegister: Boolean = true
        private var destinationId: BigInteger? = "0".toBigInteger()
        private var isAllowance: Boolean = true

        init {
            this.marketData = marketData
            this.address = address
            this.displaySendTo = displaySendTo
            this.usdValue = usdValue
            this.isEPN = isEPN
            this.isETH = isETH
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                if (isEPN!!) {
                    val epn = EPN.load(
                        Constants.EPNAddress,
                        PesaApplication.getWeb3j(),
                        Constants.getAccountCredential(),
                        CustomGasProvider()
                    )
                    val transactionReceipt = epn.checkEPNAuthorizedStatus(
                        Constants.getAccountAddress(),
                        address!!.toBigInteger()
                    ).sendAsync().get()

                    destinationId = transactionReceipt.component4()
                    val statusCode = transactionReceipt.component3().toString()
                    val isAvailable = transactionReceipt.component1()
                    isRegister = false
                    if (statusCode == "1" && !isAvailable)
                        isRegister = true
                }
                if (!isETH!!) {
                    isAllowance = getIsAllowance(
                        PAYMENT_Address,
                        marketData!!.underlyingAddress,
                        Constants.getAccountCredential()!!
                    )
                }
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            btnSend?.isEnabled = true
            btnSend?.isClickable = true
            if (result!!) {
                if (isAllowance) {
                    transactionSendMoneyDialog(
                        marketData!!,
                        address!!,
                        displaySendTo!!,
                        usdValue!!,
                        isETH!!,
                        isEPN!!,
                        isRegister!!,
                        destinationId!!
                    )
                } else {
                    allowanceDialog(marketData!!)
                }
            }
        }

    }

    // get allowance balance is enable or disable
    private fun getIsAllowance(
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

    // Allowance Send Money
    private fun allowanceDialog(
        marketData: InvestData
    ) {
        try {
            val allowanceDialog = Dialog(activity!!, R.style.CustomDialog)
            allowanceDialog.setContentView(R.layout.approve_token)
            val d = ColorDrawable(Color.TRANSPARENT)
            allowanceDialog.window!!.setBackgroundDrawable(d)
            allowanceDialog.setCancelable(false)
            val approveTitle = allowanceDialog.findViewById<TextView>(R.id.approve_title)
            val tvMsg = allowanceDialog.findViewById<TextView>(R.id.tv_msg)
            val tvGasFee = allowanceDialog.findViewById<TextView>(R.id.tv_gas_fee)
            val ivCurrency = allowanceDialog.findViewById<ImageView>(R.id.iv_currency)
            val btnApprove = allowanceDialog.findViewById<Button>(R.id.btn_approve)
            val ivClose = allowanceDialog.findViewById<ImageView>(R.id.iv_close)
            try {
                approveTitle.text = "Approve " + marketData.underlyingSymbol
                tvMsg.text =
                    "Please approve PESA in " + marketData.underlyingSymbol + " for transactions"
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val gasFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toDouble()
                tvGasFee.text = "GAS fee: " + UserInterface.round(gasFee) + "ETH"
                ivCurrency.setImageDrawable(
                    context!!.getDrawable(
                        UserInterface.getCoinIcon(
                            marketData.underlyingSymbol
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            btnApprove?.setOnClickListener {
                allowanceDialog?.dismiss()
                EnableTask(
                    marketData
                ).execute()
            }

            ivClose?.setOnClickListener {
                allowanceDialog?.dismiss()
            }
            allowanceDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // enable allowance balance task
    private inner class EnableTask(marketData: InvestData) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var marketData: InvestData? = null
        private var errorMsg = "Your transaction has been failed..."

        init {
            this.marketData = marketData
        }

        override fun onPreExecute() {
            UserInterface.showTransactionProgress(
                "Approving " + marketData!!.underlyingSymbol,
                "Transaction is being proceeded",
                context
            )
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val credentials = Constants.getAccountCredential()
                val ercToken =
                    ERC20.load(
                        marketData!!.underlyingAddress,
                        PesaApplication.getWeb3j(),
                        credentials,
                        CustomGasProvider()
                    )
                val transactionReceipt = ercToken.approve(
                    PAYMENT_Address, BigInteger(Constants.allowanceBalanceLimit)
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
                if (e.message!!.contains("Insufficient funds")) {
                    this.errorMsg = "You have insufficient funds..."
                }
            }
            return false
        }


        override fun onPostExecute(result: Boolean?) {
            UserInterface.hideProgress(context)
            if (result!!) {
                sendMoney()
            } else {
                UserInterface.showTransactionFailedDialog(errorMsg, context!!)
            }
        }
    }


    // Transaction confirmation for Send Money
    private fun transactionSendMoneyDialog(
        marketData: InvestData,
        address: String,
        displaySendTo: String,
        usdValue: Double,
        isETH: Boolean,
        isEPN: Boolean,
        isRegister: Boolean,
        destinationId: BigInteger
    ) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog.setContentView(R.layout.send_confirmation)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog.window!!.setBackgroundDrawable(d)
            cfmDialog.setCancelable(false)
            val currencyValue = cfmDialog.findViewById<TextView>(R.id.currency_value)
            val ivUSDText = cfmDialog.findViewById<TextView>(R.id.usd_value)
            val tvSendTo = cfmDialog.findViewById<TextView>(R.id.tv_send_to)
            val brandLogo = cfmDialog.findViewById<ImageView>(R.id.brand_logo)
            val tvGasFee = cfmDialog.findViewById<TextView>(R.id.tv_gas_fee)
            val errorMessage = cfmDialog.findViewById<TextView>(R.id.error_message)
            val tvNoteText = cfmDialog.findViewById<TextView>(R.id.tv_note)
            val btnConfirm = cfmDialog.findViewById<Button>(R.id.btn_confirm)
            val btnClose = cfmDialog.findViewById<Button>(R.id.btn_cancel)
            val warningLay = cfmDialog.findViewById<LinearLayout>(R.id.warning_lay)
            val noteView = cfmDialog.findViewById<LinearLayout>(R.id.note_view)

            try {
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val gasFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toDouble()
                tvGasFee.text = "GAS fee: " + UserInterface.round(gasFee) + " ETH"
                tvSendTo.text = displaySendTo
                ivUSDText.text = "$" + UserInterface.roundTwo(usdValue)
                val perUSD = marketData.underlyingPriceUSD
                val coinValue = usdValue!! / perUSD
                currencyValue.text =
                    UserInterface.round(coinValue) + " " + marketData.underlyingSymbol
                brandLogo.setImageDrawable(
                    context!!.getDrawable(
                        UserInterface.getCoinIcon(
                            marketData.underlyingSymbol
                        )
                    )
                )
                if (tvNote?.text.toString().trim().isNullOrEmpty())
                    noteView.visibility = View.GONE
                else
                    tvNoteText.text = tvNote?.text
            } catch (e: Exception) {
                e.printStackTrace()
            }

            try {
                if (!isRegister) {
                    warningLay.visibility = View.VISIBLE
                    setTextViewHTML(
                        errorMessage,
                        "This number is not registered, the amount will be in escrow for 7 days. You can reclaim the amount from escrow if it is not claimed by the receiver within 7 days. <a href=learn_more>Learn more.</a>"
                    )
                } else
                    warningLay.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }

            btnConfirm?.setOnClickListener {
                cfmDialog?.dismiss()
                SendMoneyTask(
                    marketData,
                    address,
                    displaySendTo,
                    usdValue,
                    isETH,
                    isEPN,
                    isRegister,
                    destinationId
                ).execute()
            }

            btnClose?.setOnClickListener {
                cfmDialog?.dismiss()
            }
            cfmDialog?.show()
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


    //SendMoney task
    private inner class SendMoneyTask(
        marketData: InvestData,
        address: String,
        displaySendTo: String,
        usdValue: Double,
        isETH: Boolean,
        isEPN: Boolean,
        isRegister: Boolean,
        destinationId: BigInteger
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var marketData: InvestData? = null
        private var address: String? = null
        private var displaySendTo: String? = null
        private var usdValue: Double? = null
        private var transactionHash: String? = ""
        private var coinValue: Double? = 0.00
        private var transactionFee: String? = "0.00"
        private var isEPN: Boolean? = null
        private var isETH: Boolean? = null
        private var isRegister: Boolean? = null
        private var destinationId: BigInteger? = null
        private var errorMsg = "Your transaction has been failed..."

        init {
            this.marketData = marketData
            this.address = address
            this.displaySendTo = displaySendTo
            this.usdValue = usdValue
            this.isEPN = isEPN
            this.isETH = isETH
            this.isRegister = isRegister
            this.destinationId = destinationId
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
                    PAYMENT_Address,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                if (isETH!!) {
                    if (isEPN!!) {
                        val currencyTOusd = marketData!!.underlyingPriceUSD
                        coinValue = usdValue!! / currencyTOusd
                        transactionHash = paymentGateway.sendEthAmount(
                            Convert.toWei(
                                coinValue!!.toBigDecimal(),
                                Convert.Unit.ETHER
                            ).toBigInteger(),
                            Constants.getEPNId().toBigInteger(),
                            destinationId
                        ).sendAsync().get().transactionHash
                    } else {
                        val currencyTOusd = marketData!!.underlyingPriceUSD
                        coinValue = usdValue!! / currencyTOusd
                        transactionHash = paymentGateway.sendEthAmountToWallet(
                            Convert.toWei(
                                coinValue!!.toBigDecimal(),
                                Convert.Unit.ETHER
                            ).toBigInteger(),
                            Constants.getEPNId().toBigInteger(),
                            address
                        ).sendAsync().get().transactionHash
                    }
                    val transactionReceipt =
                        PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                            .sendAsync().get().transactionReceipt.get()
                    Log.d("SendMoney_Receipt_Hash-->", transactionHash)
                    if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                        return true
                    }
                } else {
                    if (isEPN!!) {
                        val currencyTOusd = marketData!!.underlyingPriceUSD
                        coinValue = usdValue!! / currencyTOusd
                        transactionHash = paymentGateway.sendErc20Token(
                            Constants.getEPNId().toBigInteger(),
                            destinationId,
                            marketData!!.underlyingAddress,
                            UserInterface.UnitMultiply(
                                coinValue!!.toBigDecimal(),
                                marketData!!.underlyingDecimals
                            ).toBigInteger()
                        ).sendAsync().get().transactionHash
                    } else {
                        val currencyTOusd = marketData!!.underlyingPriceUSD
                        coinValue = usdValue!! / currencyTOusd
                        transactionHash = paymentGateway.sendErc20TokenToWallet(
                            Constants.getEPNId().toBigInteger(),
                            address,
                            marketData!!.underlyingAddress,
                            UserInterface.UnitMultiply(
                                coinValue!!.toBigDecimal(),
                                marketData!!.underlyingDecimals
                            ).toBigInteger()
                        ).sendAsync().get().transactionHash
                    }
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
            UserInterface.hideProgress(context)
            if (result!!) {
                try {
                    try {
                        val gasPriceGwei =
                            Convert.fromWei(
                                CustomGasProvider.GAS_PRICE.toBigDecimal(),
                                Convert.Unit.GWEI
                            )
                                .toBigInteger()
                        val totalFee = Convert.fromWei(
                            (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                            Convert.Unit.GWEI
                        ).toPlainString()
                        this.transactionFee = totalFee
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    val params = JSONObject()
                    try {
                        params.put("type", "sent")
                        params.put("status", "success")
                        params.put("txHash", transactionHash)
                        if (isEPN!!)
                            params.put("to", destinationId.toString())
                        else
                            params.put("to", address)
                        params.put("toAddress", address)
                        params.put("from", Constants.getEPNId())
                        params.put("fromEPN", Constants.getEPNNumber().replace("+", ""))
                        params.put("isToken", !isETH!!)
                        params.put("cryptoSymbol", marketData!!.underlyingSymbol)
                        params.put("amount", coinValue)
                        params.put("usdAmount", usdValue)
                        params.put("txnFee", transactionFee)
                        params.put("isSendToEPN", isEPN!!)
                        if (isEPN!! && !isRegister!!)
                            params.put("isEscrow", true)
                        else
                            params.put("isEscrow", false)
                        params.put("notes", tvNote?.text.toString().trim())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
                    var body = params.toString().toRequestBody(JSON)
                    var basicDetailsService = FetchDataBackgroungService()
                    basicDetailsService.createTransaction(1, body, context!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
                PesaApplication.getChildFragmentManager()!!.popBackStackImmediate()
                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                    .add(
                        R.id.shareg_main_container,
                        SendMoneySuccessFragment.newInstance(
                            usdValue!!,
                            coinValue!!,
                            transactionFee!!.toDouble(),
                            tvNote?.text.toString().trim(),
                            displaySendTo!!,
                            transactionHash!!,
                            (isEPN!! && !isRegister!!),
                            marketData!!
                        )
                    ).addToBackStack("send_money").commitAllowingStateLoss()
            } else {
                UserInterface.showTransactionFailedDialog(errorMsg, context!!)
            }
        }
    }


    private fun onScanWalletAddress() {
        try {
            Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            try {
                                val intent = Intent(activity, QRCodeScannerActivity::class.java)
                                startActivityForResult(intent, Constants.QR_CODE_REQUEST_CODE)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onSearchEPNNumber() {
        try {
            Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.READ_CONTACTS)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            try {
                                val intent = Intent(
                                    Intent.ACTION_PICK,
                                    ContactsContract.Contacts.CONTENT_URI
                                )
                                startActivityForResult(intent, Constants.CONTACT_REQUEST_CODE)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showSettingsDialog() {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(
            getString(android.R.string.cancel)
        ) { dialog, which -> dialog.cancel() }
        builder.show()

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 145)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            if (requestCode == Constants.QR_CODE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                val walletAddress = data?.getStringExtra(Constants.QR_CODE_RESULT)
                if (walletAddress != null) {
                    try {
                        val isRight = Keys.toChecksumAddress(walletAddress) == walletAddress
                        if (isRight) {
                            epnNumber?.setText("")
                            epnNumberPick?.tag = walletAddress
                            pickLay?.visibility = View.VISIBLE
                            enterLay?.visibility = View.GONE
                            epnNumber?.tag = ""
                            UserInterface.showWalletAddress(epnNumberPick!!, walletAddress)
                        } else {
                            UserInterface.showToast(context, "Invalid Wallet Address")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else if (requestCode == Constants.CONTACT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data!!.data
                val cursor1: Cursor?
                val cursor2: Cursor?
                val tempNameHolder: String
                var tempNumberHolder: String
                val tempContactID: String
                var idResult = ""
                val idResultHolder: Int

                cursor1 = context!!.contentResolver.query(uri!!, null, null, null, null)

                if (cursor1!!.moveToFirst()) {

                    tempNameHolder =
                        cursor1!!.getString(cursor1!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    tempContactID =
                        cursor1!!.getString(cursor1!!.getColumnIndex(ContactsContract.Contacts._ID))

                    idResult =
                        cursor1!!.getString(cursor1!!.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                    idResultHolder = Integer.valueOf(idResult)

                    if (idResultHolder == 1) {

                        cursor2 = context!!.contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + tempContactID,
                            null,
                            null
                        )
                        var allDatas: Array<String> = arrayOf()
                        var multipleContacts: HashMap<String, String> = hashMapOf()
                        while (cursor2!!.moveToNext()) {

                            tempNumberHolder =
                                cursor2!!.getString(cursor2!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            multipleContacts.put(tempNumberHolder, tempNameHolder)

                        }
                        multipleContacts.forEach { (key, value) -> allDatas += (key) }
                        if (allDatas.size == 1) {
                            epnNumber?.setText("")
                            epnNumberPick?.tag = allDatas[0]
                            pickLay?.visibility = View.VISIBLE
                            enterLay?.visibility = View.GONE
                            epnNumber?.tag = ""
                            epnNumberPick?.text = allDatas[0]
                        } else {
                            onMultiContactNumber(tempNameHolder, allDatas)
                        }
                    }

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onMultiContactNumber(name: String, arrayAdapter: Array<String>) {
        try {
            // setup the alert builder
            val builder = AlertDialog.Builder(context!!)
            builder.setTitle(name)
            // create and show the alert dialog
            builder.setItems(arrayAdapter) { dialog, which ->
                // Get the dialog selected item
                val selected = arrayAdapter[which]
                epnNumber?.setText("")
                epnNumberPick?.tag = selected
                pickLay?.visibility = View.VISIBLE
                enterLay?.visibility = View.GONE
                epnNumber?.tag = ""
                epnNumberPick?.text = selected
                dialog.dismiss()
            }
            builder.setNegativeButton("cancel") { dialogInterface, which ->
                // d.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun onUIUpdateCurrency() {
        try {
            val allMarket = Constants.getAllMarkets()
            if (allMarket.isNullOrEmpty()) {
                fromType?.isClickable = false
                fromType?.isEnabled = false
            } else {
                lastCurrencySelectedPos = 0
                for (i in allMarket.indices) {
                    val market = allMarket[i]
                    if (market.underlyingSymbol == "ETH") {
                        lastCurrencySelectedPos = i
                    }
                }
                selectedItem = allMarket[lastCurrencySelectedPos!!]
                updateUIData()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class CalculateWalletBalance(
        marketData: InvestData, totalSendBalanceUSD: Double
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var totalSendBalanceUSD: Double? = 0.00
        private var totalSendBalanceValue: Double? = 0.00
        private var marketData: InvestData? = null

        init {
            this.marketData = marketData
            this.totalSendBalanceUSD = totalSendBalanceUSD
        }

        override fun onPreExecute() {
            sendBalance?.isEnabled = false
            sendBalance?.isClickable = false
            sendCurrencyBalance?.isEnabled = false
            sendCurrencyBalance?.isClickable = false
            btnSend?.isEnabled = false
            btnSend?.isClickable = false
            fromType?.isEnabled = false
            fromType?.isClickable = false
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                if (marketData!!.underlyingAddress != Constants.ETH_UnderlyingAddress) {
                    isETHSelected = false
                    try {
                        totalAvailableBalanceValue = getTokenWallet(
                            marketData!!.underlyingAddress,
                            Constants.getAccountCredential()!!,
                            marketData!!.underlyingDecimals
                        ).toDouble()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {
                    isETHSelected = true
                    try {
                        totalAvailableBalanceValue = getCoinWallet(
                            Constants.getAccountAddress(),
                            marketData!!.underlyingDecimals
                        ).toDouble()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                totalAvailableBalanceUSD =
                    (totalAvailableBalanceValue!! * marketData!!.underlyingPriceUSD)

                totalSendBalanceValue =
                    (totalSendBalanceUSD!! / marketData!!.underlyingPriceUSD)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

        override fun onPostExecute(result: Boolean?) {
            try {
                availability?.text = marketData!!.underlyingSymbol + " available: "
                tokenAvailable?.text =
                    UserInterface.round(totalAvailableBalanceValue!!) + "= $" + UserInterface.roundTwo(
                        totalAvailableBalanceUSD!!
                    )
                fromType?.isEnabled = true
                fromType?.isClickable = true
                sendBalance?.isEnabled = true
                sendBalance?.isClickable = true
                sendCurrencyBalance?.isEnabled = true
                sendCurrencyBalance?.isClickable = true
                if (totalAvailableBalanceUSD!! > totalSendBalanceUSD!! && totalSendBalanceUSD!! > 0) {
                    btnSend?.isEnabled = true
                    btnSend?.isClickable = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

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

}