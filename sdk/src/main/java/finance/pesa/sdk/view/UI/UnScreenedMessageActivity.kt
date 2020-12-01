package finance.pesa.sdk.view.UI

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.Model.Conversation
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.view.adapter.UnScreenedMessageAdapter
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
import java.text.SimpleDateFormat
import java.util.*

class UnScreenedMessageActivity : AppCompatActivity() {

    /**
     * The Conversation list.
     */
    private var convList: ArrayList<Conversation>? = null
    //private ArrayList<ParseObject> msgList;

    /**
     * The chat_layout adapter.
     */
    private var mChatAdapter: UnScreenedMessageAdapter? = null

    private var tvname: TextView? = null
    private var ic_back: ImageView? = null
    private var btn_accept: Button? = null
    private var btn_decline: TextView? = null
    /**
     * The user name of toUser.
     */
    private var toUser: String? = null
    private var fromUser: String? = null

    /**
     * The date of last message in conversation.
     */
    private var lastMsgDate: Date? = null

    /**
     * The date of first message in conversation.
     */
    private var firstMsgDate: Date? = null


    private var isAllChat = false
    private var isLoadindOldChats = false

    /**
     * The user.
     */
    private var chat_history: RecyclerView? = null
    private var parseLiveQueryClient: ParseLiveQueryClient? = null
    private var receiveSubscriptionHandling: SubscriptionHandling<ParseObject>? = null
    private var receiveParseQuery: ParseQuery<ParseObject>? = null

