package finance.pesa.sdk.view.UI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.HapticFeedbackConstants
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.mukesh.OtpView
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.LOCK_CODE_RESULT
import finance.pesa.sdk.utils.UserInterface

class LockScreen : AppCompatActivity(), View.OnClickListener {

    private var lockView: OtpView? = null
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_launch_pwd)
        lockView = findViewById(R.id.lock_view)
        dialOne = findViewById(R.id.dial_pad_1)
        dialOne!!.setOnClickListener(this)
        dialTwo = findViewById(R.id.dial_pad_2)
        dialTwo!!.setOnClickListener(this)
        dialThree = findViewById(R.id.dial_pad_3)
        dialThree!!.setOnClickListener(this)
        dialFour = findViewById(R.id.dial_pad_4)
        dialFour!!.setOnClickListener(this)
        dialFive = findViewById(R.id.dial_pad_5)
        dialFive!!.setOnClickListener(this)
        dialSix = findViewById(R.id.dial_pad_6)
        dialSix!!.setOnClickListener(this)
        dialSeven = findViewById(R.id.dial_pad_7)
        dialSeven!!.setOnClickListener(this)
        dialEight = findViewById(R.id.dial_pad_8)
        dialEight!!.setOnClickListener(this)
        dialNine = findViewById(R.id.dial_pad_9)
        dialNine!!.setOnClickListener(this)
        dialZero = findViewById(R.id.dial_pad_0)
        dialZero!!.setOnClickListener(this)
        UserInterface.changeStatusBar(this, R.color.white_black)
        UserInterface.changeNavigationBar(this, R.color.white_black)
        PesaApplication.setIsLockShowing(true)
        val backSpace = findViewById<ImageButton>(R.id.backspace)
        backSpace.setOnClickListener {
            val length = lockView!!.selectionStart
            if (length > 0) {
                lockView!!.text?.delete(length - 1, length)
            }
        }
        backSpace.setOnLongClickListener {
            lockView!!.setText("")
            true
        }

        lockView?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    lockView?.setTextColor(getColor(R.color.app_green)!!)
                    if (s?.length == 6) {
                        if (Constants.getScreenPwd(this@LockScreen) == s.toString().trim()) {
                            PesaApplication.setIsLockShowing(false)
                            val resultIntent = Intent()
                            resultIntent.putExtra(LOCK_CODE_RESULT, "success")
                            setResult(Activity.RESULT_OK, resultIntent)
                            finish()
                        }else{
                            lockView?.setTextColor(getColor(R.color.progress_select)!!)
                            UserInterface.errorShow(lockView, this@LockScreen)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        UserInterface.hideKeyboard(this,lockView!!)

    }

    override fun onBackPressed() {
    }

    override fun onClick(v: View?) {
        onDigitClick(v as Button)
    }

    private fun onDigitClick(button: Button) {
        button.performHapticFeedback(HapticFeedbackConstants.KEYBOARD_TAP)
        lockView!!.text?.insert(lockView!!.selectionStart, button.text)
    }

}