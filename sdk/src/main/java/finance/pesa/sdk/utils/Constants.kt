package finance.pesa.sdk.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.preference.PreferenceManager
import finance.pesa.sdk.BuildConfig
import finance.pesa.sdk.Model.*
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.view.Interface.ActivitiesUpdateListener
import finance.pesa.sdk.view.Interface.EPNActivationListener
import finance.pesa.sdk.view.Interface.ETHBalanceUpdatedListener
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.web3j.crypto.Bip32ECKeyPair
import org.web3j.crypto.Bip32ECKeyPair.HARDENED_BIT
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.MnemonicUtils
import org.web3j.utils.Numeric
import java.util.*
import kotlin.collections.HashMap


object Constants {


    const val BASE_URL: String = BuildConfig.BASE_URL
    const val TOKEN_SERVER_URL: String = BuildConfig.TOKEN_SERVER_URL
    const val X_Authy_API_Key: String = BuildConfig.X_Authy_API_Key
    const val PARSE_APP_IDS = BuildConfig.PARSE_APP_IDS
    const val PARSE_CLIENT_KEY = BuildConfig.PARSE_CLIENT_KEY
    const val PARSE_SERVER_KEY = BuildConfig.PARSE_SERVER_KEY
    const val SEGMENT_TOKEN = BuildConfig.SEGMENT_TOKEN
    const val BUILD_BASE = BuildConfig.BUILD_BASE
    const val PACKAGE_NAME = BuildConfig.PACKAGE_NAME
    const val pControllerAddress = BuildConfig.pControllerAddress
    const val EPNAddress = BuildConfig.EPNAddress
    const val PAYMENT_Address = BuildConfig.PAYMENT_Address
    const val allowanceBalanceLimit = BuildConfig.allowanceBalanceLimit
    const val kovan_url = BuildConfig.kovan_url
    const val kovan_transaction_url = BuildConfig.kovan_transaction_url


    const val CONVERSION_URL = "https://min-api.cryptocompare.com"
    const val WEB_PORTAL_URL = "https://joinhoom.com/"
    const val KEY_INTENT_READ = "keyintentREAD"
    const val KEY_CALL_END = "key_call_end"
    const val KEY_CALL_INCOMING = "key_call_incoming"
    const val KEY_CALL_ANSWER = "key_call_answer"
    const val KEY_INTENT_HELP = "keyintenthelp"
    const val KEY_INTENT_SEND = "keyintentsend"
    const val CONVERSATIONS_ID = "conversations_id"
    const val FROM_NUMBER = "from_number"
    const val SENDER_ID = "sender_id"
    const val RECEIVER_ID = "receiver_id"
    const val REQUEST_CODE_MORE = 100
    const val REQUEST_CODE_HELP = 101
    const val REQUEST_CODE_OPEN = 108
    const val REQUEST_CODE_ANSWER = 158
    const val REQUEST_CODE_END = 159
    const val REQUEST_CODE_INCOMING = 160
    const val Minimum_EPN_Activation_Charge: Double = 25.0
    const val NOTIFICATION_REPLY = "NotificationReply"
    const val CHANNNEL_ID = "SimplifiedCodingChannel"
    const val JWT_TOKEN = "JWT_token"
    const val TO_USER_NUMBER = "toUserNumber"
    const val CONTACT_NUMBER = "contact_number"
    const val CONTACT_NAME = "contact_name"
    const val CONTACT_URI = "contact_uri"
    const val RATE_COUNT = "rate_count"
    const val CALL_RATE_COUNT = "call_rate_count"
    const val MESSAGE_RATE_COUNT = "message_rate_count"
    const val IS_RATED = "is_rate"
    const val IS_RATTING_SHOW = "is_ratting_show"
    const val ACTION_TEXT = "action_text"
    const val BYTE_MULTIPLIER: Long = 1048576

