package finance.pesa.sdk.view.UI

import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.*
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.Model.Conversation
import finance.pesa.sdk.Model.ConversationData
import finance.pesa.sdk.Model.UserDetailsListener
import finance.pesa.sdk.R
import finance.pesa.sdk.view.adapter.ChatListAdapter
import finance.pesa.sdk.utils.ChatHistorySelectListener
import finance.pesa.sdk.utils.ChatRecyclerSectionItemDecoration
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.Constants.NOTIFICATION_ID
import finance.pesa.sdk.utils.UserInterface
import kotlinx.android.synthetic.main.chat_layout.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.Map

class ChatActivity : AppCompatActivity() {

    /**
     * The Conversation list.
     */
    private var convList: ArrayList<Conversation>? = null
    //private ArrayList<ParseObject> msgList;

    /**
     * The chat_layout adapter.
     */
    private var mChatAdapter: ChatListAdapter? = null

    private var btnSend: ImageView? = null
    private var tvname: TextView? = null
    private var reloadCredits: TextView? = null
    private var ic_back: ImageView? = null
    private var iv_user_icon: ImageView? = null
    /**
     * The Editext to compose the message.
     */
    private var etxtMessage: EditText? = null

    /**
     * The user name of toUser.
     */
    private var toUser: String? = null
    private var fromUser: String? = null
    private var conversationId: String? = null

    /**
     * The date of last message in conversation.
     */
    private var lastMsgDate: Date? = null

    /**
     * The date of first message in conversation.
     */
    private var firstMsgDate: Date? = null


    private var isAllChat = false
    private var isMessageHistoryLoad = false
    private var isLoadindOldChats = false

    /**
     * The user.
     */
    private var chat_history: RecyclerView? = null
    private var parseLiveQueryClient: ParseLiveQueryClient? = null
    private var updateSubscriptionHandling: SubscriptionHandling<ParseObject>? = null
    private var receiveSubscriptionHandling: SubscriptionHandling<ParseObject>? = null
    private var receiveParseQuery: ParseQuery<ParseObject>? = null
    private var updateParseQuery: ParseQuery<ParseObject>? = null

    private var updateUserSubscriptionHandling: SubscriptionHandling<ParseUser>? = null
    private var parseUserParseQuery: ParseQuery<ParseUser>? = null


