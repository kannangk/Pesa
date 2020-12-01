package finance.pesa.sdk.view.UI

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface

class WelcomeCreateWallet : Fragment() {
    private var importWallet: Button? = null
    private var createWallet: TextView? = null
    private var link: TextView? = null
    private var mobileNumber: String? = null

    companion object {
        fun newInstance(mobileNumber: String): WelcomeCreateWallet {
            val fragment = WelcomeCreateWallet()
            fragment.mobileNumber = mobileNumber
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.welcome_create_wallet, container, false)
        link = view.findViewById(R.id.link)
        createWallet = view.findViewById(R.id.new_wallet)
        importWallet = view.findViewById(R.id.import_wallet)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        createWallet!!.setOnClickListener {
            PesaApplication.getChildFragmentManager()!!.beginTransaction()
                .add(
                    R.id.shareg_main_container,
                    CreateMnemonicFragment.newInstance(mobileNumber!!, true)
                ).addToBackStack("register")
                .commitAllowingStateLoss()
        }
        importWallet!!.setOnClickListener {
            PesaApplication.getChildFragmentManager()!!.beginTransaction()
                .add(
                    R.id.shareg_main_container,
                    ImportMnemonicFragment.newInstance(mobileNumber!!, true)
                ).addToBackStack("register")
                .commitAllowingStateLoss()
        }

        return view
    }


    // create new wallet
    private fun onImportWallet() {
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
        if (requestCode == Constants.QR_CODE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val privateKey = data?.getStringExtra(Constants.QR_CODE_RESULT)
            if (privateKey != null) {
                try {
                    val isRight = Constants.setPrivateKey(privateKey, context!!)
                    if (isRight) {
                        PesaApplication.getChildFragmentManager()!!.beginTransaction().replace(
                            R.id.shareg_main_container,
                            CreateMnemonicFragment.newInstance(mobileNumber!!, true)
                        ).commitAllowingStateLoss()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


}