    const val PESA_TOKEN: String = "5000000000"
    const val ETH_UnderlyingAddress: String = "0x0000000000000000000000000000000000000000"
    const val ETH_UnderlyingSymbol: String = "ETH"
    const val PARSE_AUTH_PHONE: String = "parse_auht_phone"
    const val LAST_AUTH_PHONE: String = "last_auht_phone"
    const val PREFS_RUN_FIRST: String = "run_first"
    const val STRIPE_PRIVATE_KEY = "stripe_private_key"
    const val SCREEN_LOCK = "screen_lock"
    const val HOOM_USER_NAME = "hoom_user_name"
    const val HOOM_USER_EMAIL = "hoom_user_email"
    const val HOOM_INITIAL_POPUP = "hoom_initial_popup"
    const val APP_ID = "AmlcvyHyIef352twQYzdUzt2"
    const val APP_SECRET = "BDbzRwipqOddZGcFTngDD577DLvaExq8"
    const val PREF_UNIQUE_ID = "unique_id"
    const val PREFS_PASS = "pass"
    const val TO_USER_ID = "to_user_id"
    const val FROM_USER_ID = "from_user_id"
    const val CONVERSATION_ID = "conversation_id"
    const val SUBSCRIPTION_PACK_NEW = "HOOM2PN20202NEW"
    const val SUBSCRIPTION_PACK = "HOOM2PN20202"
    const val BUY_CREDITS_PACK = "PREPAID2020MAY"
    const val HOOM_DEFAULT_NUMBER = "hoom_default_number"
    const val AVAILABLE_CREDITS = "available_credits"
    const val COUNTRY_RATE_VERSION = "country_rate_version"
    const val ALL = "all"
    const val SENT = "sent"
    const val RECEIVED = "received"
    const val REQUEST = "request"
    const val ESCROW = "escrow"
    const val NOTIFICATION_PENDING = "notification_pending"
    lateinit var CHAT_TO_NUMBER: String
    const val NOTIFICATION_ID: Int = 200
    const val YEAR_SECONDS: Int = 31556952
    const val CALL_NOTIFICATION_ID: Int = 250
    var callingScreen: Boolean? = false
    private var didInit: Boolean = false
    private var branchLink: String? = null
    private var passCode: String? = null
    private var stripePrivateKey: String? = null
    private var userName: String? = null
    private var userEmail: String? = null
    private var defaultNumber: String = ""
    private var mintFrom: List<String> = emptyList()
    private var mintTo: List<String> = emptyList()
    private var acivatedNumbers: List<ActiveNumbers> = emptyList()
    private var allContactNumbers: List<ContactDetails> = emptyList()
    private var contactUpdatedListeners: ArrayList<ContactUpdatedListener>? = arrayListOf()
    private var activitiesUpdateListeners: ArrayList<ActivitiesUpdateListener>? = arrayListOf()
    private var allLocalContactNumbers: HashMap<String, LocalContact?> = hashMapOf()
    private var availableCredits: Double = 0.0
    private var encryptedKey: String? = null
    private var cipher_algo: String? = null
    private var cipher_salt: String? = null
    private var bufferKey: String? = null
    private var JWTTokenSecret: String? = ""
    private var credentials: Credentials? = null
    private var allMarkets: List<InvestData>? = emptyList()
    /*private var overAllSupplied: Double = 0.0
    private var overAllBorrowed: Double = 0.0
    private var borrowedLimit: Double = 0.0
    private var borrowedInPercentage: Double = 0.0
    private var netAPY: Double = 0.0
    private var supplyBarValue: Double = 0.0
    private var borrowBarValue: Double = 0.0*/
    private var investDash: InvestDash? = null
    private var bannerShow: Boolean? = false
    private var isTrial: Boolean? = false
    private var activatedISO: String? = null
    private var activatedDialCode: String? = null
    private var noCreditsTitle: String? = null
    private var noCreditsMessage: String? = null
    private var noCreditsAction: String? = null
    private var deviceId: String? = null
    private var currentStage: Int? = 0
    private var ethTousd: Double? = 0.0
    private var allCryptoCurrencies: List<CryptoCurrenciesValue>? = arrayListOf()
    private var verifyToken = ""
    private var isLinkedPackAvailable: Boolean? = false
    private var maximumNumbers: Int? = 3
    private var supportEmailId: String? = null
    private var walletAddress: String? = ""
    private var authorizedNumber: String? = null
    private var isEPNEnabled: Boolean? = false
    private var EPNNumber: String? = ""
    private var EPNId: String? = ""
    private var refreshWalletListeners: ArrayList<RefreshWalletListener?> = arrayListOf()
    private var epnActivationListener: EPNActivationListener? = null
    private var ethBalanceUpdatedListener: ETHBalanceUpdatedListener? = null
    private var marketValues: HashMap<String, MarketValues>? = hashMapOf()
    var isLoadUnScreen: Boolean? = false
    const val CALL_SID_KEY = "CALL_SID"
    const val SUPPORT_MAIL = "support_email"
    const val PRIVATE_KEY = "private_key"
    const val VOICE_CHANNEL_LOW_IMPORTANCE = "notification-channel-low-importance"
    const val VOICE_CHANNEL_HIGH_IMPORTANCE = "notification-channel-high-importance"
    const val INCOMING_CALL_INVITE = "INCOMING_CALL_INVITE"
    const val CANCELLED_CALL_INVITE = "CANCELLED_CALL_INVITE"
    const val INCOMING_CALL_NOTIFICATION_ID = "INCOMING_CALL_NOTIFICATION_ID"
    const val ACTION_ACCEPT = "ACTION_ACCEPT"
    const val ACTION_REJECT = "ACTION_REJECT"
    const val ACTION_INCOMING_CALL_NOTIFICATION = "ACTION_INCOMING_CALL_NOTIFICATION"
    const val ACTION_INCOMING_CALL = "ACTION_INCOMING_CALL"
    const val ACTION_CANCEL_CALL = "ACTION_CANCEL_CALL"
    const val ACTION_FCM_TOKEN = "ACTION_FCM_TOKEN"
    const val CALL_DIALLING = "call_dialing"
    const val CALL_ANSWER = "call_answer"
    const val CALL_DECLINE = "call_decline"
    const val CALL_INCOMING = "call_incoming"
    const val TO_CALLER_NUMBER = "to_caller_number"
    const val FROM_CALLER_NUMBER = "from_caller_number"
    const val DEVICE_ID = "deviceId"
    const val OUT_GOING_CALL_RATE = "out_going_call_rate"
    const val INCOMING_CALL_RATE = "incoming_call_rate"
    const val MSG_CALL_RATE = "msg_call_rate"
    const val CURRENT_STAGE = "current_stage"
    const val CALL_END_BY_USER = "user"
    const val CALL_END_BY_APP = "app"
    const val CIPHER_ALGO = "CIPHER_ALGO"
    const val CIPHER_SALT = "CIPHER_SALT"
    const val ENCRYPTION_KEY = "ENCRYPTION_KEY"
    const val BUFFER_KEY = "BUFFER_KEY"
    const val JWT_TOKEN_SECRET = "JWT_TOKEN_SECRET"
    const val QR_CODE_RESULT = "qr_code_result"
    const val LOCK_CODE_RESULT = "lock_code_result"
    const val QR_CODE_REQUEST_CODE = 1001
    const val LOCK_REQUEST_CODE = 1002
    const val CONTACT_REQUEST_CODE = 1003
    //Referral Code
    const val BRANCH_REFERRAL_CODE = "branch_referral_code"
    const val REFERRAL_CODE = "referral_code"
    const val REFERRAL_LINK = "referral_link"
    const val REFERRAL_MESSAGE = "referral_message"
    const val REFERRAL_CREDITS = "referral_credits"
    const val REFERRAL_DESCRIPTION = "referral_description"
    const val IS_NAVIGATE_Activity = "isNavigateToActivity"

