package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
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
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import com.mukesh.OtpView
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Api.TokenServerApi
import finance.pesa.sdk.Model.EPNCreateStatus
import finance.pesa.sdk.Model.ResponseStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.generator.EPN
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.EPNAddress
import finance.pesa.sdk.utils.CustomGasProvider
import finance.pesa.sdk.utils.KeyboardHelper
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.utils.UserInterface.Companion.getFlagMasterResID
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.utils.Convert
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger

class EPNOTPVerifyFragment : Fragment() {
    private var verifyViaSmsButton: Button? = null
    private var btnClaim: Button? = null
    private var tokenServerApi: TokenServerApi? = null
    private var count_down: TextView? = null
    private var countryCode: String? = null
    private var phNo: String? = null
    private var authItemId: String? = null
    private var transactionHash: String? = ""
    private var ivBack: ImageView? = null
    private var textView_phone: TextView? = null
    private var cTimer: CountDownTimer? = null
    private var otpView: OtpView? = null

    companion object {
        fun newInstance(
            countryCode: String,
            phNo: String,
            authItemId: String,
            transactionHash: String
        ): EPNOTPVerifyFragment {
            val fragment = EPNOTPVerifyFragment()
            fragment.countryCode = countryCode
            fragment.phNo = phNo
            fragment.authItemId = authItemId
            fragment.transactionHash = transactionHash
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.epn_otp_verify, container, false)
        verifyViaSmsButton = view.findViewById(R.id.btn_verify)
        textView_phone = view.findViewById(R.id.textView_phone)
        count_down = view.findViewById(R.id.count_down)
        ivBack = view.findViewById(R.id.iv_back)
        otpView = view.findViewById(R.id.otp_view)
        btnClaim = view.findViewById(R.id.btn_claim)
        textView_phone!!.text = phNo
        count_down!!.isClickable = false
        startTimer(20000)
        initTokenServerApi()
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        // Click to Verify OTP
        verifyViaSmsButton!!.setOnClickListener {
            KeyboardHelper.hideKeyboard(activity)
            if (otpView!!.text.toString().trim().length === 6)
                onOTPVerify(otpView!!.text.toString().trim(), countryCode!!, phNo!!)
            else
                UserInterface.showSnack("Please enter 6-dgit code", false, getView())
        }

        otpView!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (otpView!!.text.toString().trim().length === 6)
                    onOTPVerify(otpView!!.text.toString().trim(), countryCode!!, phNo!!)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        //Click to Back(ReEnter Mobile Number)
        ivBack!!.setOnClickListener { activity!!.onBackPressed() }
        return view
    }


    //verify otp
//verify OTP from backend
    private fun onOTPVerify(code: String, countryCode: String, phoneNumber: String) {
        if (context == null)
            return
        UserInterface.showProgress("verifying OTP...", context)
        val params = JSONObject()
        try {
            params.put("phoneNumber", phoneNumber)
            params.put("countryCode", countryCode)
            params.put("itemId", authItemId)
            params.put("code", code)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<ResponseStatus>? =
            ApiClient.build()?.verifyOTP(requestBody, Constants.getHeader(context)!!)
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
                            if (it.status) {
                                verifyViaSmsButton?.isEnabled = false
                                verifyViaSmsButton?.isClickable = false
                                otpView?.isEnabled = false
                                otpView?.isClickable = false
                                createEPN(phoneNumber)
                            } else
                            // createEPN(phoneNumber)
                                UserInterface.showToast(context, it.message)
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


    //start timer function
    internal fun startTimer(totalMilliSec: Long) {
        cTimer = object : CountDownTimer(totalMilliSec, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (activity != null) {
                    val seconds = millisUntilFinished / 1000
                    count_down!!.text =
                        "We sent you a code. If you did not receive the code tap to " + String.format(
                            "%02d",
                            seconds
                        )
                    // count_down!!.isClickable = false
                    // count_down!!.setTextColor(resources.getColor(R.color.light_gray_white, null))
                }
            }

            override fun onFinish() {
                if (activity != null) {
                    setTextViewHTML(
                        count_down!!,
                        "We sent you a code. If you did not receive the code tap to <a href=resend>Resend</a>."
                    )
                    // count_down!!.isClickable = true
                    // count_down!!.setTextColor(resources.getColor(R.color.light_gray_white, null))
                }
            }
        }
        cTimer!!.start()
    }

    //cancel timer
    internal fun cancelTimer() {
        if (cTimer != null)
            cTimer!!.cancel()
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
                    if (span.url == "resend") {
                        //Resend OTP
                        onOTPReSend()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    override fun onDestroy() {
        cancelTimer()
        super.onDestroy()
    }

    private fun initTokenServerApi() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.TOKEN_SERVER_URL)
            .build()
        tokenServerApi = retrofit.create(finance.pesa.sdk.Api.TokenServerApi::class.java!!)
    }

    //resend OTP from backend
    private fun onOTPReSend() {
        if (context == null)
            return
        UserInterface.showProgress("sending OTP...", context)
        val params = JSONObject()
        try {
            params.put("phoneNumber", phNo)
            params.put("countryCode", countryCode)
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


    //CREATE EPN number in backend
    private fun createEPN(epnNumber: String) {
        UserInterface.showTransactionProgress(
            "Claiming EPN",
            "Please wait...",
            context
        )
        if (context == null)
            return
        val params = JSONObject()
        try {
            params.put("phoneNumber", epnNumber)
            params.put("walletAddress", Constants.getAccountAddress())
            params.put("itemId", authItemId)
            if (transactionHash != "")
                params.put("transactionHash", transactionHash)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<EPNCreateStatus>? =
            ApiClient.build()?.createEPNNumber(requestBody, Constants.getHeader(context)!!)
        call?.enqueue(object : Callback<EPNCreateStatus> {
            override fun onFailure(call: Call<EPNCreateStatus>, t: Throwable) {
                UserInterface.hideProgress(context)
                btnClaim?.visibility = View.VISIBLE
                UserInterface.showOKTitleAlert(
                    activity,
                    "Alert",
                    t.message.toString(),
                    true,
                    DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            }

            override fun onResponse(
                call: Call<EPNCreateStatus>,
                response: Response<EPNCreateStatus>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(context)
                    if (response.isSuccessful) {
                        if (it.status) {
                            try {
                                clearPreviousScreen()
                                Constants.setIsEPNEnabled(true)
                                Constants.setEPNNumber(epnNumber)
                                Constants.setEPNId(authItemId!!)
                                if (Constants.getEPNActivationListener() != null)
                                    Constants.getEPNActivationListener()!!.onEPNRegistered(
                                        epnNumber,
                                        it.isClaimEnable,
                                        it.message
                                    )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            btnClaim?.visibility = View.VISIBLE
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

                        } else {
                            btnClaim?.visibility = View.VISIBLE
                            UserInterface.showOKTitleAlert(
                                activity,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                        }
                    }
                }
            }

        })
    }


    private fun clearPreviousScreen() {
        try {
            if (PesaApplication.getChildFragmentManager() != null) {
                val mFragmentManager = PesaApplication.getChildFragmentManager()
                if (mFragmentManager.backStackEntryCount > 0) {
                    for (i in 0 until mFragmentManager.backStackEntryCount) {
                        if (mFragmentManager.getBackStackEntryAt(i).name == "EPN_Activation")
                            mFragmentManager.popBackStackImmediate(
                                mFragmentManager.getBackStackEntryAt(
                                    i
                                ).name, i
                            )
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}