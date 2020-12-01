package finance.pesa.sdk.view.UI

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.parse.ParseInstallation
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.CredentialStatus
import finance.pesa.sdk.Model.DeviceStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.FetchDataBackgroungService
import finance.pesa.sdk.utils.UserInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import org.web3j.crypto.*
import org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT
import org.web3j.utils.Numeric
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

class PasswordEnterFragment : Fragment() {

    private var btnContinue: Button? = null
    private var ivBack: ImageView? = null
    private var pwdText: OtpView? = null
    private var cfmPwdText: OtpView? = null
    private var errorMessage: TextView? = null
    private var mobileNumber: String? = null
    private var mnemonicWord: String? = null
    private var isNewUser: Boolean? = null

    companion object {
        fun newInstance(
            mobileNumber: String, mnemonicWord: String, isNewUser: Boolean
        ): PasswordEnterFragment {
            val fragment = PasswordEnterFragment()
            fragment.mobileNumber = mobileNumber
            fragment.mnemonicWord = mnemonicWord
            fragment.isNewUser = isNewUser
            return fragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.enter_pwd, container, false)
        pwdText = view.findViewById(R.id.pwd_view)
        cfmPwdText = view.findViewById(R.id.cfm_pwd_view)
        ivBack = view.findViewById(R.id.iv_back)
        btnContinue = view.findViewById(R.id.btn_continue)
        errorMessage = view.findViewById(R.id.error_message)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        btnContinue!!.setOnClickListener {
            if (pwdText?.text.toString().length == 6 && cfmPwdText?.text.toString().length == 6) {
                if (pwdText?.text.toString() == cfmPwdText?.text.toString()) {
                    // if (Constants.getAccountCredential() == null)
                    generateWallet(pwdText!!.text.toString().trim())
                    /*else {
                        if (isNewUser!!)
                            register()
                        else
                            doPasswordAuth()
                    }*/
                } else {
                    cfmPwdText?.setTextColor(context?.getColor(R.color.error_color)!!)
                    errorMessage?.visibility = View.VISIBLE
                    UserInterface.errorShow(cfmPwdText, context)
                    UserInterface.errorShow(errorMessage, context)
                }
            }
        }

        cfmPwdText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                cfmPwdText?.setTextColor(context?.getColor(R.color.app_green)!!)
                errorMessage?.visibility = View.GONE
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        ivBack!!.setOnClickListener {
            activity!!.onBackPressed()
        }

        return view
    }

