package finance.pesa.sdk.view.UI

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.hbb20.CountryCodePicker
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.robinhood.ticker.TickerUtils
import com.robinhood.ticker.TickerView
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.Minimum_EPN_Activation_Charge
import finance.pesa.sdk.utils.RefreshWalletListener
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.Interface.EPNActivationListener
import finance.pesa.sdk.view.UI.ActivitiesFragment.Companion.activitiesFragment
import finance.pesa.sdk.view.UI.DashboardFragment.Companion.dashboardFragment
import org.web3j.crypto.Keys
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Convert


class SendRequestFragment : Fragment(), View.OnClickListener {

    private var isViewGenerated: Boolean? = false
    private var ivQRCode: ImageView? = null
    private var ivNotification: ImageView? = null
    private var btnSend: Button? = null
    private var btnRequest: Button? = null
    private var dialOne: Button? = null
    private var dialTwo: Button? = null
    private var dialThree: Button? = null
    private var dialFour: Button? = null
    private var dialFive: Button? = null
    private var dialSix: Button? = null
    private var dialSeven: Button? = null
    private var dialEight: Button? = null
    private var dialNine: Button? = null
    private var dialZero: Button? = null
    private var dialDot: Button? = null
    private var txtValue: EditText? = null
    private var epnSetupBanner: LinearLayout? = null
    private var epnNumber: TextView? = null
    private var balance: TextView? = null
    private var bannerTitle: TextView? = null
    private var bannerMsg: TextView? = null
    private var bannerIcon: ImageView? = null
    private var cTimer: CountDownTimer? = null
    private var ethDollarValue: Double? = 0.0


    companion object {
        var sendRequestFragment: SendRequestFragment? = SendRequestFragment()
        fun newInstance(): SendRequestFragment {
            return sendRequestFragment!!
        }
    }

