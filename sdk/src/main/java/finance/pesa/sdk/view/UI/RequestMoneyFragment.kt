package finance.pesa.sdk.view.UI

import android.Manifest
import android.app.Activity
import android.app.Dialog
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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.Model.ResponseStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.EPN
import finance.pesa.sdk.utils.*
import finance.pesa.sdk.view.adapter.CurrencyAdapter
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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
import java.math.BigInteger
import java.util.*

class RequestMoneyFragment : Fragment() {
    private var closeBtn: Button? = null
    private var btnSend: Button? = null
    private var addressField: LinearLayout? = null
    private var enterLay: RelativeLayout? = null
    private var pickLay: RelativeLayout? = null
    private var ivContact: ImageButton? = null
    private var ivDelete: ImageView? = null
    private var sendBalance: EditText? = null
    private var epnNumber: EditText? = null
    private var tvNote: EditText? = null
    private var dollarLabel: TextView? = null
    private var tokenAvailable: TextView? = null
    private var availability: TextView? = null
    private var currencyName: TextView? = null
    private var ivCurrency: ImageView? = null
    private var epnNumberPick: TextView? = null
    private var sendValue: String? = null
    private var fromType: RelativeLayout? = null
    private var totalAvailableBalanceUSD: Double? = 0.00
    private var totalAvailableBalanceValue: Double? = 0.00
    private var selectedItem: InvestData? = null
    private var isUSDFocus: Boolean? = true
    private var isETHSelected: Boolean? = false
    private var lastCurrencySelectedPos: Int? = 0
    private var currencyLay: LinearLayout? = null
    private var usdLay: LinearLayout? = null
    private var ivUSDValue: TextView? = null
    private var currencyLabel: TextView? = null
    private var sendCurrencyBalance: EditText? = null
    private var exchangeTypeLay: LinearLayout? = null
    private var ivCurrencyValue: TextView? = null
    private var enterBalanceLay: RelativeLayout? = null

