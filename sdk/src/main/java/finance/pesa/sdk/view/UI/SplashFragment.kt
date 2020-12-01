package finance.pesa.sdk.view.UI

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.AvailableDevice
import finance.pesa.sdk.Model.DeviceStatus
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.ConnectionUtils
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.FetchDataBackgroungService
import finance.pesa.sdk.utils.UserInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.fixedRateTimer


class SplashFragment : Fragment() {

    // App Force Update Dialog
    internal var forceUpdateDialog: Dialog? = null
    private var keyWord = ""

    companion object {
        fun newInstance(keyWord: String): SplashFragment {
            val fagment = SplashFragment()
            fagment.keyWord = keyWord
            return fagment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.splash, container, false)
        PesaApplication.setEnableLock(false)
        UserInterface.changeStatusBar(activity!!, R.color.app_green_black)
        UserInterface.changeNavigationBar(activity!!, R.color.app_green_black)
        return view
    }

    override fun onResume() {
        super.onResume()
        if (!isForceUpdateDialogShowing())
            onServiceLoad()
    }


    private fun onServiceLoad() {
        if (activity == null)
            return
        if (ConnectionUtils.isConnectedToInternet(activity)) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            val previouslyStarted = prefs.getBoolean(Constants.PREFS_RUN_FIRST, false)
            if (!previouslyStarted) {
                PesaApplication.getChildFragmentManager().beginTransaction()
                    .replace(R.id.shareg_main_container, WelcomeFragment())
                    .commitAllowingStateLoss()
            } else {
                val mobNo = prefs.getString(Constants.PARSE_AUTH_PHONE, null)
                when {
                    mobNo == null -> PesaApplication.getChildFragmentManager().beginTransaction().replace(
                        R.id.shareg_main_container,
                        EnterMobileNumber()
                    ).commitAllowingStateLoss()
                    else -> getDeviceStatus(mobNo)
                }
            }
        }
    }


    private fun userValidation(mobNo: String) {
        if (activity == null)
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
                                if (activity == null)
                                    return
                                fragmentManager!!.beginTransaction().replace(
                                    R.id.shareg_main_container,
                                    WelcomeImportWallet.newInstance(mobNo)
                                ).commitAllowingStateLoss()
                            } else {
                                if (activity == null)
                                    return
                                fragmentManager!!.beginTransaction().replace(
                                    R.id.shareg_main_container,
                                    WelcomeCreateWallet.newInstance(mobNo)
                                ).commitAllowingStateLoss()
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if ((response.code() === 401 || response.code() === 402 || response.code() === 403) && response.message().contentEquals(
                                "Unauthorized"
                            )
                        ) {
                            onResume()
                        } else if (response.code() === 401 || response.code() === 402 || response.code() === 403) {
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
                        } else {

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
                /* params.put(
                     "installationId",
                     ParseInstallation.getCurrentInstallation().installationId
                 )*/
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

    fun getDeviceStatus(mobNo: String) {
        PesaApplication.setUserPhNo(mobNo)
        var call: Call<DeviceStatus>? =
            ApiClient.build()
                ?.getDeviceStatus(getDeviceInfo()!!, Constants.getHeader(context!!)!!)
        call?.enqueue(object : Callback<DeviceStatus> {
            override fun onFailure(call: Call<DeviceStatus>, t: Throwable) {
                if (t.message!!.contains("Invalid Keys")) {
                    val pref = PreferenceManager.getDefaultSharedPreferences(activity)
                    val ed = pref.edit()
                    ed.putString(Constants.PREF_UNIQUE_ID, null)
                    ed.apply()
                    userValidation(mobNo)
                } else if (t.message!!.contains("Invalid session token")) {
                    if (context != null)
                        userValidation(mobNo)
                } else {
                    if (context != null)
                        getDeviceStatus(mobNo)
                }
            }

            override fun onResponse(
                call: Call<DeviceStatus>,
                response: Response<DeviceStatus>
            ) {
                response?.body()?.let {
                    if (response.isSuccessful) {
                        try {
                            if (it.app_update_required_in_android) {
                                try {
                                    showForceUpdateDialog(it.msg_in_android)
                                    return
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
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
                                context!!,
                                it.JWT_TOKEN_SECRET!!
                            )
                            Constants.setSupportEmailId(it.SUPPORT_EMAIL, context)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        try {
                            if (it.authroizedNumber != null) {
                                Constants.setLastAuthorizedNumber(it.authroizedNumber)
                            }
                            Constants.setAllCryptoCurrencies(it.allCryptoCurrencies)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        handleDeviceResponse(it)
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (response.message().contains("Invalid Keys") || response.code() === 400 || response.code() === 401 || response.code() === 402 || response.code() === 403) {
                            val pref =
                                PreferenceManager.getDefaultSharedPreferences(activity)
                            val ed = pref.edit()
                            ed.putString(Constants.PREF_UNIQUE_ID, null)
                            ed.apply()
                            userValidation(mobNo)
                        } else if (response.message().contains("Invalid session token")) {
                            if (context != null)
                                userValidation(mobNo)
                        } else {
                            if (context != null)
                                getDeviceStatus(mobNo)
                        }
                    }
                }
            }

        })

    }

    private fun handleDeviceResponse(deviceStatus: DeviceStatus) {
        try {
            when (deviceStatus.deviceStatus) {
                101 -> {
                    if (context == null)
                        return
                    val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
                    val eds = prefs.edit()
                    eds.putString(Constants.PREF_UNIQUE_ID, null)
                    eds.apply()

                    if (deviceStatus.isRegistered) {
                        Constants.setLastWalletAddress(deviceStatus.walletAddress)
                        Constants.setIsEPNEnabled(deviceStatus.isEPNEnabled)
                        if (deviceStatus.isEPNEnabled) {
                            Constants.setEPNNumber(deviceStatus.EPNNumber)
                            Constants.setEPNId(deviceStatus.EPN_ID)
                        } else {
                            Constants.setEPNNumber("")
                            Constants.setEPNId("")
                        }
                        PesaApplication.getChildFragmentManager()!!.beginTransaction().replace(
                            R.id.shareg_main_container,
                            WelcomeImportWallet.newInstance(deviceStatus.registeredNumber)
                        ).commitAllowingStateLoss()
                    } else {
                        PesaApplication.getChildFragmentManager()!!.beginTransaction().replace(
                            R.id.shareg_main_container,
                            WelcomeCreateWallet.newInstance(deviceStatus.registeredNumber)
                        ).commitAllowingStateLoss()
                    }
                }
                102 -> {
                    if (deviceStatus.userName != null)
                        Constants.setUserName(context!!, deviceStatus.userName)
                    if (deviceStatus.deviceId != null) {
                        Constants.setDeviceId(deviceStatus.deviceId, context!!)
                    }
                    Constants.setLastWalletAddress(deviceStatus.walletAddress)
                    Constants.setIsEPNEnabled(deviceStatus.isEPNEnabled)
                    if (deviceStatus.isEPNEnabled) {
                        Constants.setEPNNumber(deviceStatus.EPNNumber)
                        Constants.setEPNId(deviceStatus.EPN_ID)
                    } else {
                        Constants.setEPNNumber("")
                        Constants.setEPNId("")
                    }

                    if (!PesaApplication.getIsLockShowing())
                        try {
                            PesaApplication.setIsLockShowing(true)
                            val intent = Intent(activity, LockScreen::class.java)
                            startActivityForResult(intent, Constants.LOCK_REQUEST_CODE)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                }
                100 -> {
                }
                else -> {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun isForceUpdateDialogShowing(): Boolean {
        try {
            //Force Update Dialog
            if (forceUpdateDialog != null)
                if (forceUpdateDialog?.isShowing!!) {
                    try {
                        forceUpdateDialog?.dismiss()
                        return false
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    return true
                }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    //Show App Force Update Dialog
    private fun showForceUpdateDialog(msgValue: String) {
        if (isForceUpdateDialogShowing())
            return
        forceUpdateDialog = Dialog(activity!!, R.style.CustomDialog)
        forceUpdateDialog?.setContentView(R.layout.app_update_force)
        val d = ColorDrawable(Color.TRANSPARENT)
        forceUpdateDialog?.window!!.setBackgroundDrawable(d)
        forceUpdateDialog?.setCancelable(false)
        val msg = forceUpdateDialog?.findViewById<TextView>(R.id.msg)
        val install = forceUpdateDialog?.findViewById<Button>(R.id.install)
        msg?.text = msgValue

        install?.setOnClickListener { redirectPlayStore() }
        forceUpdateDialog?.show()
    }

    private fun redirectPlayStore() {
        if (context == null)
            return
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.packageName)
                )
            )
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + context!!.packageName)
                )
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.LOCK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val isSuccess = data?.getStringExtra(Constants.LOCK_CODE_RESULT)
            if (isSuccess != null) {
                try {
                    if (isSuccess == "success") {
                        try {
                            var basicDetailsService = FetchDataBackgroungService()
                            basicDetailsService.loadFirstAllMarkets(
                                1,
                                context!!
                            )

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        var selectedTab=1
                        if(keyWord=="isNavigateToActivity")
                            selectedTab=2
                        PesaApplication.getChildFragmentManager()!!.beginTransaction().replace(
                            R.id.shareg_main_container,
                            DashboardFragment.newInstance(selectedTab)
                        ).commitAllowingStateLoss()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

}