    private fun generateWallet(password: String) {
        try {
            val masterKeypair =
                Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonicWord, null))
            val path = intArrayOf(
                44 or HARDENED_BIT,
                60 or HARDENED_BIT,
                0 or HARDENED_BIT,
                0,
                0
            )
            val x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)
            val credentials = Credentials.create(x)
            if (credentials != null) {
                Log.d("mnemonic", mnemonicWord)
                Log.d("old_account_address", Keys.toChecksumAddress(credentials.address))
                Log.d("new_account_address", credentials.address)
                Log.d(
                    "credential_private_key",
                    Numeric.toHexStringNoPrefix(credentials.ecKeyPair.privateKey)
                )
                Log.d(
                    "credential_public_key",
                    Numeric.toHexStringNoPrefix(credentials.ecKeyPair.publicKey)
                )
                Constants.setCredentials(credentials, context!!)
                if (isNewUser!!)
                    getDeviceStatus()
                else
                    getLoginDeviceStatus()
                return
            }
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: CipherException) {
            e.printStackTrace()
        }
    }

    private fun register() {
        UserInterface.showProgress("Creating Account...", activity)
        val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
        val params = JSONObject()

        try {
            params.put("walletAddress", Constants.getAccountAddress())
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val inviteCode = prefs.getString(Constants.BRANCH_REFERRAL_CODE, null)
            if (inviteCode != null)
                params.put("inviteCode", inviteCode)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = params.toString().toRequestBody(JSON)
        var call: Call<CredentialStatus>? =
            ApiClient.build()?.registerDevice(requestBody, Constants.getHeader(context!!)!!)
        call?.enqueue(object : Callback<CredentialStatus> {
            override fun onFailure(call: Call<CredentialStatus>, t: Throwable) {
                UserInterface.hideProgress(activity!!)
            }

            override fun onResponse(
                call: Call<CredentialStatus>,
                response: Response<CredentialStatus>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(activity!!)
                    if (response.isSuccessful) {
                        try {
                            var basicDetailsService = FetchDataBackgroungService()
                            basicDetailsService.loadFirstAllMarkets(
                                1,
                                context!!
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Constants.setScreenPwd(context!!, cfmPwdText?.text.toString().trim())
                        PesaApplication.getChildFragmentManager().beginTransaction().replace(
                            R.id.shareg_main_container,
                            AccountConfigurationSuccessFragment.newInstance(false)
                        ).commitAllowingStateLoss()
                    }
                } ?: run {
                    if (response.body() == null) {
                        UserInterface.hideProgress(activity!!)
                        if (response.code() === 401 || response.code() === 402 || response.code() === 403) {
                            UserInterface.showOKTitleAlert(activity,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> activity!!.finish() })

                        } else {
                            UserInterface.showSnack(if (UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ).contains("incorrect")
                            )
                                resources.getString(R.string.password_incorrect)
                            else
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                view,
                                resources.getString(R.string.retry),
                                View.OnClickListener {
                                    if (response.code() === 401)
                                        if (context != null)
                                            UserInterface.unAuthorized(context!!, 401)
                                        else
                                            register()
                                })
                        }
                    }
                }
            }

        })

    }

    private fun getLoginDeviceStatus() {
        UserInterface.showProgress("Signing in..", activity)
        var call: Call<DeviceStatus>? =
            ApiClient.build()
                ?.getDeviceStatus(getDeviceInfo()!!, Constants.getHeader(context!!)!!)
        call?.enqueue(object : Callback<DeviceStatus> {
            override fun onFailure(call: Call<DeviceStatus>, t: Throwable) {
                UserInterface.hideProgress(context!!)
                UserInterface.showSnack(
                    UserInterface.errorMessage(t.message.toString()),
                    false,
                    view
                )
            }

            override fun onResponse(
                call: Call<DeviceStatus>,
                response: Response<DeviceStatus>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it.userName != null)
                            Constants.setUserName(context!!, it.userName)
                        if (it.deviceId != null) {
                            Constants.setDeviceId(it.deviceId, context!!)
                        }
                        /*try {
                            Constants.setMintFromList(it.MINT_BUY_FROM_TOKEN)
                            Constants.setMintToList(it.MINT_BUY_TO_TOKEN)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }*/
                        try {
                            Constants.setEncryptionKeyDatas(
                                it.CIPHER_ALGO,
                                it.CIPHER_SALT,
                                it.ENCRYPTION_KEY,
                                it.BUFFEFR_KEY,
                                context!!, it.JWT_TOKEN_SECRET!!
                            )
                            Constants.setMaximumNumbers(it.MAXIMUM_NUMBERS)
                            Constants.setSupportEmailId(it.SUPPORT_EMAIL, context)
                            Constants.setLastWalletAddress(it.walletAddress)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        try {
                            Constants.setIsEPNEnabled(it.isEPNEnabled)
                            if (it.isEPNEnabled) {
                                Constants.setEPNNumber(it.EPNNumber)
                                Constants.setEPNId(it.EPN_ID)
                            } else {
                                Constants.setEPNNumber("")
                                Constants.setEPNId("")
                            }
                            if (it.authroizedNumber != null) {
                                Constants.setLastAuthorizedNumber(it.authroizedNumber)
                            }
                            Constants.setAllCryptoCurrencies(it.allCryptoCurrencies)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        doPasswordAuth()
                    }
                } ?: run {
                    if (response.body() == null) {
                        UserInterface.hideProgress(context!!)
                        if (response.code() === 401 || response.code() === 402 || response.code() === 403) {
                            if (response.code() === 403) {
                                UserInterface.showOKTitleAlert(activity,
                                    "Alert",
                                    "Due to unusual activity, your account is blocked. Please email us at support@pesa-support.zendesk.com",
                                    true,
                                    DialogInterface.OnClickListener { dialog, which -> activity!!.finish() })
                            } else {
                                UserInterface.unAuthorized(context!!, response.code())
                            }
                        } else {
                            Log.d("ResponseDeviceStatus", response.message() + " erro")
                            UserInterface.showSnack(response.message(), false, view)
                        }
                    }
                }
            }

        })

    }

    private fun doPasswordAuth() {
        UserInterface.showProgress("Signing in..", activity)
        val params = JSONObject()
        try {
            params.put("walletAddress", Constants.getAccountAddress())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<CredentialStatus>? =
            ApiClient.build()?.loginDevice(body, Constants.getHeader(context!!)!!)
        call?.enqueue(object : Callback<CredentialStatus> {
            override fun onFailure(call: Call<CredentialStatus>, t: Throwable) {
                UserInterface.hideProgress(activity!!)
            }

            override fun onResponse(
                call: Call<CredentialStatus>,
                response: Response<CredentialStatus>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(context!!)
                    if (response.isSuccessful) {
                        val editor = PreferenceManager.getDefaultSharedPreferences(activity).edit()
                        editor.putString(
                            Constants.PREFS_PASS,
                            pwdText!!.text!!.toString()
                        ).apply()
                        editor.apply()
                        try {
                            var basicDetailsService = FetchDataBackgroungService()
                            basicDetailsService.loadFirstAllMarkets(
                                1,
                                context!!
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Constants.setScreenPwd(context!!, cfmPwdText?.text.toString().trim())
                        PesaApplication.getChildFragmentManager().beginTransaction().replace(
                            R.id.shareg_main_container,
                            AccountConfigurationSuccessFragment.newInstance(false)
                        ).commitAllowingStateLoss()
                    }
                } ?: run {
                    if (response.body() == null) {
                        UserInterface.hideProgress(context!!)
                        if (response.code() === 401 || response.code() === 402 || response.code() === 403) {
                            UserInterface.showOKTitleAlert(
                                context,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> activity!!.finish() })
                        } else {
                            UserInterface.showOKTitleAlert(
                                context,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> })
                        }
                    }
                }
            }

        })

    }


    private fun getDeviceInfo(): RequestBody? {
        var requestBody: RequestBody? = null
        try {
            val telephonyManager =
                activity!!.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val operatorName = telephonyManager.networkOperatorName
            val params = JSONObject()
            try {
                params.put("carrier", operatorName)
                params.put("model", Build.MODEL)
                params.put("manufacturer", Build.MANUFACTURER)
                params.put("version", Build.VERSION.RELEASE)
                params.put("currentAppVersionUsed", Constants.getAppVersion(context!!))
                params.put("os", "Android")
                params.put(
                    "installationId",
                    ParseInstallation.getCurrentInstallation().installationId
                )
                params.put("isInAppPurchase", true)
            } catch (e: JSONException) {
                e.printStackTrace()
            }


            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            requestBody = params.toString().toRequestBody(JSON)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return requestBody
    }

    private fun getDeviceStatus() {
        UserInterface.showProgress("Creating Account...", activity)
        var call: Call<DeviceStatus>? =
            ApiClient.build()
                ?.getDeviceStatus(getDeviceInfo()!!, Constants.getHeader(context!!)!!)
        call?.enqueue(object : Callback<DeviceStatus> {
            override fun onFailure(call: Call<DeviceStatus>, t: Throwable) {
                UserInterface.hideProgress(context!!)
                UserInterface.showSnack(
                    UserInterface.errorMessage(t.message.toString()),
                    false,
                    view
                )
            }

            override fun onResponse(
                call: Call<DeviceStatus>,
                response: Response<DeviceStatus>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        if (it.userName != null)
                            Constants.setUserName(context!!, it.userName)
                        if (it.deviceId != null) {
                            Constants.setDeviceId(it.deviceId, context!!)
                        }
                        /*try {
                            Constants.setMintFromList(it.MINT_BUY_FROM_TOKEN)
                            Constants.setMintToList(it.MINT_BUY_TO_TOKEN)
                        }catch (e:Exception){
                            e.printStackTrace()
                        }*/
                        try {
                            Constants.setEncryptionKeyDatas(
                                it.CIPHER_ALGO,
                                it.CIPHER_SALT,
                                it.ENCRYPTION_KEY,
                                it.BUFFEFR_KEY,
                                context!!, it.JWT_TOKEN_SECRET!!
                            )
                            Constants.setSupportEmailId(it.SUPPORT_EMAIL, context)
                            Constants.setAllCryptoCurrencies(it.allCryptoCurrencies)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        Constants.setIsEPNEnabled(false)
                        Constants.setEPNNumber("")
                        Constants.setEPNId("")
                        register()
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (response.code() === 401 || response.code() === 402 || response.code() === 403) {
                            UserInterface.hideProgress(context!!)
                            UserInterface.showOKTitleAlert(activity,
                                "Alert",
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ),
                                true,
                                DialogInterface.OnClickListener { dialog, which -> activity!!.finish() })
                        } else {
                            UserInterface.hideProgress(context!!)
                            UserInterface.showSnack(
                                UserInterface.errorMessage(
                                    response.message(),
                                    response.errorBody()
                                ), false, view
                            )
                        }
                    }
                }
            }

        })

    }


}