    private var action_popup: LinearLayout? = null
    private var iv_delete: ImageView? = null
    private var iv_back_popup: ImageView? = null
    private var iv_call_details: ImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chat_layout)
        convList = ArrayList<Conversation>()
        chat_history = findViewById(R.id.chatlist)
        btnSend = findViewById(R.id.btnSend)
        ic_back = findViewById(R.id.iv_back)
        iv_user_icon = findViewById(R.id.iv_user_icon)
        tvname = findViewById(R.id.tvName)
        reloadCredits = findViewById(R.id.get_credit)
        action_popup = findViewById(R.id.action_popup)
        iv_delete = findViewById(R.id.iv_delete)
        iv_call_details = findViewById(R.id.iv_call_details)
        iv_back_popup = findViewById(R.id.iv_back_popup)
        chat_history!!.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        chat_history!!.layoutManager = layoutManager
        UserInterface.changeStatusBar(this, R.color.white_black)
        fromUser = PesaApplication.getUserPhNo().replace("+", "")
        if (fromUser!!.isNullOrEmpty())
            finish()
        mChatAdapter =
            ChatListAdapter(
                this@ChatActivity,
                Constants.getAllNumbers()!!,
                convList!!,
                chatHistorySelectListener
            )
        val sectionItemDecoration = finance.pesa.sdk.utils.ChatRecyclerSectionItemDecoration(
            resources.getDimensionPixelSize(R.dimen.btn_height_50),
            true,
            getSectionCallback(convList!!)
        )
        chat_history!!.addItemDecoration(sectionItemDecoration)
        chat_history!!.adapter = mChatAdapter
        etxtMessage = findViewById<View>(R.id.txt) as EditText
        /*etxtMessage.setInputType(InputType.TYPE_CLASS_TEXT
                       | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
       */
        toUser = intent.getStringExtra(Constants.TO_USER_ID).replace("+", "")
        UserInterface.retrieveContactPhotoWithName(
            applicationContext!!,
            toUser!!,
            iv_user_icon!!,
            tvname!!
        )
        val uid = Constants.getDefaultNumber()!!
        conversationId = if (toUser!! > uid) toUser!! + uid else uid + toUser
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()
        msgSeenCallBack()
        btnSend?.setOnClickListener { sendMessage() }


        ic_back?.setOnClickListener { onBackPressed() }

        iv_delete?.setOnClickListener { v -> onDeleteUpdate() }

        iv_back_popup?.setOnClickListener { v ->
            mChatAdapter?.removeAllSelecter()
            onSelectedUI(-1)
        }

        chat_history!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemno = layoutManager.findFirstVisibleItemPosition()
                if (itemno == 0) onLoadOldChats()
            }
        })

        reloadCredits!!.setOnClickListener {
            finish()
        }

        iv_call_details!!.setOnClickListener {
            val startIntent = Intent(applicationContext, ContactDetailsActivity::class.java)
            startIntent.putExtra(Constants.CONTACT_NUMBER, toUser)
            startActivityForResult(startIntent, 1011)
        }


    }


    override fun onBackPressed() {
        if (action_popup?.visibility == View.VISIBLE) {
            mChatAdapter?.removeAllSelecter()
            onSelectedUI(-1)
        } else {
            finish()
        }
    }

    private val chatHistorySelectListener = object : ChatHistorySelectListener {
        override fun onSelected(conversation: Conversation, pos: Int) {
            if (mChatAdapter?.getSelectedConversation()!!.containsKey(conversation.chat_id)) {
                mChatAdapter?.removeSelecter(conversation)
            } else {
                mChatAdapter?.addSelecter(conversation)
            }
            onSelectedUI(pos)
        }
    }

    private fun onSelectedUI(pos: Int) {
        try {
            if (mChatAdapter?.getSelectedConversation()!!.size === 0) {
                mChatAdapter?.updateSelected(false)
                action_popup?.visibility = View.GONE
            } else {
                mChatAdapter?.updateSelected(true)
                action_popup?.visibility = View.VISIBLE
            }
            runOnUiThread {
                if (pos >= 0) {
                    mChatAdapter?.notifyItemChanged(pos)
                } else {
                    mChatAdapter?.notifyDataSetChanged()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun onDeleteUpdate() {
        try {
            if (mChatAdapter?.getSelectedConversation()!!.size === 1) {
                val it = mChatAdapter?.getSelectedConversation()!!.iterator()
                var singleConversation: Conversation? = null
                while (it.hasNext()) {
                    val pair = it.next() as Map.Entry<String, Conversation>
                    singleConversation = pair.value
                }
                if (singleConversation != null) {
                    deleteForMe(singleConversation)
                    /* if (singleConversation!!.from == fromUser!!) {
                         if (singleConversation!!.deletedForEveryOne!!)
                             deleteForMe(singleConversation)
                         else
                             deleteForAll(singleConversation)
                     } else {
                         deleteForMe(singleConversation)
                     }*/
                }
            } else {
                deleteMultipleConversation()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }


    private fun deleteMultipleConversation() {
        try {
            val toUserName = tvname?.text.toString().trim { it <= ' ' }
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Delete message from $toUserName")
            builder.setPositiveButton(
                "Delete"
            ) { dialog, id ->
                dialog.cancel()
                val it = mChatAdapter?.getSelectedConversation()!!.iterator()
                val allSelectedConversation = ArrayList<String>()
                while (it.hasNext()) {
                    val pair = it.next() as Map.Entry<String, Conversation>
                    allSelectedConversation.add(pair.key)
                }
                val parseQuery = ParseQuery.getQuery<ParseObject>("messages")
                parseQuery.whereContainedIn("chat_id", allSelectedConversation)
                    .findInBackground { objects, e ->
                        if (objects != null) {
                            if (objects!!.size > 0) {
                                for (po in objects!!) {
                                    var deleteNumber = po.get("from")
                                    if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                                        deleteNumber = po.get("to")
                                    po.put(
                                        "delete_from_" + deleteNumber!!, true
                                    )
                                    po.saveEventually()
                                }
                                isMessageHistoryLoad = true
                            }
                        }
                    }
                mChatAdapter?.removeAllSelecter()
                onSelectedUI(-1)
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialog, id -> dialog.cancel() }

            builder.create().show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun deleteForMe(c: Conversation) {
        try {
            val toUserName = tvname?.text.toString().trim { it <= ' ' }
            val builder = AlertDialog.Builder(this)
            // builder.setTitle("Test");
            // builder.setIcon(R.drawable.icon_file_doc);
            builder.setMessage("Delete message from $toUserName")
            builder.setPositiveButton(
                "Delete"
            ) { dialog, id ->
                dialog.cancel()
                val parseQuery = ParseQuery.getQuery<ParseObject>("messages")
                parseQuery.whereEqualTo("chat_id", c.chat_id)
                    .findInBackground { objects, e ->
                        if (objects != null) {
                            if (objects!!.size > 0) {
                                val po = objects!![0]
                                var deleteNumber = po.get("from")
                                if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                                    deleteNumber = po.get("to")
                                po.put(
                                    "delete_from_" + deleteNumber!!, true
                                )
                                po.saveEventually()
                                isMessageHistoryLoad = true
                            }
                        }
                    }
                mChatAdapter?.removeAllSelecter()
                onSelectedUI(-1)
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialog, id -> dialog.cancel() }

            builder.create().show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun deleteForAll(c: Conversation) {
        try {
            val builder = AlertDialog.Builder(this)
            // builder.setTitle("Test");
            // builder.setIcon(R.drawable.icon_file_doc);
            builder.setMessage("Delete message?")
            builder.setPositiveButton(
                "Delete for me"
            ) { dialog, id ->
                dialog.cancel()
                val parseQuery = ParseQuery.getQuery<ParseObject>("messages")
                parseQuery.whereEqualTo("chat_id", c.chat_id)
                    .findInBackground { objects, e ->
                        if (objects != null) {
                            if (objects!!.size > 0) {
                                val po = objects!![0]
                                var deleteNumber = po.get("from")
                                if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                                    deleteNumber = po.get("to")
                                po.put(
                                    "delete_from_" + deleteNumber!!, true
                                )
                                po.saveEventually()
                            }
                        }
                    }
                mChatAdapter?.removeAllSelecter()
                onSelectedUI(-1)
            }

            builder.setNegativeButton(
                "Cancel"
            ) { dialog, id -> dialog.cancel() }

            builder.setNeutralButton(
                "Delete for everyone"
            ) { dialog, id ->
                dialog.cancel()
                val parseQuery = ParseQuery.getQuery<ParseObject>("messages")
                parseQuery.whereEqualTo("chat_id", c.chat_id)
                    .findInBackground { objects, e ->
                        if (objects != null) {
                            if (objects!!.size > 0) {
                                val po = objects!![0]
                                po.put("delete_for_everyone", true)
                                po.saveEventually()
                            }
                        }
                    }
                mChatAdapter?.removeAllSelecter()
                onSelectedUI(-1)
            }


            builder.create().show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

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


    private fun msgSeenCallBack() {
        val query1 = ParseQuery.getQuery<ParseObject>("messages")
        query1.whereContainedIn("from", Constants.getAllNumbers())
        query1.whereEqualTo("to", toUser)

        val query2 = ParseQuery.getQuery<ParseObject>("messages")
        query2.whereContainedIn("to", Constants.getAllNumbers())
        query2.whereEqualTo("from", toUser)

        val list = ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)

        updateParseQuery = ParseQuery.or(list)
        updateParseQuery!!.orderByDescending("createdAt")
        updateParseQuery!!.limit = 50
        updateSubscriptionHandling = parseLiveQueryClient!!.subscribe(updateParseQuery)
        updateSubscriptionHandling!!.handleEvent(
            SubscriptionHandling.Event.UPDATE
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
                if (c.isDeleted!!) run { runOnUiThread { mChatAdapter?.updateStatus(c) } }
                else if (c.deletedForEveryOne!! || Constants.getAllNumbers()!!.contains(c.from)) {
                    val updatedPos = mChatAdapter!!.updateStatus(c)
                    runOnUiThread { mChatAdapter!!.notifyItemChanged(updatedPos) }
                } else if (c.deletedForEveryOne!! || Constants.getAllNumbers()!!.contains(c.to)) {
                    if (!containsChatId(c.chat_id)) {
                        convList!!.add(c)
                        po.put("is_delivery", true)
                        po.put("is_read", true)
                        po.put("status", "success")
                        po.saveEventually()
                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread {
                            mChatAdapter!!.notifyDataSetChanged()
                            chat_history!!.smoothScrollToPosition(mChatAdapter!!.itemCount - 1)
                        }
                        isMessageHistoryLoad = true
                    }
                }
            }
        }
    }

    private fun receivedCallBack() {
        val query1 = ParseQuery.getQuery<ParseObject>("messages")
        query1.whereContainedIn("from", Constants.getAllNumbers())
        query1.whereEqualTo("to", toUser)

        val query2 = ParseQuery.getQuery<ParseObject>("messages")
        query2.whereContainedIn("to", Constants.getAllNumbers())
        query2.whereEqualTo("from", toUser)

        /* val query3 = ParseQuery.getQuery<ParseObject>("messages")
         query3.whereContainedIn("from", Constants.getAllNumbers())
         query3.whereEqualTo("to", "none")
         query3.whereEqualTo("screen_to", toUser)*/

        val list = ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)
        // list.add(query3)

        receiveParseQuery = ParseQuery.or(list)
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
                lastMsgDate = po.createdAt
                if (Constants.getAllNumbers()!!.contains(po.getString("to"))) {
                    if (!containsChatId(c.chat_id)) {
                        convList!!.add(c)
                        po.put("is_delivery", true)
                        po.put("is_read", true)
                        po.put("status", "success")
                        po.saveEventually()
                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread {
                            mChatAdapter!!.notifyDataSetChanged()
                            chat_history!!.smoothScrollToPosition(mChatAdapter!!.itemCount - 1)
                        }
                    }
                }
                isMessageHistoryLoad = true
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1011 -> {
                if (resultCode == RESULT_OK)
                    finish()
            }
        }

    }

    private fun containsChatId(chatId: String): Boolean {
        return convList!!.stream().map(Conversation::chat_id).filter(chatId::equals).findFirst()
            .isPresent
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
        val query1 = ParseQuery.getQuery<ParseObject>("messages")
        query1.whereContainedIn("from", Constants.getAllNumbers())
        query1.whereEqualTo("to", toUser)

        val query2 = ParseQuery.getQuery<ParseObject>("messages")
        query2.whereContainedIn("to", Constants.getAllNumbers())
        query2.whereEqualTo("from", toUser)

        val query3 = ParseQuery.getQuery<ParseObject>("messages")
        query3.whereContainedIn("from", Constants.getAllNumbers())
        query3.whereEqualTo("to", "none")
        query3.whereEqualTo("screen_to", toUser)


        val list = ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)
        list.add(query3)

        val parseQuery = ParseQuery.or(list)
        parseQuery.whereLessThan("createdAt", firstMsgDate)
        for (number in Constants.getAllNumbers()!!) {
            parseQuery.whereNotEqualTo("delete_from_" + number, true)
        }
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
                                if (Constants.getAllNumbers()!!.contains(po.getString("to")))
                                    if (!c.isRead!!) {
                                        po.put("is_delivery", true)
                                        po.put("is_read", true)
                                        po.put("status", "success")
                                        po.saveEventually()
                                        isMessageHistoryLoad = true
                                    }
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
        val query1 = ParseQuery.getQuery<ParseObject>("messages")
        query1.whereContainedIn("from", Constants.getAllNumbers())
        query1.whereEqualTo("to", toUser)

        val query2 = ParseQuery.getQuery<ParseObject>("messages")
        query2.whereContainedIn("to", Constants.getAllNumbers())
        query2.whereEqualTo("from", toUser)

        val query3 = ParseQuery.getQuery<ParseObject>("messages")
        query3.whereContainedIn("from", Constants.getAllNumbers())
        query3.whereEqualTo("to", "none")
        query3.whereEqualTo("screen_to", toUser)

        val list = ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)
        list.add(query3)

        val parseQuery = ParseQuery.or(list)

        if (lastMsgDate != null)
            parseQuery.whereGreaterThan("createdAt", lastMsgDate)
        for (number in Constants.getAllNumbers()!!) {
            parseQuery.whereNotEqualTo("delete_from_" + number, true)
        }
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
                            if (!containsChatId(c.chat_id)) {
                                convList!!.add(c)
                            }
                            if (Constants.getAllNumbers()!!.contains(po.getString("to")))
                                if (!c.isRead!!) {
                                    po.put("is_delivery", true)
                                    po.put("is_read", true)
                                    po.put("status", "success")
                                    po.saveEventually()
                                    isMessageHistoryLoad = true
                                }
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
        PesaApplication.currentActivityContext = this@ChatActivity
        onLoadPreviousChats()
        receivedCallBack()
        //msgSeenCallBack();
        Constants.CHAT_TO_NUMBER = toUser!!
        var notificationNo = NOTIFICATION_ID
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
        if (isMessageHistoryLoad) {
            isMessageHistoryLoad = false
            UserInterface.onRefreshMessageHistory()
        }
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
        Constants.CHAT_TO_NUMBER = ""
        stopReceivedCallBack()
        //stopMsgSeenCallBack();
    }

    private fun stopMsgSeenCallBack() {
        try {
            if (updateSubscriptionHandling != null) {
                parseLiveQueryClient!!.unsubscribe(updateParseQuery)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

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

    private fun sendMessage() {
        try {
            // if (Constants.getActivatedNumber()!!.isEmpty()) {
            if (Constants.getDefaultNumber() == null) {
                return
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.hideSoftInputFromWindow(etxtMessage!!.windowToken, 0)
            if (etxtMessage!!.length() == 0)
                return
            if (UserInterface.getAllowedMsg(toUser!!, this@ChatActivity)!!) {
                val s = etxtMessage!!.text.toString()
                val chatId = random()
                val c = Conversation(
                    s,
                    "pending",
                    false,
                    false,
                    Date(),
                    chatId,
                    conversationId!!,
                    Constants.getDefaultNumber()!!,
                    toUser!!, false, false
                )
                convList!!.add(c)
                mChatAdapter!!.notifyDataSetChanged()
                chat_history!!.scrollToPosition(mChatAdapter!!.itemCount - 1)
                etxtMessage!!.text = null
                var isEncrypted = false
                var enCryptedMsg = s
                try {
                    enCryptedMsg = UserInterface.cyberEncrypt(s, applicationContext)
                    isEncrypted = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val po = finance.pesa.sdk.Model.ConversationData()
                    po.put("from", Constants.getDefaultNumber()!!)
                    po.put("to", toUser!!)
                    // po.put("to", "none")
                    po.put("screen_to", toUser!!)
                    po.put("is_screening", false)
                    po.put("conversation_id", conversationId!!)
                    po.put("message", enCryptedMsg)
                    po.put("is_encrypted", isEncrypted)
                    po.put("is_delivery", false)
                    po.put("is_read", false)
                    po.put("chat_id", chatId)
                    po.put("status", "pending")
                    po.put("direction", "outbound")
                    po.put("delete_from_" + Constants.getDefaultNumber()!!, false)
                    po.put("delete_for_everyone", false)
                    po.saveEventually { e ->
                        if (applicationContext != null) {
                            if (e == null) {
                                isMessageHistoryLoad = true
                            } else
                                c.status = "failed"
                            mChatAdapter!!.notifyDataSetChanged()
                        }
                    }
                    isMessageHistoryLoad = true
                } catch (e: Exception) {
                    e.message
                }
            } else {
    return
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    companion object {

        fun random(): String {
            return UUID.randomUUID().toString()
        }
    }


}
