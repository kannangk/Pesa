package finance.pesa.sdk.view.UI

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.text.Editable
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.ResponseStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.EPN
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.EPNAddress
import finance.pesa.sdk.utils.CustomGasProvider
import finance.pesa.sdk.utils.KeyboardHelper
import finance.pesa.sdk.utils.UserInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigDecimal
import java.math.BigInteger

class EPNAuthFragmet : Fragment() {
    private var ivBack: ImageView? = null
    private var btnSendAuth: Button? = null
    private var sendOTP: Button? = null
    private var phoneNumber: EditText? = null
    private var ccp: CountryCodePicker? = null
    private var errorMessage: TextView? = null
    private var authItemId: String? = ""
    private var transactionHash: String? = ""
    private var ivMinus: ImageButton? = null
    private var ivPlus: ImageButton? = null
    private var epnDuration: TextView? = null
    private var usdValue: TextView? = null
    private var ethValue: TextView? = null
    private var ethAvailable: TextView? = null
    private var tvGasFee: TextView? = null
    private var ethBalance: Double? = 0.0
    private var ethDollarValue: Double? = 0.0
    private var payableFee: Double? = 0.0
    private var payableDuration: BigInteger? = null


    companion object {
        fun newInstance(): EPNAuthFragmet {
            return EPNAuthFragmet()
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
        val view = inflater.inflate(R.layout.register_epn, container, false)
        ivBack = view?.findViewById(R.id.iv_back)
        btnSendAuth = view?.findViewById(R.id.check_auth)
        sendOTP = view?.findViewById(R.id.send_otp)
        phoneNumber = view?.findViewById(R.id.phone_number)
        errorMessage = view?.findViewById(R.id.error_message)
        ivMinus = view?.findViewById(R.id.iv_minus)
        ivPlus = view?.findViewById(R.id.iv_plus)
        epnDuration = view?.findViewById(R.id.epn_duration)
        usdValue = view?.findViewById(R.id.usd_value)
        ethValue = view?.findViewById(R.id.eth_value)
        ethAvailable = view?.findViewById(R.id.eth_available)
        tvGasFee = view?.findViewById(R.id.tv_gas_fee)
        ccp = view?.findViewById(R.id.ccp)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        epnDuration?.tag = 1
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        updateAvailableETH(Constants.getAccountAddress())
        sendOTP?.visibility = View.GONE
        btnSendAuth?.visibility = View.VISIBLE
        ivBack?.setOnClickListener { activity?.onBackPressed() }
        ivPlus?.setOnClickListener {
            durationUpdate(true)
        }
        ivMinus?.setOnClickListener {
            durationUpdate(false)
        }
        phoneNumber?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                isError(false, "")
                val phoneNumberText = phoneNumber!!.text.toString()
                btnSendAuth?.visibility = View.VISIBLE
                btnSendAuth?.isEnabled = false
                btnSendAuth?.isClickable = false
                sendOTP?.visibility = View.GONE
                sendOTP?.isEnabled = false
                sendOTP?.isClickable = false
                ivPlus?.isEnabled = true
                ivPlus?.isClickable = true
                ivMinus?.isEnabled = true
                ivMinus?.isClickable = true
                if (phoneNumberText.trim().length >= 10) {
                    btnSendAuth?.isEnabled = true
                    btnSendAuth?.isClickable = true
                }
            }
        })

        btnSendAuth?.setOnClickListener {
            KeyboardHelper.hideKeyboard(activity)
            validateAndStartVerification(true)
        }
        sendOTP?.setOnClickListener {
            try {
                sendOTP(phoneNumber!!.text.toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        setDefaultNumber()
        return view
    }

    private fun setDefaultNumber() {
        try {
            ccp!!.fullNumber = Constants.getLastAuthorizedNumber()
            phoneNumber?.setText(Constants.getLastAuthorizedNumber().replace("+", ""))
            ccp!!.registerCarrierNumberEditText(phoneNumber)
            validateAndStartVerification(false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun isError(isError: Boolean, msg: String) {
        if (context == null)
            return
        if (isError) {
            ccp?.contentColor = context!!.getColor(R.color.cal_red)
            errorMessage?.text = msg
            errorMessage?.visibility = View.VISIBLE
            phoneNumber?.setTextColor(resources.getColor(R.color.cal_red, null))
            errorMessage?.setTextColor(resources.getColor(R.color.cal_red, null))
        } else {
            phoneNumber?.setTextColor(resources.getColor(R.color.app_green, null))
            ccp?.contentColor = context!!.getColor(R.color.app_green)
            errorMessage?.setTextColor(resources.getColor(R.color.app_green, null))
            errorMessage?.visibility = View.GONE
        }
    }

    private fun durationUpdate(isAdded: Boolean) {
        try {
            val durationVal: Int = epnDuration?.tag as Int
            if (isAdded) {
                if (durationVal < 25) {
                    epnDuration?.text = (durationVal + 1).toString() + " Years"
                    epnDuration?.tag = durationVal + 1
                    CalculateEPNCreateBalance(durationVal + 1, false).execute()
                }
            } else {
                if (durationVal > 1) {
                    epnDuration?.text =
                        if (durationVal == 2)
                            "1 Year"
                        else
                            (durationVal - 1).toString() + "Years"
                    epnDuration?.tag = durationVal - 1
                    CalculateEPNCreateBalance(durationVal - 1, false).execute()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateAvailableETH(address: String) {
        try {
            val walletBalance = PesaApplication.getWeb3j()
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get()
            val ethTOusd = Constants.getUSDValueFromCurrency("ETH")
            Log.d("ETH_TO_USD", ethTOusd.toString())
            var usdValue = "0.0"
            if (ethTOusd != 0.0) {
                ethBalance = (
                        Convert.fromWei(
                            walletBalance.balance.toBigDecimal(),
                            Convert.Unit.ETHER
                        ).toDouble())
                ethDollarValue = ethBalance!! * ethTOusd
                usdValue = UserInterface.round(ethDollarValue!!)
            }
            ethAvailable?.text =
                "ETH available: " + UserInterface.round(ethBalance!!) + "= $" + usdValue
            CalculateEPNCreateBalance(1, false).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class CalculateEPNCreateBalance(
        year: Int,
        isAuthorized: Boolean
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var totalPayableETH: BigDecimal? = null
        private var totalPayableUSD: BigDecimal? = null
        private var isAuthorized: Boolean? = null
        private var year: Int? = null

        init {
            this.year = year
            this.isAuthorized = isAuthorized
        }

        override fun onPreExecute() {
            btnSendAuth?.isEnabled = false
            btnSendAuth?.isClickable = false
            ivMinus?.isEnabled = false
            ivMinus?.isClickable = false
            ivPlus?.isEnabled = false
            ivPlus?.isClickable = false
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val epn = EPN.load(
                    EPNAddress,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                payableDuration = (year!! * Constants.YEAR_SECONDS).toBigInteger()
                val minimumRegFee = epn.MIN_REGISTRATION_FEE().sendAsync().get()
                val minimumRegDuration = epn.MIN_REGISTRATION_DURATION().sendAsync().get()
                val ethValueBal =
                    payableDuration!! * minimumRegFee / minimumRegDuration
                val ethTOusd = Constants.getUSDValueFromCurrency("ETH").toBigDecimal()
                totalPayableETH = Convert.fromWei(
                    ethValueBal.toBigDecimal(),
                    Convert.Unit.ETHER
                )
                totalPayableUSD = totalPayableETH!! * ethTOusd
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return false
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                try {
                    payableFee = totalPayableETH!!.toDouble()
                    ethValue?.text = UserInterface.round(payableFee!!) + " ETH"
                    usdValue?.text = "$"+UserInterface.round(totalPayableUSD!!.toDouble()) + " USD"
                    val gasPriceGwei =
                        Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                            .toBigInteger()
                    val gasFee = Convert.fromWei(
                        (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                        Convert.Unit.GWEI
                    ).toDouble()
                    tvGasFee?.text=(gasFee+payableFee!!).toString()+" ETH"
                    if (!isAuthorized!!) {
                        ivMinus?.isEnabled = true
                        ivMinus?.isClickable = true
                        ivPlus?.isEnabled = true
                        ivPlus?.isClickable = true
                    }
                    if (ethDollarValue!! > totalPayableUSD!!.toDouble()) {
                        btnSendAuth?.isEnabled = true
                        btnSendAuth?.isClickable = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun epnAvailable(msg: String) {
        if (context == null)
            return
        phoneNumber!!.setTextColor(resources.getColor(R.color.app_green, null))
        ccp!!.contentColor = context!!.getColor(R.color.app_green)
        errorMessage?.setTextColor(resources.getColor(R.color.app_green, null))
        errorMessage!!.visibility = View.VISIBLE
        errorMessage!!.text = msg
    }


    //Mobile Number Verify
    private fun validateAndStartVerification(isProcess: Boolean) {
        val phoneNumberText = phoneNumber!!.text.toString()
        if (!Patterns.PHONE.matcher(phoneNumberText).matches()) {
            isError(true, getString(R.string.invalid_phone_number))
            return
        }
        ccp!!.registerCarrierNumberEditText(phoneNumber)
        val mobileNumber = ccp!!.fullNumberWithPlus
        CheckEPNNumberAvailableTask(mobileNumber.replace("+", ""), isProcess).execute()
        //checkEPNAuth(mobileNumber.replace("+", ""))
    }

    //checkEPNNumberAvailable task
    private inner class CheckEPNNumberAvailableTask(
        phoneNumber: String,
        isProcess: Boolean
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var isProcess: Boolean? = false
        private var phoneNumber: String? = null
        private var statusCode: String? = null

        init {
            this.isProcess = isProcess
            this.phoneNumber = phoneNumber
        }

        override fun onPreExecute() {
            sendOTP?.isClickable = false
            sendOTP?.isEnabled = false
            btnSendAuth?.isClickable = false
            btnSendAuth?.isEnabled = false
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val epn = EPN.load(
                    EPNAddress,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                val transactionReceipt = epn.checkEPNAuthorizedStatus(
                    Constants.getAccountAddress(),
                    phoneNumber!!.toBigInteger()
                ).sendAsync().get()

                authItemId = transactionReceipt.component4().toString()
                statusCode = transactionReceipt.component3().toString()

                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result!!)
                when {
                    statusCode == "1" -> {
                        isError(true, "This EPN is already registered, please try another number")
                    }
                    statusCode == "4" -> {
                        isError(true, "This EPN is already registered, please try another number")
                    }
                    statusCode == "5" -> {
                        isError(true, "This EPN is already registered, please try another number")
                    }
                    statusCode == "3" -> {
                        epnAvailable("This EPN is available to register")
                        sendOTP?.isClickable = true
                        sendOTP?.isEnabled = true
                        sendOTP?.visibility = View.VISIBLE
                        btnSendAuth?.visibility = View.GONE
                        ivPlus?.isClickable = false
                        ivPlus?.isEnabled = false
                        ivMinus?.isClickable = false
                        ivMinus?.isEnabled = false
                    }
                    statusCode == "2" -> {
                        epnAvailable("This EPN is available to register")
                        btnSendAuth?.isClickable = false
                        btnSendAuth?.isEnabled = false
                        sendOTP?.isClickable = true
                        sendOTP?.isEnabled = true
                        sendOTP?.visibility = View.VISIBLE
                        btnSendAuth?.visibility = View.GONE
                        ivPlus?.isClickable = false
                        ivPlus?.isEnabled = false
                        ivMinus?.isClickable = false
                        ivMinus?.isEnabled = false
                        try {
                            val epn = EPN.load(
                                EPNAddress,
                                PesaApplication.getWeb3j(),
                                Constants.getAccountCredential(),
                                CustomGasProvider()
                            )
                            val transactionReceipt = epn.authorizedNumbers(
                                authItemId!!.toBigInteger()
                            ).sendAsync().get()
                            val duration = transactionReceipt.component4().toInt()
                            val totalYear = duration / Constants.YEAR_SECONDS
                            epnDuration?.tag = totalYear
                            epnDuration?.text =
                                if (totalYear == 1)
                                    "1 Year"
                                else
                                    (totalYear).toString() + "Years"
                            CalculateEPNCreateBalance(totalYear, true).execute()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        //verifyEPN(phoneNumber, authItemId!!, "")
                    }
                    statusCode == "6" -> {
                        epnAvailable("This EPN is available to register")
                        btnSendAuth?.isClickable = true
                        btnSendAuth?.isEnabled = true
                        if (isProcess!!)
                            transactionAuthDialog(phoneNumber!!, payableFee!!)
                    }
                    statusCode == "7" -> {
                        epnAvailable("This EPN is available to register")
                        btnSendAuth?.isClickable = true
                        btnSendAuth?.isEnabled = true
                        if (isProcess!!)
                            transactionAuthDialog(phoneNumber!!, payableFee!!)
                    }
                    statusCode == "0" -> {
                        epnAvailable("This EPN is available to register")
                        btnSendAuth?.isClickable = true
                        btnSendAuth?.isEnabled = true
                        if (isProcess!!)
                            transactionAuthDialog(phoneNumber!!, payableFee!!)
                    }

                }
        }

    }


    // Transaction confirmation for EPN Authorization
    private fun transactionAuthDialog(
        phoneNumber: String,
        epnCost: Double
    ) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog.setContentView(R.layout.epn_auth_confirm)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog.window!!.setBackgroundDrawable(d)
            cfmDialog.setCancelable(false)
            val tvNumber = cfmDialog.findViewById<TextView>(R.id.tv_number)
            val tvDuration = cfmDialog.findViewById<TextView>(R.id.tv_duration)
            val tvEPNCost = cfmDialog.findViewById<TextView>(R.id.tv_epn_cost)
            val tvNote = cfmDialog.findViewById<TextView>(R.id.tv_note)
            val btnConfirm = cfmDialog.findViewById<Button>(R.id.btn_confirm)
            val btnReject = cfmDialog.findViewById<Button>(R.id.btn_reject)
            val btnClose = cfmDialog.findViewById<ImageView>(R.id.iv_close)
            try {
                setTextViewHTML(
                    tvNote,
                    "Activation may take upto 48 hours. You’ll be notified once the EPN is ready. <a href=learn_more>Learn more</a>"
                )
                tvNumber.text = "+" + phoneNumber
                tvDuration.text = epnDuration?.text.toString()
                val gasPriceGwei =
                    Convert.fromWei(CustomGasProvider.GAS_PRICE.toBigDecimal(), Convert.Unit.GWEI)
                        .toBigInteger()
                val gasFee = Convert.fromWei(
                    (gasPriceGwei * CustomGasProvider.GAS_LIMIT).toBigDecimal(),
                    Convert.Unit.GWEI
                ).toDouble()
                val totalFee = gasFee + epnCost
                tvEPNCost.text = "Total fee: "+UserInterface.round(totalFee) + "ETH"

            } catch (e: Exception) {
                e.printStackTrace()
            }
            btnConfirm?.setOnClickListener {
                cfmDialog?.dismiss()
                AuthorizedTask(phoneNumber, epnCost).execute()
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

    //authorized task
    private inner class AuthorizedTask(
        phoneNumber: String,
        epnCost: Double
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var phoneNumber: String? = null
        private var epnCost: Double? = null
        private var errorMsg = "Your transaction has been failed..."

        init {
            this.phoneNumber = phoneNumber
            this.epnCost = epnCost
        }

        override fun onPreExecute() {
            UserInterface.showTransactionProgress(
                "Confirming EPN",
                "Transaction is being proceeded",
                context
            )
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val epn = EPN.load(
                    EPNAddress,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                transactionHash = epn.authorizeEPN(
                    phoneNumber!!.toBigInteger(),
                    payableDuration,
                    Convert.toWei(epnCost!!.toBigDecimal(), Convert.Unit.ETHER).toBigInteger()
                ).sendAsync().get().transactionHash
                val transactionReceipt =
                    PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                        .sendAsync().get().transactionReceipt.get()
                Log.d("EPNAuthorized_Receipt_Hash-->", transactionHash)
                if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
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
                Constants.setLastAuthorizedNumber(phoneNumber!!)
                sendOTP(phoneNumber!!)
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.showTransactionFailedDialog(errorMsg, context!!)
            }
        }
    }

    //verify EPN from backend
    private fun verifyEPN(mobNo: String, itemId: String, transactionHash: String) {
        if (context == null)
            return
        UserInterface.showProgress("Loading...", context)
        val params = JSONObject()
        try {
            params.put("phoneNumber", mobNo)
            params.put("walletAddress", Constants.getAccountAddress())
            params.put("itemId", itemId)
            if (transactionHash != "")
                params.put("transactionHash", transactionHash)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.verifyEPN(requestBody, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ResponseStatus> {
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                UserInterface.hideProgress(context)
            }

            override fun onResponse(
                call: Call<ResponseStatus>,
                response: Response<ResponseStatus>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(context)
                    if (response.isSuccessful) {
                        if (it.status) {
                            btnSendAuth?.isClickable = false
                            btnSendAuth?.isEnabled = false
                            sendOTP?.isClickable = true
                            sendOTP?.isEnabled = true
                        } else {
                            UserInterface.showToast(context, it.message)
                        }
                    }
                } ?: run {
                    UserInterface.hideProgress(context)
                    if (response.body() == null) {
                        if (response.code() === 401 || response.code() === 402) {
                            UserInterface.hideProgress(activity!!)
                            UserInterface.showOKTitleAlert(
                                activity,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> activity!!.finish() })

                        } else if (response.code() === 405) {
                            verifyEPN(mobNo, itemId, transactionHash)
                        }
                    }
                }
            }

        })
    }


    //send OTP from backend
    private fun sendOTP(mobNo: String) {
        if (context == null)
            return
        UserInterface.showProgress("sending OTP...", context)
        val params = JSONObject()
        try {
            params.put("phoneNumber", phoneNumber!!.text.toString())
            params.put("countryCode", ccp?.selectedCountryCode)
            params.put("itemId", authItemId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.sendOTP(requestBody, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<ResponseStatus> {
            override fun onFailure(call: Call<ResponseStatus>, t: Throwable) {
                UserInterface.hideProgress(context)
                sendOTP?.visibility = View.VISIBLE
                sendOTP?.isClickable = true
                sendOTP?.isEnabled = true
                btnSendAuth?.visibility = View.GONE
                ivPlus?.isClickable = false
                ivPlus?.isEnabled = false
                ivMinus?.isClickable = false
                ivMinus?.isEnabled = false
            }

            override fun onResponse(
                call: Call<ResponseStatus>,
                response: Response<ResponseStatus>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(context)
                    if (response.isSuccessful) {
                        try {
                            UserInterface.showToast(context, it.message)
                            if (it.status) {
                                ccp!!.registerCarrierNumberEditText(phoneNumber)
                                val mobileNumber = ccp!!.fullNumberWithPlus
                                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                                    .replace(
                                        R.id.shareg_main_container,
                                        EPNOTPVerifyFragment.newInstance(
                                            ccp!!.selectedCountryCode,
                                            mobileNumber,
                                            authItemId!!,
                                            transactionHash!!
                                        )
                                    ).addToBackStack("EPN_Activation").commitAllowingStateLoss()
                            } else {
                                sendOTP?.visibility = View.VISIBLE
                                sendOTP?.isClickable = true
                                sendOTP?.isEnabled = true
                                btnSendAuth?.visibility = View.GONE
                                ivPlus?.isClickable = false
                                ivPlus?.isEnabled = false
                                ivMinus?.isClickable = false
                                ivMinus?.isEnabled = false
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    UserInterface.hideProgress(context)
                    if (response.body() == null) {
                        if (response.code() === 401 || response.code() === 402) {
                            UserInterface.hideProgress(activity!!)
                            UserInterface.showOKTitleAlert(
                                activity,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> activity!!.finish() })
                        }
                    }
                }
            }

        })
    }


}