package finance.pesa.sdk.view.UI

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
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
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.mukesh.OtpView
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.AvailableDevice
import finance.pesa.sdk.Model.TokenServerResponse
import finance.pesa.sdk.Model.VerifyModel
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.ConnectionUtils.isConnectedToInternet
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.TOKEN_SERVER_URL
import finance.pesa.sdk.utils.KeyboardHelper
import finance.pesa.sdk.utils.UserInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EnterOTP : Fragment() {
    private var verifyViaSmsButton: Button? = null
    private var tokenServerApi: finance.pesa.sdk.Api.TokenServerApi? = null
    private var count_down: TextView? = null
    internal var countryCode: String? = null
    internal var phNo: String? = null
    private var ivBack: ImageView? = null
    internal var textView_phone: TextView? = null
    internal var cTimer: CountDownTimer? = null
    private var otpView: OtpView? = null

    companion object {
        fun newInstance(bundle: Bundle): EnterOTP {
            val fragment = EnterOTP()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.register_otp_verify, container, false)
        verifyViaSmsButton = view.findViewById(R.id.btn_verify)
        textView_phone = view.findViewById(R.id.textView_phone)
        count_down = view.findViewById(R.id.count_down)
        ivBack = view.findViewById(R.id.iv_back)
        otpView = view.findViewById(R.id.otp_view)
        val bundle = arguments
        countryCode = bundle!!.getString("countryCode")
        phNo = bundle.getString("phNo")
        textView_phone!!.text = phNo
        count_down!!.isClickable = false
        startTimer(20000)
        initTokenServerApi()
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        // Click to Verify OTP
        verifyViaSmsButton!!.setOnClickListener {
            KeyboardHelper.hideKeyboard(activity)
            if (otpView!!.getText().toString().trim().length === 6)
                onOTPVerify(otpView!!.getText().toString().trim(), countryCode!!, phNo!!)
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
        return view;
    }

    //Resend OTP
    private fun onOTPReSend() {
        UserInterface.showProgress("Sending Code", activity)
        tokenServerApi!!
            .getOTP("sms", countryCode, "6", "en", phNo)
            .enqueue(object : Callback<TokenServerResponse> {
                override fun onResponse(
                    call: Call<TokenServerResponse>,
                    response: Response<TokenServerResponse>
                ) {
                    UserInterface.hideProgress(activity!!)
                    try {
                        startTimer(20000)
                        UserInterface.showToast(activity, "Code Resent to\n$phNo")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        UserInterface.showSnack("Verification Failed...", false, view)
                    }

                }

                override fun onFailure(call: Call<TokenServerResponse>, t: Throwable) {
                    UserInterface.hideProgress(activity!!)
                    UserInterface.showSnack(t.message.toString(), false, view)
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
            .baseUrl(TOKEN_SERVER_URL)
            .build()
        tokenServerApi = retrofit.create(finance.pesa.sdk.Api.TokenServerApi::class.java!!)
    }

    // Verify Mobile Number using OTP
    fun onOTPVerify(code: String, countryCode: String, phNo: String) {
        UserInterface.showProgress("Verifying", activity)
        tokenServerApi!!
            .getVerify(countryCode, code, phNo)
            .enqueue(object : Callback<VerifyModel> {
                override fun onResponse(call: Call<VerifyModel>, response: Response<VerifyModel>) {
                    UserInterface.hideProgress(activity!!)
                    try {
                        if (response.body()!!.success.contentEquals("true")) {
                            try {
                                cancelTimer()
                                verifyViaSmsButton!!.isClickable = false
                                verifyViaSmsButton!!.isEnabled = false

                                val pref = PreferenceManager.getDefaultSharedPreferences(activity)
                                val lastUserNo = pref.getString(Constants.LAST_AUTH_PHONE, null)
                                /* if (lastUserNo != null) {
                                     if (lastUserNo != phNo) {
                                         val db = WificoinDB.getDatabase(activity)
                                         db.clearAllTables()
                                     }
                                 }*/
                                //Save Verified Mobile Number
                                PesaApplication.setUserPhNo(phNo)
                                val ed = pref.edit()
                                ed.putString(Constants.PARSE_AUTH_PHONE, phNo)
                                ed.putString(Constants.LAST_AUTH_PHONE, phNo)
                                ed.apply()
                                UserInterface.showToast(activity, "Verified")
                                userValidation(phNo)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else {
                            UserInterface.showSnack("Verification Failed...", false, view)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        UserInterface.showSnack("Verification Failed...", false, view)
                    }

                }

                override fun onFailure(call: Call<VerifyModel>, t: Throwable) {
                    UserInterface.hideProgress(activity!!)
                    if (isConnectedToInternet(activity!!)) {
                        UserInterface.showSnack(t.message.toString(), false, view)
                    } else {
                        UserInterface.showSnack(
                            resources.getString(R.string.no_internet),
                            false,
                            view
                        )
                    }
                }
            })
    }

    private fun userValidation(mobNo: String) {
        if (context == null)
            return
        cancelTimer()
        val prefst = PreferenceManager.getDefaultSharedPreferences(activity)
        val edst = prefst.edit()
        edst.putString(Constants.PREF_UNIQUE_ID, null)
        edst.apply()
        UserInterface.hideProgress(activity!!)
        val params = JSONObject()
        try {
            params.put("phoneNumber", mobNo)
            params.put("os", "Android")
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody1 = params.toString().toRequestBody(JSON)
        var call: Call<AvailableDevice>? = ApiClient.build()?.findDevice(requestBody1)
        call?.enqueue(object : Callback<AvailableDevice> {
            override fun onFailure(call: Call<AvailableDevice>, t: Throwable) {
                userValidation(mobNo)
            }

            override fun onResponse(
                call: Call<AvailableDevice>,
                response: Response<AvailableDevice>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        try {
                            if (it.status) {
                                Constants.setLastWalletAddress(it.walletAddress)
                                if (context == null)
                                    return
                                PesaApplication.getChildFragmentManager().beginTransaction()
                                    .replace(
                                        R.id.shareg_main_container,
                                        WelcomeImportWallet.newInstance(mobNo)
                                    ).commitAllowingStateLoss()
                                try {
                                    val manager = PesaApplication.getChildFragmentManager()
                                    if (manager!!.getFragments() != null && manager.getFragments().size > 0) {
                                        for (i in 0 until manager.getFragments().size) {
                                            val mFragment = manager.getFragments().get(i)
                                            if (mFragment != null) {
                                                manager.beginTransaction().remove(mFragment!!)
                                                    .commitAllowingStateLoss()
                                                manager.popBackStackImmediate()
                                            }
                                        }
                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            } else {
                                if (context == null)
                                    return
                                PesaApplication.getChildFragmentManager().beginTransaction()
                                    .replace(
                                        R.id.shareg_main_container,
                                        WelcomeCreateWallet.newInstance(mobNo)
                                    ).commitAllowingStateLoss()
                                try {
                                    val manager = PesaApplication.getChildFragmentManager()
                                    if (manager!!.getFragments() != null && manager.getFragments().size > 0) {
                                        for (i in 0 until manager.getFragments().size) {
                                            val mFragment = manager.getFragments().get(i)
                                            if (mFragment != null) {
                                                manager.beginTransaction().remove(mFragment!!)
                                                    .commitAllowingStateLoss()
                                                manager.popBackStackImmediate()
                                            }
                                        }
                                    }
                                }catch (e:Exception){
                                    e.printStackTrace()
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (response.code() === 401 || response.code() === 402 || response.code() === 403) {
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
                            userValidation(mobNo)
                        }
                    }
                }
            }

        })
    }


}