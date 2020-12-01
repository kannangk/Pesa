/*
package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import de.hdodenhof.circleimageview.CircleImageView
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.ResponseStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.EPN
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.EPNAddress
import finance.pesa.sdk.utils.Constants.YEAR_SECONDS
import finance.pesa.sdk.utils.CustomGasProvider
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.utils.UserInterface.Companion.getFlagMasterResID
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

class RegisterEPNFragment : Fragment() {
    private var ivBack: ImageView? = null
    private var ivMinus: ImageButton? = null
    private var ivPlus: ImageButton? = null
    private var ivCountryFlag: CircleImageView? = null
    private var epnNumber: TextView? = null
    private var epnDuration: TextView? = null
    private var usdValue: TextView? = null
    private var ethValue: TextView? = null
    private var ethAvailable: TextView? = null
    private var btnRegister: Button? = null
    private var ethBalance: Double? = 0.0
    private var ethDollarValue: Double? = 0.0
    private var payableFee: Double? = 0.0
    private var authItemId: String? = null
    private var phoneNumber: String? = null


    companion object {
        val registerEPNFragment = RegisterEPNFragment()
        fun newInstance(authItemId: String,phoneNumber: String): RegisterEPNFragment {
            registerEPNFragment.authItemId=authItemId
            registerEPNFragment.phoneNumber=phoneNumber
            return registerEPNFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_epn, container, false)
        ivBack = view?.findViewById(R.id.iv_back)
        ivCountryFlag = view?.findViewById(R.id.iv_country_flag)
        epnNumber = view?.findViewById(R.id.epn_number)
        ivMinus = view?.findViewById(R.id.iv_minus)
        ivPlus = view?.findViewById(R.id.iv_plus)
        epnDuration = view?.findViewById(R.id.epn_duration)
        usdValue = view?.findViewById(R.id.usd_value)
        ethValue = view?.findViewById(R.id.eth_value)
        ethAvailable = view?.findViewById(R.id.eth_available)
        btnRegister = view?.findViewById(R.id.btn_register)
        epnDuration?.tag = 1
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        updateAvailableETH(Constants.getAccountAddress())
        ivBack?.setOnClickListener { activity?.onBackPressed() }
        ivPlus?.setOnClickListener {
            durationUpdate(true)
        }
        ivMinus?.setOnClickListener {
            durationUpdate(false)
        }
        btnRegister?.setOnClickListener {
            transactionEPNRegisterDialog(payableFee!!)
        }
        return view
    }

    private fun durationUpdate(isAdded: Boolean) {
        try {
            val durationVal: Int = epnDuration?.tag as Int
            if (isAdded) {
                if (durationVal < 25) {
                    epnDuration?.text = (durationVal + 1).toString() + " Years"
                    epnDuration?.tag = durationVal + 1
                    calculateEPNCreateBalance(durationVal + 1).execute()
                }
            } else {
                if (durationVal > 1) {
                    epnDuration?.text =
                        if (durationVal == 2)
                            "1 Year"
                        else
                            (durationVal - 1).toString() + "Years"
                    epnDuration?.tag = durationVal - 1
                    calculateEPNCreateBalance(durationVal - 1).execute()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun updateAvailableETH(address: String) {
        try {
            try {
                val ccp_loadFullNumber = CountryCodePicker(context)
                ccp_loadFullNumber!!.fullNumber = phoneNumber
                val isoCode = ccp_loadFullNumber?.selectedCountryNameCode
                ivCountryFlag?.setImageDrawable(
                    context!!.getDrawable(
                        getFlagMasterResID(
                            isoCode
                        )
                    )
                )
                epnNumber?.text=phoneNumber
            } catch (e: Exception) {
                e.printStackTrace()
                ivCountryFlag?.setImageDrawable(
                    context!!.getDrawable(
                        getFlagMasterResID(
                            "us"
                        )
                    )
                )
            }

            val walletBalance = PesaApplication.getWeb3j()
                .ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get()
            val ethTOusd = Constants.getUSDValueFromETH()
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
            calculateEPNCreateBalance(1).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private inner class calculateEPNCreateBalance(
        year: Int
    ) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var totalPayableETH: BigDecimal? = null
        private var totalPayableUSD: BigDecimal? = null
        private var year: Int? = null

        init {
            this.year = year
        }

        override fun onPreExecute() {
            btnRegister?.isEnabled = false
            btnRegister?.isClickable = false
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
                val totalSeconds = year!! * YEAR_SECONDS
                val minimumRegFee = epn.MIN_REGISTRATION_FEE().sendAsync().get()
                val minimumRegDuration = epn.MIN_REGISTRATION_DURATION().sendAsync().get()
                val ethValueBal =
                    totalSeconds.toBigInteger() * minimumRegFee / minimumRegDuration
                val ethTOusd = Constants.getUSDValueFromETH().toBigDecimal()
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
                    usdValue?.text = UserInterface.round(totalPayableUSD!!.toDouble()) + "USD"
                    ivMinus?.isEnabled = true
                    ivMinus?.isClickable = true
                    ivPlus?.isEnabled = true
                    ivPlus?.isClickable = true
                    if (ethDollarValue!! > totalPayableUSD!!.toDouble()) {
                        btnRegister?.isEnabled = true
                        btnRegister?.isClickable = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    // Transaction confirmation for EPN Register
    private fun transactionEPNRegisterDialog(
        fees: Double
    ) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog!!.setContentView(R.layout.epn_auth_confirm)
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
                totalPriceTxt.text = totalFee + " + " + UserInterface.round(fees)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            btnConfirm?.setOnClickListener {
                cfmDialog?.dismiss()
                val durationVal: Int = epnDuration?.tag as Int
                val totalSeconds = durationVal!! * YEAR_SECONDS
                CreateEPNTask(totalSeconds.toBigInteger(),fees).execute()
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


    //CreateEPN task
    private inner class CreateEPNTask(durationEPN:BigInteger,fees: Double) :
        AsyncTask<Boolean, Boolean, Boolean>() {
        private var transactionHash: String? = null
        private var fees: Double? = null
        private var durationEPN: BigInteger? = null

        init {
            this.durationEPN=durationEPN
            this.fees=fees
        }

        override fun onPreExecute() {
            UserInterface.showProgress("Loading...", context)
        }

        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                val epn = EPN.load(
                    EPNAddress,
                    PesaApplication.getWeb3j(),
                    Constants.getAccountCredential(),
                    CustomGasProvider()
                )
                this.transactionHash = epn.createEPN(
                    Convert.toWei(fees!!.toBigDecimal(), Convert.Unit.ETHER).toBigInteger(),
                    Constants.getAccountAddress(),
                    authItemId?.toBigInteger(),
                    durationEPN
                ).sendAsync().get().transactionHash
                val transactionReceipt =
                    PesaApplication.getWeb3j().ethGetTransactionReceipt(transactionHash)
                        .sendAsync().get().transactionReceipt.get()
                Log.d("EPNCreate_Receipt_Hash-->", transactionHash)
                if (transactionReceipt.isStatusOK && transactionReceipt.logs.isNotEmpty()) {
                    return true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

        override fun onPostExecute(result: Boolean?) {
            if (result!!) {
                try {
                    Constants.setEPNNumber(phoneNumber!!)
                    Constants.setIsEPNEnabled(true)
                    updateEPN(phoneNumber!!, transactionHash!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                UserInterface.loadWalletDatas(context!!, true)
            } else {
                UserInterface.hideProgress(context)
                UserInterface.showToast(context, "Create EPN failed...")
            }
        }
    }


    //update EPN number in backend
    private fun updateEPN(epnNumber: String, transactionHash: String) {
        if (context == null)
            return
        UserInterface.showProgress("Loading...", context)
        val params = JSONObject()
        try {
            params.put("EPNNumber", epnNumber)
            params.put("itemId", authItemId)
            if(transactionHash!="")
                params.put("transactionHash", transactionHash)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.updateEPNNumber(requestBody, Constants.getHeader(context)!!)
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
                        try {
                            successEPNRegisterDialog()
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

    // Transaction success for EPN Register
    private fun successEPNRegisterDialog() {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog!!.setContentView(R.layout.epn_register_success)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog!!.window!!.setBackgroundDrawable(d)
            cfmDialog!!.setCancelable(false)
            val phNumber = cfmDialog!!.findViewById<TextView>(R.id.epn_number)
            val ivFlag = cfmDialog!!.findViewById<ImageView>(R.id.iv_country_flag)
            val btnDone = cfmDialog!!.findViewById<Button>(R.id.btn_done)
            try {
                val ccp_loadFullNumber = CountryCodePicker(context)
                ccp_loadFullNumber!!.fullNumber = phoneNumber
                val isoCode = ccp_loadFullNumber?.selectedCountryNameCode
                ivFlag?.setImageDrawable(
                    context!!.getDrawable(
                        getFlagMasterResID(
                            isoCode
                        )
                    )
                )
                phNumber?.text=phoneNumber
            } catch (e: Exception) {
                e.printStackTrace()
                ivFlag?.setImageDrawable(
                    context!!.getDrawable(
                        getFlagMasterResID(
                            "us"
                        )
                    )
                )
            }
            btnDone?.setOnClickListener {
                cfmDialog?.dismiss()
                activity!!.onBackPressed()
            }

            cfmDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}*/