    private var updateUserSubscriptionHandling: SubscriptionHandling<ParseUser>? = null
    private var parseUserParseQuery: ParseQuery<ParseUser>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.un_screened_message)
        convList = ArrayList<Conversation>()
        chat_history = findViewById(R.id.chatlist)
        ic_back = findViewById(R.id.iv_back)
        tvname = findViewById(R.id.tvName)
        btn_accept = findViewById(R.id.btn_accept)
        btn_decline = findViewById(R.id.btn_decline)
        chat_history!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        chat_history!!.layoutManager = layoutManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.white, null)
        }
        fromUser = PesaApplication.getUserPhNo().replace("+", "")
        if (fromUser!!.isNullOrEmpty())
            finish()
        mChatAdapter =
            UnScreenedMessageAdapter(
                this@UnScreenedMessageActivity,
                convList!!
            )
        val sectionItemDecoration = finance.pesa.sdk.utils.ChatRecyclerSectionItemDecoration(
            resources.getDimensionPixelSize(R.dimen.btn_height_50),
            true,
            getSectionCallback(convList!!)
        )
        chat_history!!.addItemDecoration(sectionItemDecoration)
        chat_history!!.adapter = mChatAdapter

        toUser = intent.getStringExtra(Constants.TO_USER_ID).replace("+", "")
        tvname!!.text = toUser
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()
        ic_back?.setOnClickListener { onBackPressed() }
        btn_accept!!.setOnClickListener { onLoadAccept(1, toUser!!) }
        btn_decline!!.setOnClickListener { onLoadDecline(1, toUser!!) }
        chat_history!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemno = layoutManager.findFirstVisibleItemPosition()
                if (itemno == 0) onLoadOldChats()
            }
        })
    }


    private fun onLoadAccept(retryCount: Int, toNumber: String) {
        UserInterface.showProgress("Loading...", this@UnScreenedMessageActivity)
        val params = JSONObject()
        try {
            val jsonArray = JSONArray()
            if (Constants.getAllNumbers()!!.size > 1) {
                for (number in Constants.getAllNumbers()!!) {
                    val conversationId =
                        if (toNumber > number) toNumber + number else number + toNumber
                    jsonArray.put(conversationId)
                }
            } else {
                val number = Constants.getDefaultNumber()!!
                val conversationId =
                    if (toNumber > number) toNumber + number else number + toNumber
                jsonArray.put(conversationId)
            }
            params.put("conversation_id", jsonArray)
            params.put("from", toNumber)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<ResponseBody>? = ApiClient.build()
            ?.setAcceptScreen(body, Constants.getHeader(PesaApplication.getContext())!!)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (retryCount <= 2) {
                    val newRetry = retryCount + 1
                    onLoadAccept(newRetry, toNumber)
                } else {
                    UserInterface.hideProgress(this@UnScreenedMessageActivity)
                    UserInterface.showToast(this@UnScreenedMessageActivity, t.message.toString())
                }
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(this@UnScreenedMessageActivity)
                    if (response.isSuccessful) {
                        try {
                            Constants.isLoadUnScreen = true
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (!(response.message().contains("Invalid Keys") || response.code() === 400 || response.code() === 401 || response.code() === 402)) {
                            UserInterface.hideProgress(this@UnScreenedMessageActivity)
                            finish()
                        } else {
                            if (retryCount <= 2) {
                                val newRetry = retryCount + 1
                                onLoadAccept(newRetry, toNumber)
                            } else {
                                UserInterface.hideProgress(this@UnScreenedMessageActivity)
                                UserInterface.showToast(
                                    this@UnScreenedMessageActivity,
                                    response.message()
                                )
                            }
                        }
                    }
                }
            }
        })
    }


    private fun onLoadDecline(retryCount: Int, toNumber: String) {
        UserInterface.showProgress("Loading...", this@UnScreenedMessageActivity)
        val params = JSONObject()
        try {
            val jsonArray = JSONArray()
            if (Constants.getAllNumbers()!!.size > 1) {
                for (number in Constants.getAllNumbers()!!) {
                    val conversationId =
                        if (toNumber > number) toNumber + number else number + toNumber
                    jsonArray.put(conversationId)
                }
            } else {
                val number = Constants.getDefaultNumber()!!
                val conversationId =
                    if (toNumber > number) toNumber + number else number + toNumber
                jsonArray.put(conversationId)
            }
            params.put("conversation_id", jsonArray)
            params.put("from", toNumber)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        var body = params.toString().toRequestBody(JSON)
        var call: Call<ResponseBody>? = ApiClient.build()
            ?.setDeclineScreen(body, Constants.getHeader(PesaApplication.getContext())!!)
        call?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                if (retryCount <= 2) {
                    val newRetry = retryCount + 1
                    onLoadDecline(newRetry, toNumber)
                } else {
                    UserInterface.hideProgress(this@UnScreenedMessageActivity)
                    UserInterface.showToast(this@UnScreenedMessageActivity, t.message.toString())
                }
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(this@UnScreenedMessageActivity)
                    if (response.isSuccessful) {
                        try {
                            Constants.isLoadUnScreen = true
                            finish()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (!(response.message().contains("Invalid Keys") || response.code() === 400 || response.code() === 401 || response.code() === 402)) {
                            UserInterface.hideProgress(this@UnScreenedMessageActivity)
                            finish()
                        } else {
                            if (retryCount <= 2) {
                                val newRetry = retryCount + 1
                                onLoadDecline(newRetry, toNumber)
                            } else {
                                UserInterface.hideProgress(this@UnScreenedMessageActivity)
                                UserInterface.showToast(
                                    this@UnScreenedMessageActivity,
                                    response.message()
                                )
                            }
                        }
                    }
                }
            }
        })
    }

    private fun getSectionCallback(conversations: List<Conversation>): finance.pesa.sdk.utils.ChatRecyclerSectionItemDecoration.SectionCallback {
        return object : finance.pesa.sdk.utils.ChatRecyclerSectionItemDecoration.SectionCallback {
            override fun isSection(position: Int): Boolean {
                return when {
                    position < 0 -> false
                    position >= conversations.size -> false
                    else -> position == 0 || show(conversations[position].date) != show(
                        conversations[position - 1].date
                    )
                }
            }

            override fun getSectionHeader(position: Int): CharSequence {
                return when {
                    position < 0 -> ""
                    position >= conversations.size -> ""
                    UserInterface.isToday(conversations[position].date) -> "Today"
                    UserInterface.isYesterday(
                        conversations[position].date
                    ) -> "Yesterday"
                    else -> UserInterface.getChatDate(conversations[position].date)
                }
            }
        }
    }


    private fun show(date: Date): String {
        var day = ""
        try {
            val outputPattern = "yyyy-MM-dd"
            val format = SimpleDateFormat(outputPattern)
            return format.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return day
    }

    private fun onLoadOldChats() {
        if (applicationContext == null)
            return
        if (isLoadindOldChats)
            return
        if (firstMsgDate == null)
            return
        if (isAllChat)
            return
        isLoadindOldChats = true
        val parseQuery = ParseQuery.getQuery<ParseObject>("unscreened_messages")
        parseQuery!!.whereContainedIn("screen_to", Constants.getAllNumbers())
        parseQuery!!.whereEqualTo("from", toUser)
        parseQuery.whereLessThan("createdAt", firstMsgDate)
        parseQuery.orderByDescending("createdAt")
        parseQuery.limit = 25
        parseQuery.findInBackground { li, e ->
            if (e == null)
                if (applicationContext != null)
                    if (li != null)
                        if (li!!.size > 0) {
                            val oldMessages = ArrayList<Conversation>()
                            for (i in li!!.indices.reversed()) {
                                // for (int i = 0; i < li.size(); i++) {
                                val po = li!![i]
                                var message = po.getString("message").toString()
                                try {
                                    if (po.getBoolean("is_encrypted"))
                                        message = UserInterface.cyberDecrypt(
                                            po.getString("message").toString(),
                                            applicationContext
                                        )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                var toNumberData = po.getString("to").toString()
                                if (toNumberData == "none")
                                    toNumberData = po.getString("screen_to").toString()
                                var deleteNumber = po.get("from")
                                if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                                    deleteNumber = toNumberData
                                val c = Conversation(
                                    message,
                                    po.getString("status").toString(),
                                    po.getBoolean("is_delivery"),
                                    po.getBoolean("is_read"),
                                    po.createdAt,
                                    po.getString("chat_id").toString(),
                                    po.getString("conversation_id").toString(),
                                    po.getString("from").toString(),
                                    toNumberData,
                                    po.getBoolean("delete_from_" + deleteNumber!!),
                                    po.getBoolean("delete_for_everyone")
                                )
                                oldMessages.add(c)
                                if (firstMsgDate!!.after(c.date))
                                    firstMsgDate = c.date

                            }
                            mChatAdapter!!.addlisttop(oldMessages)
                            isLoadindOldChats = false
                        } else {
                            isAllChat = true
                        }
        }
    }

    private fun onLoadPreviousChats() {
        val parseQuery = ParseQuery.getQuery<ParseObject>("unscreened_messages")
        parseQuery!!.whereContainedIn("screen_to", Constants.getAllNumbers())
        parseQuery!!.whereEqualTo("from", toUser)
        if (lastMsgDate != null)
            parseQuery.whereGreaterThan("createdAt", lastMsgDate)
        parseQuery.orderByDescending("createdAt")
        parseQuery.limit = 25
        parseQuery.findInBackground { li, e ->
            if (applicationContext != null)
                if (li != null)
                    if (li!!.size > 0) {
                        for (i in li!!.indices.reversed()) {
                            val po = li!![i]
                            var message = po.getString("message").toString()
                            try {
                                if (po.getBoolean("is_encrypted"))
                                    message = UserInterface.cyberDecrypt(
                                        po.getString("message").toString(),
                                        applicationContext
                                    )
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                            var deleteNumber = po.get("from")
                            var toNumberData = po.getString("to").toString()
                            if (toNumberData == "none")
                                toNumberData = po.getString("screen_to").toString()
                            if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                                deleteNumber = toNumberData
                            val c = Conversation(
                                message,
                                po.getString("status").toString(),
                                po.getBoolean("is_delivery"),
                                po.getBoolean("is_read"),
                                po.createdAt,
                                po.getString("chat_id").toString(),
                                po.getString("conversation_id").toString(),
                                po.getString("from").toString(),
                                toNumberData,
                                po.getBoolean("delete_from_" + deleteNumber!!),
                                po.getBoolean("delete_for_everyone")
                            )
                            convList!!.add(c)
                            if (lastMsgDate == null)
                                firstMsgDate = c.date
                            if (lastMsgDate == null || lastMsgDate!!.before(c.date))
                                lastMsgDate = c.date
                        }
                        mChatAdapter!!.notifyDataSetChanged()
                        chat_history!!.scrollToPosition(mChatAdapter!!.itemCount - 1)
                    }
        }
    }

    override fun onResume() {
        super.onResume()
        PesaApplication.currentActivityContext = this@UnScreenedMessageActivity
        onLoadPreviousChats()
        receivedCallBack()
        Constants.CHAT_TO_NUMBER = toUser!!
        var notificationNo = Constants.NOTIFICATION_ID
        try {
            val input = toUser
            if (input!!.length > 4) {
                notificationNo = Integer.parseInt(input!!.substring(input!!.length - 4))
            }
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager!!.cancel(notificationNo)
    }

    override fun onDestroy() {
        Constants.CHAT_TO_NUMBER = ""
        try {
            if (updateUserSubscriptionHandling != null) {
                parseLiveQueryClient!!.unsubscribe(parseUserParseQuery)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        super.onDestroy()

    }

    override fun onPause() {
        super.onPause()
        stopReceivedCallBack()
    }


    private fun stopReceivedCallBack() {
        try {
            if (receiveSubscriptionHandling != null) {
                parseLiveQueryClient!!.unsubscribe(receiveParseQuery)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun receivedCallBack() {
        receiveParseQuery = ParseQuery.getQuery<ParseObject>("unscreened_messages")
        receiveParseQuery!!.whereContainedIn("screen_to", Constants.getAllNumbers())
        receiveParseQuery!!.whereEqualTo("from", toUser)
        receiveParseQuery!!.orderByDescending("createdAt")
        receiveParseQuery!!.limit = 50
        receiveSubscriptionHandling = parseLiveQueryClient!!.subscribe(receiveParseQuery)
        // Listen for CREATE events
        receiveSubscriptionHandling!!.handleEvent(
            SubscriptionHandling.Event.CREATE
        ) { query, po ->
            if (applicationContext != null) {
                var message = po.getString("message").toString()
                try {
                    if (po.getBoolean("is_encrypted"))
                        message = UserInterface.cyberDecrypt(
                            po.getString("message").toString(),
                            applicationContext
                        )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                var deleteNumber = po.get("from")
                var toNumberData = po.getString("to").toString()
                if (toNumberData == "none")
                    toNumberData = po.getString("screen_to").toString()
                if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                    deleteNumber = toNumberData
                val c = Conversation(
                    message,
                    po.getString("status").toString(),
                    po.getBoolean("is_delivery"),
                    po.getBoolean("is_read"),
                    po.createdAt,
                    po.getString("chat_id").toString(),
                    po.getString("conversation_id").toString(),
                    po.getString("from").toString(),
                    toNumberData,
                    po.getBoolean("delete_from_" + deleteNumber!!),
                    po.getBoolean("delete_for_everyone")
                )
                convList!!.add(c)
                lastMsgDate = po.createdAt
                Constants.isLoadUnScreen = true
                runOnUiThread {
                    mChatAdapter!!.notifyDataSetChanged()
                    chat_history!!.smoothScrollToPosition(mChatAdapter!!.itemCount - 1)
                }
            }
        }
    }

}
