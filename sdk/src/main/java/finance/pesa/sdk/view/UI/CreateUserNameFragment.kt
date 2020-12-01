/*
package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.parse.ParseObject
import com.parse.ParseQuery
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.agrawalsuneet.dotsloader.loaders.TashieLoader


class CreateUserNameFragment : Fragment() {

    private var btnContinue: RelativeLayout? = null
    private var userName: EditText? = null
    private var emailText: EditText? = null
    private var userNameDiv: View? = null
    private var ivBack: ImageView? = null
    private var alertLay: LinearLayout? = null
    private var alertText: TextView? = null
    private var userNamePb: TashieLoader? = null
    var mobileNumber: String? = null
    var isNameAvailable = false

    companion object {
        fun newInstance(mobileNumber: String): CreateUserNameFragment {
            val fragment = CreateUserNameFragment()
            fragment.mobileNumber = mobileNumber
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.username_validate, container, false)
        ivBack = view.findViewById(R.id.iv_back)
        btnContinue = view.findViewById(R.id.btn_continue)
        userName = view.findViewById(R.id.user_name)
        emailText = view.findViewById(R.id.email_text)
        userNameDiv = view.findViewById(R.id.user_name_div)
        alertLay = view.findViewById(R.id.alert_lay)
        alertText = view.findViewById(R.id.alert_text)
        userNamePb = view.findViewById(R.id.user_name_pb)
        isNameAvailable = false
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        btnContinue!!.setOnClickListener {
            onContinue()
        }
        ivBack!!.setOnClickListener {
            activity!!.onBackPressed()
        }

        userName?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                alertLay?.visibility = View.GONE
                userNamePb?.visibility = View.GONE
                userNameDiv?.setBackgroundColor(context?.getColor(R.color.light_gray_white)!!)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        return view
    }

    private fun onContinue() {
        try {
            val name = userName!!.text!!.toString().trim { it <= ' ' }
            if (name.isEmpty()) {
                userName!!.error = "Required Name"
            } else {
                if (UserInterface.isValidEmail(emailText!!.text.toString().trim { it <= ' ' })) {
                    emailText?.isEnabled = false
                    alertLay?.visibility = View.GONE
                    userNamePb?.visibility = View.VISIBLE
                    UserInterface.hideKeyboard(context!!, emailText!!)
                    checkAvailable(userName?.text.toString().trim())
                } else {
                    if (emailText!!.text.toString().trim { it <= ' ' }.isEmpty())
                        emailText!!.error = "Required Email Id"
                    else
                        emailText!!.error = "Invalid Email Id"
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //UI update
    private fun updateAvailability() {
        try {
            userNamePb?.visibility = View.GONE
            alertLay?.visibility = View.VISIBLE
            if (isNameAvailable) {
                emailText?.isEnabled = true
                alertText?.text = getString(R.string.user_name_available)
                userNameDiv?.setBackgroundColor(context?.getColor(R.color.light_gray_white)!!)
                alertText?.setTextColor(context?.getColor(R.color.app_green)!!)
            } else {
                emailText?.isEnabled = true
                alertText?.text = getString(R.string.user_name_not_available)
                userNameDiv?.setBackgroundColor(context?.getColor(R.color.progress_select)!!)
                alertText?.setTextColor(context?.getColor(R.color.progress_select)!!)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkAvailable(name: String) {
        try {
            isNameAvailable = false
            val parseQuery = ParseQuery.getQuery<ParseObject>("devices")
            parseQuery.whereEqualTo("userName", name)
            parseQuery.limit = 1
            parseQuery.findInBackground { li, e ->
                if (e == null) {
                    isNameAvailable = false
                    if (li != null) {
                        if (li.size == 0) {
                            isNameAvailable = true
                            PesaApplication.getChildFragmentManager()!!.beginTransaction()
                                .replace(
                                    R.id.shareg_main_container,
                                    PasswordEnterFragment.newInstance(
                                        mobileNumber!!,
                                        userName!!.text.toString().trim(),
                                        emailText!!.text.toString().trim()
                                    )
                                )
                                .commitAllowingStateLoss()
                        }
                    }
                }
                updateAvailability()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}*/
