package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import org.web3j.crypto.*
import org.web3j.crypto.MnemonicUtils.validateMnemonic
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException


class ImportMnemonicFragment : Fragment() {
    private var mobileNumber: String? = null
    private var isNewUser: Boolean? = null
    private var ivBack: ImageView? = null
    private var btnImportWallet: Button? = null
    private var btnCreatWallet: TextView? = null
    private var mnemonicTxt: TextView? = null
    private var errorMessage: TextView? = null

    companion object {
        fun newInstance(mobileNumber: String, isNewUser: Boolean): ImportMnemonicFragment {
            val fragment = ImportMnemonicFragment()
            fragment.mobileNumber = mobileNumber
            fragment.isNewUser = isNewUser
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mnemonic_import, container, false)
        mnemonicTxt = view.findViewById(R.id.mnemonic_txt)
        btnCreatWallet = view.findViewById(R.id.create_new_wallet)
        ivBack = view.findViewById(R.id.iv_back)
        btnImportWallet = view.findViewById(R.id.btn_import)
        errorMessage = view.findViewById(R.id.error_message)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        mnemonicTxt?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                errorMessage?.visibility = View.GONE
                if(s.toString().isNotEmpty()){
                    btnImportWallet?.isClickable=true
                    btnImportWallet?.isEnabled=true
                }else{
                    btnImportWallet?.isClickable=false
                    btnImportWallet?.isEnabled=false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        btnImportWallet?.setOnClickListener {
            if (isValidKeyLength(mnemonicTxt?.text.toString().trim())) {
                importWallet()
            } else {
                errorMessage?.text="Wrong Key phrases, please try again! "
                errorMessage?.visibility = View.VISIBLE
                UserInterface.errorShow(errorMessage, context)
            }
        }

        btnCreatWallet?.setOnClickListener {
            PesaApplication.getChildFragmentManager()!!.beginTransaction()
                .add(
                    R.id.shareg_main_container,
                    CreateMnemonicFragment.newInstance(mobileNumber!!, isNewUser!!)
                ).addToBackStack("register")
                .commitAllowingStateLoss()
        }
        ivBack?.setOnClickListener { activity!!.onBackPressed() }

        return view
    }

    //import wallet
    private fun importWallet() {
        try {
            if (isNewUser!!) {
                if (validateMnemonic(mnemonicTxt?.text.toString().trim())) {
                    PesaApplication.getChildFragmentManager()!!.beginTransaction().add(
                        R.id.shareg_main_container,
                        PasswordEnterFragment.newInstance(
                            mobileNumber!!,
                            mnemonicTxt!!.text.toString().trim(),
                            isNewUser!!
                        )
                    ).addToBackStack("register").commitAllowingStateLoss()
                } else {
                    errorMessage?.text="Wrong Key phrases, please try again! "
                    errorMessage?.visibility = View.VISIBLE
                    UserInterface.errorShow(errorMessage, context)
                }
            } else {
                if (isAuthorizedWallet(mnemonicTxt?.text.toString().trim())) {
                    PesaApplication.getChildFragmentManager()!!.beginTransaction().add(
                        R.id.shareg_main_container,
                        PasswordEnterFragment.newInstance(
                            mobileNumber!!,
                            mnemonicTxt!!.text.toString().trim(),
                            isNewUser!!
                        )
                    ).addToBackStack("register").commitAllowingStateLoss()
                } else {
                    errorMessage?.text="Wrong Key phrases for your existing wallet, please try again! "
                    errorMessage?.visibility = View.VISIBLE
                    UserInterface.errorShow(errorMessage, context)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    //check is valid Mnemonic key
    private fun isValidKeyLength(mnemonic: String): Boolean {
        try {
            return mnemonic.split(" ").size == 12
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }


    private fun isAuthorizedWallet(mnemonicWord: String): Boolean {
        try {
            val masterKeypair =
                Bip32ECKeyPair.generateKeyPair(MnemonicUtils.generateSeed(mnemonicWord, null))
            val path = intArrayOf(
                44 or Bip32ECKeyPair.HARDENED_BIT,
                60 or Bip32ECKeyPair.HARDENED_BIT,
                0 or Bip32ECKeyPair.HARDENED_BIT,
                0,
                0
            )
            val x = Bip32ECKeyPair.deriveKeyPair(masterKeypair, path)
            val credentials = Credentials.create(x)
            if (credentials != null) {
                return Constants.getLastWalletAddress() == Keys.toChecksumAddress(credentials.address)
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
        return false
    }


}