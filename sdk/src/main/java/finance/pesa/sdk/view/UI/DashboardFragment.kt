package finance.pesa.sdk.view.UI

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.NavigationIcons
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.UI.ActivitiesFragment.Companion.activitiesFragment
import finance.pesa.sdk.view.UI.InvestFragment.Companion.investFragment
import finance.pesa.sdk.view.UI.WalletFragment.Companion.walletFragment
import finance.pesa.sdk.view.adapter.PesaViewPageAdapter


class DashboardFragment : Fragment() {
    var tabView: LinearLayout? = null
    var viewPager: ViewPager? = null
    var walletTab: NavigationIcons? = null
    var moreTab: NavigationIcons? = null
    var investTab: NavigationIcons? = null
    var activityTab: NavigationIcons? = null
    var securityTab: NavigationIcons? = null
    var adapter: PesaViewPageAdapter? = null
    var displayTab: Int? = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 23
    private val CONTACT_PERMISSION_REQUEST_CODE = 24
    private val cameraPermissions = arrayOf(
        android.Manifest.permission.CAMERA
    )

    private val contactPermissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS
    )

    //Permission Dialog
    private var contactCheck: ImageView? = null
    private var cameraCheck: ImageView? = null
    private var notification_check: ImageView? = null
    private var continuePermissionBtn: Button? = null
    private var btn_microphone: LinearLayout? = null
    private var btn_notification: LinearLayout? = null
    private var btn_contact: LinearLayout? = null
    private var permissionDialog: Dialog? = null
    private var initialDialog: Dialog? = null

    companion object {
        var dashboardFragment = DashboardFragment()
        fun newInstance(selectedTab:Int): DashboardFragment {
            dashboardFragment.displayTab=selectedTab
            return dashboardFragment
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_home, container, false)
        dashboardFragment = this
        tabView = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.viewPager)
        setTabView()
        viewPager?.offscreenPageLimit = 4
        adapter = PesaViewPageAdapter(childFragmentManager)
        adapter?.add(WalletFragment.newInstance())
        adapter?.add(SendRequestFragment.newInstance())
        adapter?.add(ActivitiesFragment.newInstance())
        adapter?.add(Fragment())
        viewPager?.adapter = adapter
        UserInterface.changeNavigationBar(activity!!, R.color.app_green)
        PesaApplication.setEnableLock(true)
        viewPager?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        walletTab!!.performClick()
                    }
                    1 -> {
                        investTab!!.performClick()
                    }
                    2 -> {
                        activityTab!!.performClick()
                    }
                    3 -> securityTab!!.performClick()
                    4 -> {
                        moreTab!!.performClick()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        walletTab!!.setOnClickListener {
            viewPager!!.currentItem = 0
            walletTab!!.setState(R.color.app_green,R.color.white_black)
            activityTab!!.setState(R.color.light_gray_white,R.color.white_black)
            securityTab!!.setState(R.color.light_gray_white,R.color.white_black)
            investTab!!.setState(R.color.light_gray_white,R.color.white_black)
            moreTab!!.setState(R.color.light_gray_white,R.color.white_black)
            clearCallSubFragments()
            clearMessageSubFragments()
            walletTabSelected()
            UserInterface.changeStatusBar(activity!!, R.color.app_green)
            UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        }
        investTab!!.setOnClickListener {
            viewPager!!.currentItem = 1
            walletTab!!.setState(R.color.invest_tab_bg,R.color.app_green)
            activityTab!!.setState(R.color.invest_tab_bg,R.color.app_green)
            securityTab!!.setState(R.color.invest_tab_bg,R.color.app_green)
            investTab!!.setState(R.color.white,R.color.app_green)
            moreTab!!.setState(R.color.invest_tab_bg,R.color.app_green)
            clearCallSubFragments()
            clearMessageSubFragments()
            investTabSelected()
            UserInterface.changeStatusBar(activity!!, R.color.app_green)
            UserInterface.changeNavigationBar(activity!!, R.color.app_green)
        }
        activityTab!!.setOnClickListener {
            viewPager!!.currentItem = 2
            walletTab!!.setState(R.color.light_gray_white,R.color.white_black)
            activityTab!!.setState(R.color.app_green,R.color.white_black)
            securityTab!!.setState(R.color.light_gray_white,R.color.white_black)
            investTab!!.setState(R.color.light_gray_white,R.color.white_black)
            moreTab!!.setState(R.color.light_gray_white,R.color.white_black)
            clearMessageSubFragments()
            activityTabSelected()
            UserInterface.changeStatusBar(activity!!, R.color.white_black)
            UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        }
        securityTab!!.setOnClickListener {
            viewPager!!.currentItem = 3
            walletTab!!.setState(R.color.light_gray_white,R.color.white_black)
            activityTab!!.setState(R.color.light_gray_white,R.color.white_black)
            securityTab!!.setState(R.color.app_green,R.color.white_black)
            investTab!!.setState(R.color.light_gray_white,R.color.white_black)
            moreTab!!.setState(R.color.light_gray_white,R.color.white_black)
            clearCallSubFragments()
            securityTabSelected()
            UserInterface.changeStatusBar(activity!!, R.color.white_black)
            UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        }

        moreTab!!.setOnClickListener {
            viewPager!!.currentItem = 4
            walletTab!!.setState(R.color.light_gray_white,R.color.white_black)
            activityTab!!.setState(R.color.light_gray_white,R.color.white_black)
            securityTab!!.setState(R.color.light_gray_white,R.color.white_black)
            investTab!!.setState(R.color.light_gray_white,R.color.white_black)
            moreTab!!.setState(R.color.app_green,R.color.white_black)
            clearCallSubFragments()
            clearMessageSubFragments()
            securityTabSelected()
            UserInterface.changeStatusBar(activity!!, R.color.white_black)
            UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        }
        onTabChanged(displayTab!!)

        if(Constants.getisEPNEnabled()){
            checkPermission()
        }

        return view
    }


    private fun clearCallSubFragments() {
        try {
            val manager = PesaApplication.getChildFragmentManager()
            if (manager.getFragments() != null && manager.fragments.size > 0) {
                for (i in 0 until manager.backStackEntryCount) {
                    try {
                        // String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        val mFragment =
                            manager.fragments[manager.backStackEntryCount - 1]
                        if (mFragment != null) {
                            /*manager.beginTransaction()
                                .remove(manager.findFragmentById(R.id.call_frame)!!).commit()
                            manager.popBackStackImmediate()*/
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

    private fun clearMessageSubFragments() {
        try {
            val manager = PesaApplication.getChildFragmentManager()
            if (manager.getFragments() != null && manager.fragments.size > 0) {
                for (i in 0 until manager.backStackEntryCount) {
                    try {
                        // String fragmentTag = manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName();
                        val mFragment =
                            manager.fragments[manager.backStackEntryCount - 1]
                        if (mFragment != null) {
                            /* manager.beginTransaction()
                                 .remove(manager.findFragmentById(R.id.message_frame)!!).commit()
                             manager.popBackStackImmediate()*/
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


    fun onTabChanged(displayTab: Int) {
        viewPager!!.currentItem = displayTab
    }


    private fun setTabView() {
        moreTab = NavigationIcons(
            activity!!,
            "Account",
            R.drawable.ic_nav_account_unselect,
            R.drawable.ic_nav_account,
            false,
            false
        )
        walletTab = NavigationIcons(
            activity!!,
            "Wallet",
            R.drawable.ic_wallet_new,
            R.drawable.ic_wallet_new_select,
            true,
            false
        )
        activityTab = NavigationIcons(
            activity!!,
            "Activities",
            R.drawable.ic_activity_new,
            R.drawable.ic_activity_new_select,
            false,
            false
        )
        securityTab = NavigationIcons(
            activity!!,
            "Security",
            R.drawable.ic_security_new,
            R.drawable.ic_security_new_select,
            false,
            false
        )
        investTab = NavigationIcons(
            activity!!,
            "Invest",
            R.drawable.ic_dollar_new,
            R.drawable.ic_dollar_new_select,
            false,
            false
        )
        tabView!!.addView(walletTab)
        tabView!!.addView(investTab)
        tabView!!.addView(activityTab)
        tabView!!.addView(securityTab)
    }

    private fun walletTabSelected() {
        try {
            if (walletFragment != null) {
                walletFragment!!.onTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun activityTabSelected() {
        try {
            if (activitiesFragment != null) {
                activitiesFragment!!.onTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun securityTabSelected() {


    }


    private fun investTabSelected() {
        try {
            if (investFragment != null) {
                investFragment!!.onTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onResume() {
        if (permissionDialog != null)
            if (permissionDialog!!.isShowing)
                permissionValidation()
        super.onResume()
    }

    fun checkPermission() {
        try {
            if (!NotificationManagerCompat.from(activity!!).areNotificationsEnabled() || checkPermissionForCamera() || checkPermissionForContact())
                showPermissionDialog()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun showPermissionDialog() {
        try {
            if (permissionDialog != null)
                if (permissionDialog!!.isShowing)
                    return
            permissionDialog = Dialog(activity!!, R.style.CustomDialog)
            permissionDialog!!.setContentView(R.layout.permission_allow_popup)
            val d = ColorDrawable(Color.TRANSPARENT)
            permissionDialog!!.window!!.setBackgroundDrawable(d)
            permissionDialog!!.setCancelable(false)
            cameraCheck = permissionDialog!!.findViewById(R.id.camera_check)
            contactCheck = permissionDialog!!.findViewById(R.id.contact_check)
            notification_check = permissionDialog!!.findViewById(R.id.notification_check)
            continuePermissionBtn = permissionDialog!!.findViewById(R.id.btn_continue)
            btn_microphone = permissionDialog!!.findViewById(R.id.btn_camera)
            btn_notification = permissionDialog!!.findViewById(R.id.btn_notification)
            btn_contact = permissionDialog!!.findViewById(R.id.btn_contact)
            //var contactMsg: TextView = permissionDialog!!.findViewById(R.id.contact_allow_msg)
            //setItalicText(contactMsg)

            continuePermissionBtn!!.setOnClickListener {
                permissionDialog!!.dismiss()
                UserInterface.readContactNumbers(context!!)
            }

            permissionValidation()
            permissionDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun permissionValidation() {
        try {
            var isStart = true
            if (checkPermissionForContact()) {
                isStart = false
                contactCheck!!.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_unselected,
                        null
                    )
                )
            } else {
                contactCheck!!.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_selected,
                        null
                    )
                )
            }

            if (NotificationManagerCompat.from(activity!!).areNotificationsEnabled()) {
                notification_check!!.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_selected,
                        null
                    )
                )
            } else {
                isStart = false
                notification_check!!.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_unselected,
                        null
                    )
                )
            }
            if (checkPermissionForCamera()) {
                isStart = false
                cameraCheck!!.setImageDrawable(
                    resources.getDrawable(
                        R.drawable.ic_unselected,
                        null
                    )
                )
            } else {
                cameraCheck!!.setImageDrawable(resources.getDrawable(R.drawable.ic_selected, null))
            }
            if (isStart) {
                continuePermissionBtn!!.isEnabled = true
                continuePermissionBtn!!.isClickable = true
            } else {
                continuePermissionBtn!!.isEnabled = false
                continuePermissionBtn!!.isClickable = false
            }
            btn_microphone!!.setOnClickListener {
                if (checkPermissionForCamera())
                    requestPermissionForCamera()
            }

            btn_contact!!.setOnClickListener {
                if (checkPermissionForContact())
                    requestPermissionForContact()
            }
            btn_notification!!.setOnClickListener {
                if (!NotificationManagerCompat.from(activity!!).areNotificationsEnabled()) {
                    val intent = Intent()
                    when {
                        Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1 -> {
                            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            intent.putExtra(
                                "android.provider.extra.APP_PACKAGE",
                                activity!!.packageName
                            )
                        }
                        Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                            intent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
                            intent.putExtra("app_package", activity!!.packageName)
                            intent.putExtra("app_uid", activity!!.applicationInfo.uid)
                        }
                        else -> {
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            intent.addCategory(Intent.CATEGORY_DEFAULT)
                            intent.data = Uri.parse("package:" + activity!!.packageName)
                        }
                    }

                    startActivityForResult(intent, 0)
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun checkPermissionForContact(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (perm in contactPermissions) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        perm
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                }
            }
        }
        return false
    }

    private fun requestPermissionForContact() {
        ActivityCompat.requestPermissions(
            activity!!,
            contactPermissions,
            CONTACT_PERMISSION_REQUEST_CODE
        )
    }

    private fun checkPermissionForCamera(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (perm in cameraPermissions) {
                if (ActivityCompat.checkSelfPermission(
                        context!!,
                        perm
                    ) !== PackageManager.PERMISSION_GRANTED
                ) {
                    return true
                }
            }
        }
        return false
    }

    private fun requestPermissionForCamera() {
        ActivityCompat.requestPermissions(
            activity!!,
            cameraPermissions,
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }


}