    companion object {

        fun newInstance(sendValue: String): RequestMoneyFragment {
            val requestMoneyFragment = RequestMoneyFragment()
            requestMoneyFragment.sendValue = sendValue
            return requestMoneyFragment
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
        val view = View.inflate(context, R.layout.request_money, null)
        currencyLay = view.findViewById(R.id.currency_lay)
        usdLay = view.findViewById(R.id.usd_lay)
        enterBalanceLay = view.findViewById(R.id.enter_bal_lay)
        ivCurrencyValue = view.findViewById(R.id.iv_currency_value)
        currencyLabel = view.findViewById(R.id.currency_label)
        sendCurrencyBalance = view.findViewById(R.id.send_currency_balance)
        exchangeTypeLay = view.findViewById(R.id.exchange_type_lay)
        ivUSDValue = view.findViewById(R.id.iv_usd_value)
        btnSend = view.findViewById(R.id.btn_request)
        closeBtn = view.findViewById(R.id.btn_cancel)
        dollarLabel = view.findViewById(R.id.dollar_label)
        sendBalance = view.findViewById(R.id.send_balance)
        currencyName = view.findViewById(R.id.currency_name)
        ivCurrency = view.findViewById(R.id.iv_currency)
        tokenAvailable = view.findViewById(R.id.token_available)
        availability = view.findViewById(R.id.availability)
        fromType = view.findViewById(R.id.from_type)
        ivContact = view.findViewById(R.id.iv_contact)
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
                        // ivUSDValue?.hint = "$0.00"
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
                }
            }
        })
        sendBalance?.setText(sendValue)
        Constants.setRefreshWalletListener(refreshWalletListener)
        onUIUpdateCurrency()
        ivContact?.setOnClickListener { onSearchEPNNumber() }
        btnSend?.setOnClickListener { requestMoney() }
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
                    // ivUSDValue?.hint = "$0.00"
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
            isUSDFocus = true
            onTextChangedBalance()
            CalculateWalletBalance(
                selectedItem!!, sendValue.toDouble()
            ).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun requestMoney() {
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
                if (UserInterface.getAllowedNumber(enteredAddress)!!) {
                    isEPN = true
                    address = enteredAddress.replace("+", "")
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
                if (isRegister)
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
                else
                    inviteRequestMoneyDialog(
                        marketData!!,
                        address!!,
                        displaySendTo!!,
                        usdValue!!,
                        isETH!!,
                        isEPN!!,
                        isRegister!!,
                        destinationId!!
                    )
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
            cfmDialog.setContentView(R.layout.request_confirmation)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog.window!!.setBackgroundDrawable(d)
            cfmDialog.setCancelable(false)
            val tvAmount = cfmDialog.findViewById<TextView>(R.id.usd_value)
            val tvSendTo = cfmDialog.findViewById<TextView>(R.id.tv_send_to)
            val brandLogo = cfmDialog.findViewById<ImageView>(R.id.brand_logo)
            val tvCurrency = cfmDialog.findViewById<TextView>(R.id.currency_value)
            val tvNoteText = cfmDialog.findViewById<TextView>(R.id.tv_note)
            val btnConfirm = cfmDialog.findViewById<Button>(R.id.btn_confirm)
            val btnClose = cfmDialog.findViewById<Button>(R.id.btn_cancel)
            val noteView = cfmDialog.findViewById<LinearLayout>(R.id.note_view)

            tvSendTo.text = displaySendTo
            tvAmount.text = "$" + UserInterface.roundTwo(usdValue)
            val perUSD = marketData.underlyingPriceUSD
            val coinValue = usdValue!! / perUSD
            if (tvNote?.text.toString().trim().isNullOrEmpty())
                noteView.visibility = View.GONE
            else
                tvNoteText.text = tvNote?.text
            tvCurrency.text = UserInterface.round(coinValue) + " " + marketData.underlyingSymbol
            brandLogo.setImageDrawable(
                context!!.getDrawable(
                    UserInterface.getCoinIcon(
                        marketData.underlyingSymbol
                    )
                )
            )

            btnConfirm?.setOnClickListener {
                cfmDialog?.dismiss()

                createTransaction(
                    1, marketData!!,
                    address!!,
                    displaySendTo!!,
                    usdValue!!,
                    isETH!!,
                    isEPN!!,
                    isRegister!!,
                    destinationId!!,
                    coinValue
                )
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

    //create transaction
    fun createTransaction(
        retryCount: Int,
        marketData: InvestData,
        address: String,
        displaySendTo: String,
        usdValue: Double,
        isETH: Boolean,
        isEPN: Boolean,
        isRegister: Boolean,
        destinationId: BigInteger,
        coinValue: Double
    ) {
        UserInterface.showTransactionProgress(
            "Sending request",
            "We’re sending request, please wait...",
            context
        )
        val params = JSONObject()
        try {
            params.put("type", "request")
            params.put("status", "success")
            params.put("txHash", "")
            if (isEPN!!)
                params.put("to", destinationId.toString())
            else
                params.put("to", address)
            params.put("fromEPN", Constants.getEPNNumber().replace("+", ""))
            params.put("toAddress", address)
            params.put("from", Constants.getEPNId())
            params.put("isToken", !isETH!!)
            params.put("cryptoSymbol", marketData!!.underlyingSymbol)
            params.put("amount", coinValue)
            params.put("usdAmount", usdValue)
            params.put("txnFee", "")
            params.put("isSendToEPN", isEPN!!)
            params.put("isEscrow", !isRegister!!)
            params.put("notes", tvNote?.text.toString().trim())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.createTransaction(body, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ResponseStatus> {
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                if (retryCount < 3) {
                    val retryCountNew = retryCount + 1
                    createTransaction(
                        retryCountNew, marketData!!,
                        address!!,
                        displaySendTo!!,
                        usdValue!!,
                        isETH!!,
                        isEPN!!,
                        isRegister!!,
                        destinationId!!,
                        coinValue
                    )
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
                                try {
                                    PesaApplication.getChildFragmentManager()!!.popBackStackImmediate()
                                    PesaApplication.getChildFragmentManager()!!.beginTransaction()
                                        .add(
                                            R.id.shareg_main_container,
                                            RequestMoneySuccessFragment.newInstance(
                                                usdValue!!,
                                                coinValue!!,
                                                tvNote?.text.toString().trim(),
                                                displaySendTo!!,
                                                marketData!!
                                            )
                                        ).addToBackStack("request_money").commitAllowingStateLoss()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (retryCount < 3) {
                            val retryCountNew = retryCount + 1
                            createTransaction(
                                retryCountNew, marketData!!,
                                address!!,
                                displaySendTo!!,
                                usdValue!!,
                                isETH!!,
                                isEPN!!,
                                isRegister!!,
                                destinationId!!,
                                coinValue
                            )
                        } else
                            UserInterface.hideProgress(context)
                    }
                }
            }

        })

    }

    // Invite friend for request Money
    private fun inviteRequestMoneyDialog(
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
            cfmDialog.setContentView(R.layout.request_money_invite)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog.window!!.setBackgroundDrawable(d)
            cfmDialog.setCancelable(false)
            val btnInvite = cfmDialog.findViewById<Button>(R.id.btn_invite)
            btnInvite?.setOnClickListener {
                cfmDialog?.dismiss()
            }

            cfmDialog?.show()
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
                            showWalletAddress(walletAddress)
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

    private fun showWalletAddress(qrCode: String) {
        try {
            var walAddress = qrCode
            try {
                val startValue = qrCode.substring(0, 8)
                val endValue = qrCode.substring(qrCode.length - 6, qrCode.length)
                walAddress = startValue + "..." + endValue
            } catch (e: Exception) {
                e.printStackTrace()
            }
            epnNumberPick?.text = walAddress
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
                btnSend?.isEnabled = true
                btnSend?.isClickable = true
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