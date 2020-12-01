package finance.pesa.sdk.view.UI

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.ParseObject
import com.parse.ParseQuery
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ContactDetailsActivity : AppCompatActivity() {

    //private var callActionFab: FloatingActionButton? = null
    private var messageActionFab: FloatingActionButton? = null
    private var blockActionFab: FloatingActionButton? = null
    private var contact_name: TextView? = null
    private var contact_number: TextView? = null
    private var profile_image: ImageView? = null
    private var iv_back: ImageView? = null
    private var contactNumber: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_details)
        iv_back = findViewById(R.id.iv_back)
        profile_image = findViewById(R.id.profile_image)
        contact_name = findViewById(R.id.contact_name)
        contact_number = findViewById(R.id.contact_number)
        // callActionFab = findViewById(R.id.call_action_fab)
        messageActionFab = findViewById(R.id.message_action_fab)
        blockActionFab = findViewById(R.id.block_action_fab)
        UserInterface.changeStatusBar(this,R.color.white)
        if (intent.getStringExtra(Constants.CONTACT_NUMBER) != null) {
            val number: String =
                intent.getStringExtra(Constants.CONTACT_NUMBER)
            contact_number!!.text = UserInterface.formatPhone(number)
            UserInterface.retrieveContactPhoto(
                this,
                number,
                profile_image!!,
                contact_name!!
            )
            // callActionFab!!.isClickable = true
            messageActionFab!!.isClickable = true
            blockActionFab!!.isClickable = false
            contactNumber = number
        }
        iv_back!!.setOnClickListener { finish() }

        blockActionFab!!.setOnClickListener { onBlockedContact(1, contactNumber!!) }
        messageActionFab!!.setOnClickListener {
            try {
                val callee =
                    contactNumber.toString().trim().replace("-", "").replace("(", "")
                        .replace(")", "")
                        .replace(" ", "")
                val startIntent = Intent(this, NewMessageActivity::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startIntent.putExtra("toUserNumber", callee)
                startActivity(startIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //UserInterface.onMessageHistoryTab()
            finish()
        }


        checkBlockContact()
    }

    private fun checkBlockContact() {
        try {
            if ((Constants.getDeviceIdFromLocal(this)) == null) {
                return
            }
            runOnUiThread {
                val parseQuery = ParseQuery<ParseObject>("blocked_contacts")
                parseQuery.whereEqualTo(
                    "deviceId",
                    Constants.getDeviceIdFromLocal(this)
                )
                parseQuery!!.limit = 1
                parseQuery.findInBackground { li, e ->
                    if (this != null)
                        if (li != null)
                            if (li!!.size > 0) {
                                var allBlockedDatas: JSONArray? = JSONArray()
                                try {
                                    allBlockedDatas = li[0].getJSONArray("contactInfo")
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                                if (allBlockedDatas!!.length() > 0) {
                                    for (i in 0 until allBlockedDatas.length()) {
                                        val item = allBlockedDatas.getJSONObject(i)
                                        if ((item.getString("phoneNumber").replace("+", "").replace(
                                                " ",
                                                ""
                                            )) == (contactNumber!!.replace("+", "").replace(
                                                " ",
                                                ""
                                            ))
                                        ) {
                                            blockActionFab!!.isClickable = false
                                            blockActionFab!!.backgroundTintList =
                                                ColorStateList.valueOf(
                                                    ContextCompat.getColor(
                                                        this,
                                                        R.color.cal_red
                                                    )
                                                )
                                            blockActionFab!!.imageTintList = ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    this,
                                                    R.color.white
                                                )
                                            )
                                            break
                                        }
                                    }
                                } else {
                                    blockActionFab!!.isClickable = true
                                    blockActionFab!!.backgroundTintList =
                                        ColorStateList.valueOf(
                                            ContextCompat.getColor(
                                                this,
                                                R.color.tab_bg_color
                                            )
                                        )
                                    blockActionFab!!.imageTintList = ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this,
                                            R.color.phone_txt
                                        )
                                    )
                                }
                            } else {
                                blockActionFab!!.isClickable = true
                                blockActionFab!!.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this,
                                            R.color.tab_bg_color
                                        )
                                    )
                                blockActionFab!!.imageTintList = ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this,
                                        R.color.phone_txt
                                    )
                                )
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Blocked Contact
    private fun onBlockedContact(retryCount: Int, number: String) {
        UserInterface.showProgress("Loading...", this)
        val params = JSONObject()
        try {
            params.put("from", number)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<ResponseBody>? = ApiClient.build()
            ?.blockContact(body, Constants.getHeader(PesaApplication.getContext())!!)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (retryCount <= 2) {
                    val newRetry = retryCount + 1
                    onBlockedContact(newRetry, number)
                } else {
                    UserInterface.hideProgress(this@ContactDetailsActivity)
                    UserInterface.showToast(this@ContactDetailsActivity, t.message.toString())
                }
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(this@ContactDetailsActivity)
                    if (response.isSuccessful) {
                        try {
                            blockActionFab!!.isClickable = false
                            blockActionFab!!.backgroundTintList =
                                ColorStateList.valueOf(
                                    ContextCompat.getColor(
                                        this@ContactDetailsActivity,
                                        R.color.cal_red
                                    )
                                )
                            blockActionFab!!.imageTintList = ColorStateList.valueOf(
                                ContextCompat.getColor(
                                    this@ContactDetailsActivity,
                                    R.color.white
                                )
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (!(response.message().contains("Invalid Keys") || response.code() === 400 || response.code() === 401 || response.code() === 402)) {
                            UserInterface.hideProgress(this@ContactDetailsActivity)
                            finish()
                        } else {
                            if (retryCount <= 2) {
                                val newRetry = retryCount + 1
                                onBlockedContact(newRetry, number)
                            } else {
                                UserInterface.hideProgress(this@ContactDetailsActivity)
                                UserInterface.showToast(
                                    this@ContactDetailsActivity,
                                    response.message()
                                )
                            }
                        }
                    }
                }
            }
        })
    }


    override fun onResume() {
        PesaApplication.currentActivityContext = this@ContactDetailsActivity
        super.onResume()
    }
}