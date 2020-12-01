package finance.pesa.sdk

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.preference.PreferenceManager
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.parse.Parse
import com.parse.ParseInstallation
import com.parse.ParseObject
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.UI.LockScreen
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.security.Security
import java.security.Security.insertProviderAt
import java.security.Security.removeProvider
import java.util.*


open class PesaApplication : Application(), LifecycleObserver {

    companion object {
        var currentActivityContext: Context? = null
        private var childFragmentManager: FragmentManager? = null
        private var userPhNo: String = ""
        private var parseToken: String = ""
        private var uniqueID: String? = ""
        private var regToken: String? = ""
        private var isLockEnable: Boolean? = false
        private var isLockShowing: Boolean? = false
        private var isAppInBackground: Boolean? = true
        private var prefs: SharedPreferences? = null
        private var context: Context? = null
        private var web3j: Web3j? = null
        private var googleApiClient: GoogleApiClient? = null
        var currentActivity: Activity? = null


        fun getContext(): Context {
            return context!!
        }

        fun getAppIsBackground(): Boolean {
            return isAppInBackground!!
        }

        fun getRegToken(): String {
            try {
                if (regToken == "") {
                    val pref = context!!.getSharedPreferences("firebase", 0)
                    val regId = pref.getString("regId", "")
                    this.regToken = regId
                    return regToken!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return regToken!!
        }

        fun setEnableLock(isEnable: Boolean) {
            this.isLockEnable = isEnable
        }

        fun setIsLockShowing(isLockShowing: Boolean) {
            this.isLockShowing = isLockShowing
        }

        fun getEnableLock(): Boolean {
            return isLockEnable!!
        }

        fun getIsLockShowing(): Boolean {
            return isLockShowing!!
        }

        fun getWeb3j(): Web3j {
            return web3j!!
        }

        fun getUserPhNo(): String {
            return if (userPhNo.isEmpty())
                prefs!!.getString(Constants.PARSE_AUTH_PHONE, "")!!
            else
                userPhNo
        }


        fun setUserPhNo(userPh: String) {
            /* try {
                 if (userPhNo.isEmpty())
                     getCrashlytics()!!.setUserId(userPh)
             } catch (e: Exception) {
                 e.printStackTrace()
             }*/
            this.userPhNo = userPh
        }

        fun getUserCountryCode(context: Context): String? {
            return if (userPhNo.isEmpty()) {
                UserInterface.getCountryCode(
                    prefs!!.getString(
                        Constants.PARSE_AUTH_PHONE,
                        ""
                    )!!, context
                )
            } else {
                UserInterface.getCountryCode(userPhNo, context)
            }
        }

        fun setParseToken(token: String) {
            this.parseToken = token
        }

        /*fun setUserPhNo(userPh: String) {
            try {
                if (userPhNo.isEmpty())
                    getCrashlytics()!!.setUserId(userPh)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            this.userPhNo = userPh
        }*/

        fun getChildFragmentManager(): FragmentManager {
            return childFragmentManager!!
        }

        fun setChildFragmentManager(childFragmentManager: FragmentManager) {
            this.childFragmentManager = childFragmentManager
        }

        fun getGoogleApiClient(): GoogleApiClient {
            return googleApiClient!!
        }

        fun isGoogleApiClient(): Boolean {
            if (googleApiClient == null)
                return false
            return true
        }

        fun setGoogleApiClient(googleApiClient: GoogleApiClient) {
            this.googleApiClient = googleApiClient
        }


        @Synchronized
        fun getUniqueID(context: Context): String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            uniqueID = prefs.getString(Constants.PREF_UNIQUE_ID, null)
            if (uniqueID == null) {
                uniqueID = UUID.randomUUID().toString()
                val editor = prefs.edit()
                editor.putString(Constants.PREF_UNIQUE_ID, uniqueID)
                editor.commit()
                addUniqueIDtoParseIntallation()
            }
            return uniqueID!!
        }


        private fun addUniqueIDtoParseIntallation() {
            val parseInstallation = ParseInstallation.getCurrentInstallation()
            parseInstallation.put("UUID", uniqueID!!)
            parseInstallation.saveInBackground()
        }


    }

    override fun onCreate() {
        super.onCreate()
        context = this
        setupBouncyCastle()
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId(Constants.PARSE_APP_IDS)
                .clientKey(Constants.PARSE_CLIENT_KEY)
                .server(Constants.PARSE_SERVER_KEY)
                .build()
        )

        web3j = Web3j.build(HttpService(Constants.kovan_url))
        /* ParseObject.registerSubclass(finance.pesa.sdk.Model.ConversationData::class.java)
         ParseObject.registerSubclass(finance.pesa.sdk.Model.ScreenSMS::class.java)*/
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        insertProviderAt(BouncyCastleProvider(), 1)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        isAppInBackground=true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        isAppInBackground=false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun disconnectListener() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun connectListener() {
        /* if (isLockEnable!! && !isLockShowing!!)
             try {
                 val lockIntent = Intent(context, LockScreen::class.java)
                 lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                 context?.startActivity(lockIntent)
             } catch (e: Exception) {
                 e.printStackTrace()
             }*/
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onAppOnDestroy() {
        setEnableLock(false)
    }

}