    // Mixpanel properties Key
    const val BRANCH_CHANNEL = "branch_channel"
    const val BRANCH_CAMPAIGN = "branch_campaign"
    const val BRANCH_FEATURE = "branch_feature"
    const val BRANCH_TAGS = "branch_Tags"
    const val BRANCH_EMAIL = "branch_email"

    fun setMintFromList(mintFrom: List<String>) {
        this.mintFrom = mintFrom
    }

    fun setMintToList(mintTo: List<String>) {
        this.mintTo = mintTo
    }

    fun setIsEPNEnabled(isEPNEnabled: Boolean) {
        this.isEPNEnabled = isEPNEnabled
    }

    fun setEPNNumber(EPNNumber: String) {
        this.EPNNumber = EPNNumber
    }

    fun getisEPNEnabled(): Boolean {
        return isEPNEnabled!!
    }

    fun setEPNId(EPNId: String) {
        this.EPNId = EPNId
    }


    fun getEPNId(): String {
        return EPNId!!
    }


    fun getEPNNumber(): String {
        return EPNNumber!!
    }

    fun setLastWalletAddress(walletAddress: String) {
        this.walletAddress = walletAddress
    }

    fun getLastWalletAddress(): String {
        return walletAddress!!
    }

    fun setLastAuthorizedNumber(authorizedNumber: String) {
        this.authorizedNumber = if (authorizedNumber.contains("+"))
            authorizedNumber
        else
            "+" + authorizedNumber
    }

