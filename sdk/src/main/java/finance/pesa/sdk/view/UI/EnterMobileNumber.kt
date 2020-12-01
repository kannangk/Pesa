package finance.pesa.sdk.view.UI

import android.app.Activity.RESULT_OK
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.CredentialPickerConfig
import com.google.android.gms.auth.api.credentials.HintRequest
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.hbb20.CountryCodePicker
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.BuildConfig
import finance.pesa.sdk.Model.AvailableDevice
import finance.pesa.sdk.Model.TokenServerResponse
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

class EnterMobileNumber : Fragment(), GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener {
    override fun onConnected(bundle: Bundle?) {
        Log.d(TAG, "GoogleApiClient Connected")
    }

    override fun onConnectionSuspended(cause: Int) {
        Log.d(TAG, "GoogleApiClient is suspended with cause code: $cause")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(TAG, "GoogleApiClient failed to connect: $connectionResult")
    }

    val TAG: String = "EnterMobileNumber"
    internal var PHONE_PICKER_RESULT_CODE = 1000
    internal var phoneNumber: EditText? = null
    internal var ccp: CountryCodePicker? = null
    internal var error_message: TextView? = null
    internal var receive_msg: TextView? = null
    internal var iv_back: ImageView? = null
    internal var ph_layout: LinearLayout? = null
    private var sendOTPButton: Button? = null
    private var googleApiClient: GoogleApiClient? = null
    private var tokenServerApi: finance.pesa.sdk.Api.TokenServerApi? = null
    var PERMISSION_ALL = 1
    internal var selectedCountryCode: String? = null
    internal var isFirstLoad: Boolean? = false
    private var mainView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mainView == null) {
            isFirstLoad = true
        }
        mainView = inflater.inflate(R.layout.activity_verification, container, false)
        phoneNumber = mainView!!.findViewById(R.id.phone_number)
        ph_layout = mainView!!.findViewById(R.id.ph_layout)
        error_message = mainView!!.findViewById(R.id.error_message)
        receive_msg = mainView!!.findViewById(R.id.receive_msg)
        iv_back = mainView!!.findViewById(R.id.iv_back)
        sendOTPButton = mainView!!.findViewById(R.id.send_code)
        ccp = mainView!!.findViewById(R.id.ccp)
        ccp!!.contentColor = context!!.getColor(R.color.app_green)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        initTokenServerApi()
        isError(false, "")
        if (isFirstLoad!!) {
            isFirstLoad = false
            try {
                onLoadNumberPicker()
                ccp!!.setAutoDetectedCountry(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        } else {
            if (selectedCountryCode != null) {
                ccp!!.setCountryForNameCode(selectedCountryCode)
            }
        }


        iv_back!!.setOnClickListener { v -> activity!!.finish() }

        //Click to Send OTP
        sendOTPButton!!.setOnClickListener(View.OnClickListener {
            KeyboardHelper.hideKeyboard(activity)
            validateAndStartVerification()
        })

        phoneNumber!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                isError(false, "")
            }
        })

        return mainView
    }


    private fun onLoadNumberPicker() {
        initGoogleApiClient()
        attemptToFetchPhoneNumber()

    }

    //Mobile Number Verify
    private fun validateAndStartVerification() {
        val phoneNumberText = phoneNumber!!.text.toString()
        if (!Patterns.PHONE.matcher(phoneNumberText).matches()) {
            isError(true, getString(R.string.invalid_phone_number))
            return
        }
        if (BuildConfig.DEBUG) {
            ccp!!.registerCarrierNumberEditText(phoneNumber)
            val mobileNumber = ccp!!.fullNumberWithPlus
            skippedOTPVerify(mobileNumber)
        } else {
            if (isConnectedToInternet(context)) {
                onOTPSend(phoneNumberText)
            } else
                isError(true, getString(R.string.no_internet))
        }
    }

    fun isError(isError: Boolean, msg: String) {
        if (context == null)
            return
        if (isError) {
            ccp!!.contentColor = context!!.getColor(R.color.cal_red)
            error_message!!.text = msg
            receive_msg!!.visibility = View.GONE
            error_message!!.visibility = View.VISIBLE
            phoneNumber!!.setTextColor(resources.getColor(R.color.cal_red, null))
        } else {
            phoneNumber!!.setTextColor(resources.getColor(R.color.app_green, null))
            ccp!!.contentColor = context!!.getColor(R.color.app_green)
            receive_msg!!.visibility = View.VISIBLE
            error_message!!.visibility = View.GONE
        }
    }


    //initialize API with retrofit
    private fun initTokenServerApi() {
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(TOKEN_SERVER_URL)
            .build()

        tokenServerApi = retrofit.create(finance.pesa.sdk.Api.TokenServerApi::class.java)
    }

    private fun initGoogleApiClient() {
        try {
            googleApiClient = GoogleApiClient.Builder(context!!)
                .addConnectionCallbacks(this)
                .enableAutoManage(activity!!, this)
                .addApi(Auth.CREDENTIALS_API)
                .build()
            PesaApplication.setGoogleApiClient(googleApiClient!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    protected fun attemptToFetchPhoneNumber() {
        KeyboardHelper.hideKeyboard(activity)
        val credentialPickerConfig = CredentialPickerConfig.Builder()
            .setShowCancelButton(true)
            .build()
        val hintRequest = HintRequest.Builder()
            .setPhoneNumberIdentifierSupported(true)
            .setHintPickerConfig(credentialPickerConfig)
            .build()

        val intent = Auth.CredentialsApi.getHintPickerIntent(googleApiClient, hintRequest)
        try {
            startIntentSenderForResult(
                intent.intentSender,
                PHONE_PICKER_RESULT_CODE,
                null,
                0,
                0,
                0,
                null
            )
        } catch (e: IntentSender.SendIntentException) {
            Log.e(TAG, "Could not start hint picker Intent", e)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PHONE_PICKER_RESULT_CODE) {
            if (resultCode == RESULT_OK) {
                val cred =
                    data!!.getParcelableExtra<com.google.android.gms.auth.api.credentials.Credential>(
                        com.google.android.gms.auth.api.credentials.Credential.EXTRA_KEY
                    )
                phoneNumber!!.setText(cred!!.id)
                phoneNumber!!.setSelection(phoneNumber!!.text.length)
                sendOTPButton!!.performClick()
            } else {
                // Show keyboard
                phoneNumber!!.requestFocus()
                KeyboardHelper.showKeyboard(activity, phoneNumber)
            }
        }
    }


    //Send OTP
    private fun onOTPSend(numberToVerify: String) {
        UserInterface.showProgress("Sending Code", context)
        tokenServerApi!!
            .getOTP("sms", ccp!!.selectedCountryCode, "6", "en", numberToVerify)
            .enqueue(object : Callback<TokenServerResponse> {
                override fun onResponse(
                    call: Call<TokenServerResponse>,
                    response: Response<TokenServerResponse>
                ) {
                    UserInterface.hideProgress(context!!)
                    try {
                        UserInterface.showToast(context, "Code sent")
                        ccp!!.registerCarrierNumberEditText(phoneNumber)
                        val mobileNumber = ccp!!.fullNumberWithPlus
                        val bundle = Bundle()
                        selectedCountryCode = ccp!!.selectedCountryNameCode
                        bundle.putString("countryCode", ccp!!.selectedCountryCode)
                        bundle.putString("phNo", mobileNumber)
                        PesaApplication.getChildFragmentManager()!!.beginTransaction().add(
                            R.id.shareg_main_container,
                            EnterOTP.newInstance(bundle)
                        ).addToBackStack("OTP_Main").commitAllowingStateLoss()

                    } catch (e: Exception) {
                        e.printStackTrace()
                        UserInterface.showSnack("Verification Failed...", false, view)
                    }

                }

                override fun onFailure(call: Call<TokenServerResponse>, t: Throwable) {
                    UserInterface.hideProgress(context!!)
                    if (isConnectedToInternet(context)) {
                        isError(true, t.message.toString())
                        UserInterface.showSnack(t.message.toString(), false, view)
                    } else {
                        isError(true, resources.getString(R.string.no_internet))
                        UserInterface.showSnack(
                            resources.getString(R.string.no_internet),
                            false,
                            view
                        )

                    }
                }
            })

    }

    private fun skippedOTPVerify(phNo: String) {
        try {
            sendOTPButton!!.isEnabled = false
            sendOTPButton!!.isClickable = false
            val pref = PreferenceManager.getDefaultSharedPreferences(activity)
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

    }

    private fun userValidation(mobNo: String) {
        if (context == null)
            return
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
                                PesaApplication.getChildFragmentManager()!!.beginTransaction().replace(
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
                                PesaApplication.getChildFragmentManager()!!.beginTransaction().replace(
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
                            userValidation(mobNo)
                        }
                    }
                }
            }

        })
    }
}