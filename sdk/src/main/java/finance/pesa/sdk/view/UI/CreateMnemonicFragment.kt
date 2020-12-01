package finance.pesa.sdk.view.UI

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface
import org.web3j.crypto.CipherException
import org.web3j.crypto.MnemonicUtils
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SecureRandom


class CreateMnemonicFragment : Fragment() {
    private var mobileNumber: String? = null
    private var isNewUser: Boolean? = null
    private var ivBack: ImageView? = null
    private var btnConfirm: Button? = null
    private var mnemonicSwitch: Switch? = null
    private var mnemonicTxt: TextView? = null
    private var tapCopy: TextView? = null
    private var moreDetails: TextView? = null


    companion object {
        fun newInstance(mobileNumber: String,isNewUser: Boolean): CreateMnemonicFragment {
            val fragment = CreateMnemonicFragment()
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
        val view = inflater.inflate(R.layout.mnemonic_create, container, false)
        mnemonicTxt = view.findViewById(R.id.mnemonic_txt)
        mnemonicSwitch = view.findViewById(R.id.mnemonic_switch)
        ivBack = view.findViewById(R.id.iv_back)
        btnConfirm = view.findViewById(R.id.btn_confirm)
        tapCopy = view.findViewById(R.id.tap_copy)
        moreDetails = view.findViewById(R.id.more_details)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        btnConfirm?.isEnabled=false
        btnConfirm?.isClickable=false
        setTextViewHTML(
            moreDetails!!,
            "Find a private place and write down your Account Key. Please store it somewhere safe. Do not save it in your phone. <a href=learn_more>Learn about Account Keys.</a>"
        )
        generateMnemonic()
        mnemonicSwitch?.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked && mnemonicTxt?.text.toString().trim().isNotEmpty()) {
                btnConfirm?.isEnabled=true
                btnConfirm?.isClickable=true
            } else {
                btnConfirm?.isEnabled=false
                btnConfirm?.isClickable=false
            }
        }
        btnConfirm?.setOnClickListener {
            PesaApplication.getChildFragmentManager()!!.beginTransaction()
                .add(
                    R.id.shareg_main_container,
                    MnemonicVerifyFragment.newInstance(mobileNumber!!,mnemonicTxt?.text.toString().trim(),isNewUser!!)
                ).addToBackStack("register")
                .commitAllowingStateLoss()
        }
        tapCopy?.setOnClickListener {
            val cm = context!!.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            cm.text = mnemonicTxt?.text
            UserInterface.showToast(context, "Copied your account key")
        }
        ivBack?.setOnClickListener { activity!!.onBackPressed() }

        return view
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
                    if (span.url == "learn_more") {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }

    private fun generateMnemonic() {
        try {
            val initialEntropy = ByteArray(16)
            val secureRandom = SecureRandom()
            secureRandom.nextBytes(initialEntropy)
            val mnemonic = MnemonicUtils.generateMnemonic(initialEntropy)
            mnemonicTxt?.text = mnemonic
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
        UserInterface.hideProgress(context)
    }
}