    fun getLastAuthorizedNumber(): String {
        return if (authorizedNumber == null)
            PesaApplication.getUserPhNo()
        else
            authorizedNumber!!
    }

    fun getMintFrom(): List<String> {
        mintFrom = emptyList()
        mintFrom += "USDC"
        mintFrom += "USDT"
        return mintFrom
    }

    fun getMintTo(): List<String> {
        mintTo = emptyList()
        mintTo += "PUSD"
        return mintTo
    }

    fun setInvestDashboardValue(investDash: InvestDash) {
        this.investDash = investDash
    }

    fun getInvestDashboardValue(): InvestDash? {
        return this.investDash
    }


    fun setRefreshWalletListener(listener: RefreshWalletListener?) {
        this.refreshWalletListeners!!.add(listener)
    }

    fun getRefreshWalletListeners(): ArrayList<RefreshWalletListener?> {
        return refreshWalletListeners
    }

    fun setEPNActivationListener(listener: EPNActivationListener?) {
        this.epnActivationListener = listener
    }

    fun getEPNActivationListener(): EPNActivationListener? {
        return epnActivationListener
    }

    fun setETHBalanceUpdatedListener(listener: ETHBalanceUpdatedListener?) {
        this.ethBalanceUpdatedListener = listener
    }

    fun getETHBalanceUpdatedListener(): ETHBalanceUpdatedListener? {
        return ethBalanceUpdatedListener
    }

    fun setMarketValues(marketValues: HashMap<String, MarketValues>) {
        this.marketValues = marketValues
    }

    fun getMarketValues(): HashMap<String, MarketValues>? {
        return marketValues
    }


    fun setAllMarkets(allMarketData: List<InvestData>?) {
        this.allMarkets = allMarketData
    }

    fun getAllMarkets(): List<InvestData>? {
        return allMarkets
    }

    fun setCredentials(credentials: Credentials, context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(
            PRIVATE_KEY,
            Numeric.toHexStringNoPrefix(credentials?.ecKeyPair.privateKey)
        )
        editor.commit()
        this.credentials = credentials
    }