    fun onTabSelected() {
        if (!isViewGenerated!!) {
            UserInterface.changeStatusBar(activity!!, R.color.app_green)
            isViewGenerated = true

        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (DashboardFragment.dashboardFragment!!.viewPager!!.currentItem == 1) {
                onTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.send_request_main, container, false)
        epnSetupBanner = view.findViewById(R.id.epn_setup_banner)
        btnSend = view.findViewById(R.id.btn_send)
        ivNotification = view.findViewById(R.id.iv_notification)
        btnRequest = view.findViewById(R.id.btn_request)
        bannerIcon = view.findViewById(R.id.banner_icon)
        bannerTitle = view.findViewById(R.id.banner_title)
        bannerMsg = view.findViewById(R.id.banner_msg)
        ivQRCode = view.findViewById(R.id.iv_qr_code)
        epnNumber = view.findViewById(R.id.epn_number)
        txtValue = view.findViewById(R.id.txt_value)
        balance = view.findViewById(R.id.balance)
        dialOne = view.findViewById(R.id.dial_pad_1)
        dialOne!!.setOnClickListener(this)
        dialTwo = view.findViewById(R.id.dial_pad_2)
        dialTwo!!.setOnClickListener(this)
        dialThree = view.findViewById(R.id.dial_pad_3)
        dialThree!!.setOnClickListener(this)
        dialFour = view.findViewById(R.id.dial_pad_4)
        dialFour!!.setOnClickListener(this)
        dialFive = view.findViewById(R.id.dial_pad_5)
        dialFive!!.setOnClickListener(this)
        dialSix = view.findViewById(R.id.dial_pad_6)
        dialSix!!.setOnClickListener(this)
        dialSeven = view.findViewById(R.id.dial_pad_7)
        dialSeven!!.setOnClickListener(this)
        dialEight = view.findViewById(R.id.dial_pad_8)
        dialEight!!.setOnClickListener(this)
        dialNine = view.findViewById(R.id.dial_pad_9)
        dialNine!!.setOnClickListener(this)
        dialZero = view.findViewById(R.id.dial_pad_0)
        dialZero!!.setOnClickListener(this)
        dialDot = view.findViewById(R.id.dial_pad_10)
        dialDot!!.setOnClickListener(this)
        btnRequest?.isEnabled = false
        btnRequest?.isClickable = false
        btnSend?.isEnabled = false
        btnSend?.isClickable = false
        if (Constants.getisEPNEnabled()) {
            btnRequest?.isEnabled = true
            btnRequest?.isClickable = true
            btnSend?.isEnabled = true
            btnSend?.isClickable = true
            epnSetupBanner?.visibility = View.GONE
            epnNumber?.text = "EPN: " + Constants.getEPNNumber()
        }
        epnSetupBanner?.setOnClickListener {
            if (ethDollarValue!! >= Minimum_EPN_Activation_Charge && !Constants.getisEPNEnabled()) {
                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                    .add(
                        R.id.shareg_main_container,
                        EPNAuthFragmet.newInstance()
                    ).addToBackStack("EPN_Activation")
                    .commitAllowingStateLoss()
            } else {
                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                    .add(
                        R.id.shareg_main_container,
                        AddFundsEPNFragment.newInstance()
                    ).addToBackStack("add_fund")
                    .commitAllowingStateLoss()
            }
        }
        val backSpace = view.findViewById<ImageButton>(R.id.backspace)
        Constants.setEPNActivationListener(epnActivationListener)
        backSpace.setOnClickListener {
            val length = txtValue!!.text.length
            if (length > 1 && txtValue!!.text.toString() != "$0") {
                txtValue!!.setText(removeLastChar(txtValue!!.text.toString()))
                if (txtValue!!.text.toString() == "$")
                    txtValue!!.setText("$0")
            }
        }
        backSpace.setOnLongClickListener {
            txtValue!!.setText("")
            true
        }
        btnSend?.setOnClickListener {
            try {
                var sendValue = txtValue?.text.toString().trim().replace("$", "")
                if (sendValue == "0")
                    sendValue = ""
                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                    .add(
                        R.id.shareg_main_container,
                        SendMoneyFragment.newInstance(sendValue, "")
                    ).addToBackStack("send_money")
                    .commitAllowingStateLoss()
                txtValue?.setText("$0")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        btnRequest?.setOnClickListener {
            try {
                var sendValue = txtValue?.text.toString().trim().replace("$", "")
                if (sendValue == "0")
                    sendValue = ""
                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                    .add(
                        R.id.shareg_main_container,
                        RequestMoneyFragment.newInstance(sendValue)
                    ).addToBackStack("request_money")
                    .commitAllowingStateLoss()
                txtValue?.setText("$0")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        ivQRCode?.setOnClickListener {
            onScanWalletAddress()
        }
        ivNotification?.setOnClickListener {
            UserInterface.redirectToActivity()
        }
        Constants.setRefreshWalletListener(refreshWalletListener)
        updateNotificationIcon(Constants.getIsPendingNotification(context!!)!!)
        return view
    }

    fun updateNotificationIcon(isPending: Boolean) {
        try {
            if (isPending)
                ivNotification?.setImageDrawable(context!!.getDrawable(R.drawable.ic_notification))
            else
                ivNotification?.setImageDrawable(context!!.getDrawable(R.drawable.ic_notification_default))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val epnActivationListener = object : EPNActivationListener {
        override fun onEPNRegistered(phoneNumber: String, isClaimUser: Boolean, msg: String) {
            try {
                successEPNRegisterDialog(phoneNumber, msg, isClaimUser)
                activity!!.runOnUiThread {
                    if (Constants.getisEPNEnabled()) {
                        btnRequest?.isEnabled = true
                        btnRequest?.isClickable = true
                        btnSend?.isEnabled = true
                        btnSend?.isClickable = true
                        epnSetupBanner?.visibility = View.GONE
                        epnNumber?.text = "EPN: " + Constants.getEPNNumber()
                    }
                }
                cancelTimer()
                //startTimer(50000)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    // Transaction success for EPN Register
    private fun successEPNRegisterDialog(phNo: String, msg: String, isClaim: Boolean) {
        try {
            val cfmDialog = Dialog(activity!!, R.style.CustomDialog)
            cfmDialog!!.setContentView(R.layout.epn_register_success)
            val d = ColorDrawable(Color.TRANSPARENT)
            cfmDialog!!.window!!.setBackgroundDrawable(d)
            cfmDialog!!.setCancelable(false)
            val phNumber = cfmDialog!!.findViewById<TextView>(R.id.epn_number)
            val tvMsg = cfmDialog!!.findViewById<TextView>(R.id.iv_msg)
            val btnDone = cfmDialog!!.findViewById<Button>(R.id.btn_done)
            phNumber?.text = phNo
            tvMsg?.text = msg
            if (isClaim)
                btnDone?.text = "Claim now"
            else
                btnDone?.text = "Done"
            btnDone?.setOnClickListener {
                cfmDialog?.dismiss()
                if (isClaim)
                    UserInterface.redirectToEscrow()
                if (dashboardFragment != null)
                    dashboardFragment.checkPermission()
            }
            cfmDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onClick(v: View?) {
        onDigitClick(v as Button)
    }

    private fun onDigitClick(button: Button) {
        button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        val oldValue = if (txtValue!!.text == null)
            ""
        else if (txtValue!!.text.toString() == "$0" && button.text != ".") {
            "$"
        } else {
            txtValue!!.text
        }
        val newValue = if (button.text == ".") {
            when {
                txtValue!!.text == null -> "0."
                txtValue!!.text.contains(".") -> ""
                else -> button.text
            }
        } else
            button.text
        txtValue!!.setText(oldValue.toString() + newValue.toString())
    }

    private fun removeLastChar(s: String): String {
        //returns the string after removing the last character
        return s.substring(0, s.length - 1)
    }

    private inner class GetWalletBalance() :
        AsyncTask<Boolean, Boolean, Boolean>() {
        override fun doInBackground(vararg params: Boolean?): Boolean? {
            try {
                getETHWalletBalance()
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }

    }

    //getCoinWallet
    private fun getETHWalletBalance(
    ) {
        try {
            val walletBalance = PesaApplication.getWeb3j()
                .ethGetBalance(Constants.getAccountAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get()

            val ethTOusd = Constants.getUSDValueFromCurrency("ETH")
            Log.d("ETH_TO_USD", ethTOusd.toString())
            var usdValue = "0.00"
            if (ethTOusd != 0.0) {
                ethDollarValue = (
                        Convert.fromWei(
                            walletBalance.balance.toBigDecimal(),
                            Convert.Unit.ETHER
                        ).toDouble()) * (ethTOusd)

                usdValue = UserInterface.roundTwo(ethDollarValue!!)
            }
            Log.d("ETH_Bal", usdValue)
            activity!!.runOnUiThread {
                when {
                    Constants.getisEPNEnabled() -> {
                        epnSetupBanner?.visibility = View.GONE
                        epnNumber?.text = "EPN: " + Constants.getEPNNumber()
                        btnRequest?.isEnabled = true
                        btnRequest?.isClickable = true
                        btnSend?.isEnabled = true
                        btnSend?.isClickable = true
                    }
                    ethTOusd > 0 -> {
                        if (ethDollarValue!! < 25.0) {
                            bannerTitle?.text = "Add your first funds on PESA"
                            bannerMsg?.text =
                                "Please deposit at least $25 worth of ETH for activating EPN, sending & receiving money."
                            bannerIcon?.setImageDrawable(context!!.getDrawable(R.drawable.ic_add_circle))
                        } else {
                            try {
                                if (Constants.getETHBalanceUpdatedListener() != null)
                                    Constants.getETHBalanceUpdatedListener()!!.onEnableEPNActivateBalance()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            bannerTitle?.text = "Activate your EPN"
                            bannerMsg?.text =
                                "Please deposit at least $25 worth of ETH for activating EPN, sending & receiving money."
                            bannerIcon?.setImageDrawable(context!!.getDrawable(R.drawable.ic_phone_circle))
                        }
                        epnSetupBanner?.visibility = View.VISIBLE
                        epnNumber?.text = ""
                        btnRequest?.isEnabled = false
                        btnRequest?.isClickable = false
                        btnSend?.isEnabled = false
                        btnSend?.isClickable = false
                    }
                    else -> {
                        btnRequest?.isEnabled = false
                        btnRequest?.isClickable = false
                        btnSend?.isEnabled = false
                        btnSend?.isClickable = false
                    }
                }
                balance?.text = "Available Balance (ETH): $" + usdValue
            }
            return
        } catch (e: Exception) {
            e.printStackTrace()
        }
        activity!!.runOnUiThread {
            if (Constants.getisEPNEnabled()) {
                btnRequest?.isEnabled = true
                btnRequest?.isClickable = true
                btnSend?.isEnabled = true
                btnSend?.isClickable = true
                epnSetupBanner?.visibility = View.GONE
                epnNumber?.text = "EPN: " + Constants.getEPNNumber()
            } else {
                btnRequest?.isEnabled = false
                btnRequest?.isClickable = false
                btnSend?.isEnabled = false
                btnSend?.isClickable = false
                epnSetupBanner?.visibility = View.VISIBLE
                epnNumber?.text = ""
                bannerTitle?.text = "Add your first funds on PESA"
                bannerMsg?.text =
                    "Please deposit at least $25 worth of ETH for activating EPN, sending & receiving money."
                bannerIcon?.setImageDrawable(context!!.getDrawable(R.drawable.ic_add_circle))
            }
            balance?.text = "Available Balance (ETH): $0.0"
        }
    }

    //start timer function
    internal fun startTimer(totalMilliSec: Long) {
        cTimer = object : CountDownTimer(totalMilliSec, 10000) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    if (activity != null) {
                        GetWalletBalance().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                if (activity != null) {
                    startTimer(50000)
                }
            }
        }
        cTimer!!.start()
    }

    //cancel timer
    private fun cancelTimer() {
        if (cTimer != null)
            cTimer!!.cancel()
    }

    override fun onPause() {
        super.onPause()
        cancelTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancelTimer()
    }

    override fun onResume() {
        super.onResume()
        if (!Constants.getisEPNEnabled())
            startTimer(50000)
        else
            updateETHBalance()
    }

    private fun updateETHBalance() {
        try {
            activity!!.runOnUiThread {
                if (Constants.getAllMarkets()!!.isNotEmpty()) {
                    val ethObj = Constants.getMarketFromCurrencySymbol("ETH")
                    if (ethObj != null) {
                        if (Constants.getisEPNEnabled()) {
                            val ethBal: Double =
                                if (Constants.getMarketValues()!!.containsKey(ethObj.underlyingSymbol)) {
                                    Constants.getMarketValues()!![ethObj.underlyingSymbol]!!.walletBalance.toDouble()
                                } else
                                    ethObj.balance
                            val ethUSDBalance = ethBal * ethObj.underlyingPriceUSD
                            btnRequest?.isEnabled = true
                            btnRequest?.isClickable = true
                            btnSend?.isEnabled = true
                            btnSend?.isClickable = true
                            balance?.text =
                                "Available Balance (ETH): $" + UserInterface.roundTwo(ethUSDBalance)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val refreshWalletListener = object : RefreshWalletListener {
        override fun onRefreshWallet() {
            updateETHBalance()
        }

        override fun onRefreshInvest() {
        }

        override fun onMarketLoaded() {
            updateETHBalance()
        }

    }

    private fun onScanWalletAddress() {
        try {
            Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            try {
                                val intent = Intent(activity, QRCodeScannerActivity::class.java)
                                startActivityForResult(intent, Constants.QR_CODE_REQUEST_CODE)
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
                            try {
                                var sendValue = txtValue?.text.toString().trim().replace("$", "")
                                if (sendValue == "0")
                                    sendValue = ""
                                PesaApplication.getChildFragmentManager()!!.beginTransaction()
                                    .add(
                                        R.id.shareg_main_container,
                                        SendMoneyFragment.newInstance(sendValue, walletAddress)
                                    ).addToBackStack("send_money")
                                    .commitAllowingStateLoss()
                                txtValue?.setText("$0")
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        } else {
                            UserInterface.showToast(context, "Invalid Wallet Address")
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

}