    fun getCredentials() {
        try {
            val masterKeypair =
                Bip32ECKeyPair.generateKeyPair(
                    MnemonicUtils.generateSeed(
                        "bridge early rebuild over fox firm tackle ability divide birth path company",
                        null
                    )
                )
            val path = intArrayOf(44 or HARDENED_BIT, 60 or HARDENED_BIT, 0 or HARDENED_BIT, 0, 0)
            val x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)
            val credentials = Credentials.create(x)
            System.out.println(credentials.address)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getAccountCredential(): Credentials? {
        try {
            if (credentials == null)
                return getCredentialFromKey()
            return credentials
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getAccountAddress(): String {
        try {
            if (credentials == null)
                return Keys.toChecksumAddress(getCredentialFromKey()!!.address)
            return Keys.toChecksumAddress(credentials!!.address)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun setPrivateKey(privateKey: String, context: Context): Boolean {
        try {
            val newCredentials = Credentials.create(privateKey)
            if (newCredentials != null) {
                this.credentials = newCredentials
                val prefs = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = prefs.edit()
                editor.putString(PRIVATE_KEY, privateKey)
                editor.apply()
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getCredentialFromKey(): Credentials? {
        try {
            val pref = PreferenceManager.getDefaultSharedPreferences(PesaApplication.getContext())
            val privateKey = pref.getString(PRIVATE_KEY, "")!!
            return Credentials.create(privateKey)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun setSupportEmailId(emailId: String?, context: Context?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(SUPPORT_MAIL, emailId)
        editor.commit()
        this.supportEmailId = emailId
    }

    fun setIsPendingNotification(isPending: Boolean?, context: Context?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(NOTIFICATION_PENDING, isPending!!)
        editor.commit()
    }

    fun getIsPendingNotification(mCtx: Context): Boolean? {
        try {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getBoolean(NOTIFICATION_PENDING, false)!!
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun getSupportEmailId(mCtx: Context): String? {
        if (supportEmailId == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(SUPPORT_MAIL, "support@pesa-support.zendesk.com")!!
        }
        return supportEmailId!!
    }

    fun setDeviceId(deviceId: String?, context: Context) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(DEVICE_ID, deviceId)
        editor.commit()
        this.deviceId = deviceId
    }

    fun getDeviceIdFromLocal(mCtx: Context): String? {
        if (deviceId == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(DEVICE_ID, null)!!
        }
        return deviceId!!
    }


    fun getDeviceId(): String? {
        return deviceId!!
    }


    fun setIsLinkedPackAvailable(isLinkedPackAvailable: Boolean) {
        this.isLinkedPackAvailable = isLinkedPackAvailable
    }

    fun getIsLinkedPackAvailable(): Boolean {
        return isLinkedPackAvailable!!
    }

    fun setMaximumNumbers(maximumNumbers: Int) {
        this.maximumNumbers = maximumNumbers
    }

    fun getMaximumNumbers(): Int {
        return maximumNumbers!!
    }

    fun addActivatedNumber(activeNumber: ActiveNumbers) {
        this.acivatedNumbers += activeNumber
    }

    fun getActivatedNumber(): List<ActiveNumbers>? {
        return acivatedNumbers
    }

    fun setEncryptionKeyDatas(
        cipher_algo: String,
        cipher_salt: String,
        encryption_key: String,
        buffer_key: String,
        mCtx: Context,
        JWTTokenSecret: String
    ) {
        this.encryptedKey = encryption_key
        this.cipher_algo = cipher_algo
        this.cipher_salt = cipher_salt
        this.bufferKey = buffer_key
        this.JWTTokenSecret = JWTTokenSecret
        val ed = PreferenceManager.getDefaultSharedPreferences(mCtx).edit()
        ed.putString(CIPHER_ALGO, cipher_algo)
        ed.putString(CIPHER_SALT, cipher_salt)
        ed.putString(ENCRYPTION_KEY, encryption_key)
        ed.putString(BUFFER_KEY, buffer_key)
        ed.putString(JWT_TOKEN_SECRET, JWTTokenSecret)
        ed.apply()
    }


    fun getBufferKey(mCtx: Context): String {
        if (bufferKey == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(BUFFER_KEY, "")!!
        }
        return bufferKey!!
    }

    fun getEncryptedKey(mCtx: Context): String {
        if (encryptedKey == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(ENCRYPTION_KEY, "")!!
        }
        return encryptedKey!!
    }

    fun getCipherAlgo(mCtx: Context): String {
        if (cipher_algo == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(Constants.CIPHER_ALGO, "")!!
        }
        return cipher_algo!!
    }


    fun getCipherSalt(mCtx: Context): String {
        if (cipher_salt == null) {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(Constants.CIPHER_SALT, "")!!
        }
        return cipher_salt!!
    }


    fun setDefaultNumber(defaultNumber: String) {
        this.defaultNumber = defaultNumber
    }

    fun getDefaultNumber(): String? {
        return defaultNumber
    }

    fun getAllNumbers(): List<String>? {
        if (acivatedNumbers.isNotEmpty()) {
            var availableNumbers: List<String>? = emptyList()
            if (availableNumbers != null) {
                for (numbers in acivatedNumbers) {
                    availableNumbers += (numbers.number)
                }
            }
            return availableNumbers
        }
        return emptyList()
    }

    private fun getJWTSecretKey(mCtx: Context): String {
        if (JWTTokenSecret == "") {
            val pref = PreferenceManager.getDefaultSharedPreferences(mCtx)
            return pref.getString(JWT_TOKEN_SECRET, "")!!
        }
        return JWTTokenSecret!!
    }


    fun setInitialNumberPopup(context: Context, isSeen: Boolean) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(HOOM_INITIAL_POPUP, isSeen)
        editor.commit()
    }

    fun getInitialNumberPopup(context: Context): Boolean? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(HOOM_INITIAL_POPUP, false)!!
    }

    fun setUserEmail(context: Context, userEmail: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(HOOM_USER_EMAIL, userEmail)
        editor.commit()
        this.userEmail = userEmail
    }

    fun getUserEmail(context: Context): String? {
        if (userEmail == null) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(HOOM_USER_EMAIL, "")!!
        }
        return userEmail!!
    }

    fun setUserName(context: Context, userName: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(HOOM_USER_NAME, userName)
        editor.commit()
        this.userName = userName
    }

    fun getUserName(context: Context): String? {
        if (userName == null) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(HOOM_USER_NAME, "")!!
        }
        return userName!!
    }

    fun setStripePrivateKey(context: Context, privateKey: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(STRIPE_PRIVATE_KEY, privateKey)
        editor.commit()
        this.stripePrivateKey = privateKey
    }

    fun getStripePrivateKey(context: Context): String? {
        if (stripePrivateKey == null) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(STRIPE_PRIVATE_KEY, "")!!
        }
        return stripePrivateKey!!
    }

    fun setScreenPwd(context: Context, passCode: String?) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        editor.putString(SCREEN_LOCK, passCode)
        editor.commit()
        this.passCode = passCode
    }

    fun getScreenPwd(context: Context): String? {
        if (passCode == null) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            return prefs.getString(SCREEN_LOCK, "")!!
        }
        return passCode!!
    }


    fun getBranchLink(): String? {
        return branchLink
    }

    fun isBranchLinkAvailable(): Boolean? {
        return branchLink != null

    }

    fun setBranchLink(branchLink: String?) {
        Constants.branchLink = branchLink
    }

    fun setAllContact(allContactNumbers: List<ContactDetails>) {
        this.allContactNumbers = allContactNumbers
    }

    fun getAllContact(): List<ContactDetails>? {
        return allContactNumbers
    }

    fun setContactUpdatedListener(listener: ContactUpdatedListener) {
        this.contactUpdatedListeners!!.add(listener)
    }

    fun getContactUpdatedListeners(): ArrayList<ContactUpdatedListener>? {
        return contactUpdatedListeners!!
    }

    fun setActivitiesUpdateListener(listener: ActivitiesUpdateListener) {
        this.activitiesUpdateListeners!!.add(listener)
    }

    fun getActivitiesUpdateListeners(): ArrayList<ActivitiesUpdateListener>? {
        return activitiesUpdateListeners!!
    }

    fun setLocalContact(number: String, contact: LocalContact?) {
        this.allLocalContactNumbers[number] = contact
    }

    fun getAllLocalContact(): HashMap<String, LocalContact?>? {
        return allLocalContactNumbers
    }


    fun getAppVersion(context: Context): String? {
        var version = ""
        try {

            version = context.packageManager
                .getPackageInfo(context.packageName, 0).versionName

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    fun getHeader(context: Context?): HashMap<String, String>? {
        var headersMap: HashMap<String, String>? = HashMap()
        try {
            var headerKey = "pesamin "
            headerKey += "device_id=" + PesaApplication.getUniqueID(context!!) + " "
            headerKey += "app_id=$APP_ID "
            headerKey += "app_secret=$APP_SECRET "
            headersMap?.put("x-pesamin-authorization", headerKey)
            headersMap?.put("x-verify-phonenumber", PesaApplication.getUserPhNo())
            headersMap?.put("verify_token", getJWTToken(context))
            if (PesaApplication.getRegToken() != "") {
                headersMap?.put("Device_token", PesaApplication.getRegToken())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return headersMap!!
    }

    fun getJWTToken(context: Context): String {
        try {
            val now = Date().time
            verifyToken = Jwts.builder()
                .setSubject(PesaApplication.getUserPhNo())
                .claim("data", PesaApplication.getUserPhNo())
                .setIssuedAt(Date(now))
                .setExpiration(Date(now + 120000))
                .signWith(SignatureAlgorithm.HS256, getJWTSecretKey(context).toByteArray())
                .compact()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return verifyToken
    }

    fun setAllCryptoCurrencies(allCryptoCurrencies: List<CryptoCurrenciesValue>) {
        this.allCryptoCurrencies = allCryptoCurrencies
    }

    fun getMarketFromCurrencySymbol(currencySymbol: String): InvestData? {
        if (getAllMarkets()!!.isNotEmpty()) {
            for (marketData in getAllMarkets()!!) {
                if (marketData.underlyingSymbol == currencySymbol) {
                    return marketData
                }
            }
        }
        return null
    }


    fun getUSDValueFromCurrency(currencySymbol: String): Double {
        try {
            for (currency in allCryptoCurrencies!!) {
                if (currency.symbol == currencySymbol) {
                    return currency.fiatCurrency
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0.00
    }

}