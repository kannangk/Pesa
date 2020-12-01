package finance.pesa.sdk.view.UI

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.parse.livequery.ParseLiveQueryClient
import com.parse.livequery.SubscriptionHandling
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.Model.ContactDetails
import finance.pesa.sdk.Model.Conversation
import finance.pesa.sdk.Model.ConversationData
import finance.pesa.sdk.Model.UserDetailsListener
import finance.pesa.sdk.R
import finance.pesa.sdk.view.adapter.ChatListAdapter
import finance.pesa.sdk.utils.ChatHistorySelectListener
import finance.pesa.sdk.utils.ChatRecyclerSectionItemDecoration
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import kotlinx.android.synthetic.main.new_chat_message.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


class NewMessageActivity : AppCompatActivity() {

    private var ic_back: ImageView? = null
    private var contact_btn: ImageView? = null
    private var contact_view: LinearLayout? = null
    var caller_name: TextView? = null
    var caller_number: TextView? = null
    var iv_user_icon: ImageView? = null
    /**
     * The Editext to compose the message.
     */
    private var etxtMessage: EditText? = null
    private var txt_new_number: EditText? = null
    private var btnSend: ImageView? = null
    private val contactPermissions = arrayOf(
        android.Manifest.permission.READ_CONTACTS,
        android.Manifest.permission.WRITE_CONTACTS
    )
    private val CONTACT_PERMISSION_REQUEST_CODE = 18

    /**
     * The Conversation list.
     */
    private var convList: ArrayList<Conversation>? = null
    //private ArrayList<ParseObject> msgList;

    /**
     * The chat_layout adapter.
     */
    private var mChatAdapter: ChatListAdapter? = null

    private var tvname: TextView? = null
    private var reloadCredits: TextView? = null
    private var iv_header_user_icon: ImageView? = null
    private var icon_lay: LinearLayout? = null
    private var iv_call_details: ImageView? = null
    /**
     * The user name of toUser.
     */
    private var toUser: String? = null
    private var conversationId: String? = null
    private var receiveCallBack: Boolean? = false

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
    private var isMessageHistoryLoad = false
    private var fromUser: String? = null

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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_chat_message)
        btnSend = findViewById(R.id.btnSend)
        ic_back = findViewById(R.id.iv_back)
        contact_btn = findViewById(R.id.contact_btn)
        etxtMessage = findViewById(R.id.txt)
        txt_new_number = findViewById(R.id.txt_new_number)
        contact_view = findViewById(R.id.contact_view)
        caller_name = findViewById(R.id.caller_name)
        caller_number = findViewById(R.id.caller_number)
        iv_user_icon = findViewById(R.id.iv_user_icon)
        iv_header_user_icon = findViewById(R.id.iv_header_user_icon)
        iv_call_details = findViewById(R.id.iv_call_details)
        icon_lay = findViewById(R.id.icon_lay)
        tvname = findViewById(R.id.tvName)
        reloadCredits = findViewById(R.id.get_credit)
        convList = ArrayList<Conversation>()
        chat_history = findViewById(R.id.chatlist)
        chat_history!!.setHasFixedSize(true)
        UserInterface.changeStatusBar(this, R.color.white_black)
        val layoutManager = LinearLayoutManager(this)
        chat_history!!.layoutManager = layoutManager
        fromUser = PesaApplication.getUserPhNo().replace("+", "")
        mChatAdapter =
            ChatListAdapter(
                this@NewMessageActivity,
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
        tvname!!.text = getString(R.string.new_message)
        icon_lay!!.visibility = View.GONE
        chat_history!!.visibility = View.GONE
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.white, null)
        }*/
        txt_new_number!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i2: Int, i3: Int) {}

            override fun afterTextChanged(editable: Editable) {
                val callee =
                    txt_new_number!!.text.toString().trim().replace("-", "").replace("(", "")
                        .replace(")", "")
                        .replace(" ", "")
                onSearchNumber(callee)
            }
        })
        ic_back!!.setOnClickListener { onBackPressed() }

        contact_view!!.setOnClickListener {
            txt_new_number!!.setText(caller_number!!.text.toString())
            txt_new_number!!.setSelection(txt_new_number!!.getText()!!.length)
        }
        contact_btn!!.setOnClickListener {
            if (checkPermissionForContact())
                requestPermissionForContact()
            else
                loadContacts()
        }
        btnSend!!.setOnClickListener { sendMessage() }

        iv_call_details!!.setOnClickListener {
            val startIntent = Intent(this, ContactDetailsActivity::class.java)
            startIntent.putExtra(Constants.CONTACT_NUMBER, toUser)
            startActivityForResult(startIntent, 101010)
        }

        reloadCredits!!.setOnClickListener {
            finish()
        }

        if (intent.getStringExtra("toUserNumber") != null) {
            val callerNumber = intent.getStringExtra("toUserNumber")
            txt_new_number!!.setText(callerNumber)
            txt_new_number!!.setSelection(txt_new_number!!.text!!.length)
        }
        if (Constants.getActivatedNumber()!!.isEmpty())
            contact_btn!!.visibility = View.GONE

        /* Constants.setUserDetailsListener(userDetailsListener)
         userDetailsListener.onUpdateUI()*/
    }

    private val chatHistorySelectListener = object : ChatHistorySelectListener {
        override fun onSelected(conversation: Conversation, pos: Int) {

        }
    }

    private fun firstMsgSent(conversationIds: String, toUserNumber: String) {
        try {
            if (conversationId != null)
                return
            conversationId = conversationIds
            toUser = toUserNumber.replace("+", "")
            UserInterface.retrieveContactPhotoWithName(
                applicationContext!!,
                toUser!!,
                iv_header_user_icon!!,
                tvname!!
            )
            icon_lay!!.visibility = View.VISIBLE
            chat_history!!.visibility = View.VISIBLE
            toUserView.visibility = View.GONE
            exampleLabel.visibility = View.GONE
            contact_view!!.visibility = View.GONE
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient()
            onResume()
            val layoutManager = LinearLayoutManager(this)
            chat_history!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val itemno = layoutManager.findFirstVisibleItemPosition()
                    if (itemno == 0) onLoadOldChats()
                }
            })
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


    private fun random(): String {
        return UUID.randomUUID().toString()
    }

    private fun sendMessage() {
        try {
            if (Constants.getActivatedNumber()!!.isEmpty()) {
                return
            }
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.hideSoftInputFromWindow(etxtMessage!!.windowToken, 0)
            if (etxtMessage!!.length() == 0)
                return
            var toUser = txt_new_number!!.text.toString().replace("+", "")
            if (UserInterface.getAllowedMsg(toUser!!, this@NewMessageActivity)!!) {
                val uid = Constants.getDefaultNumber()!!
                var newConversationId =
                    if (toUser!! > uid) toUser!! + uid else uid + toUser
                val s = etxtMessage!!.text.toString()
                val chatId = random()
                val c = Conversation(
                    s,
                    "pending",
                    false,
                    false,
                    Date(),
                    chatId,
                    newConversationId,
                    Constants.getDefaultNumber()!!,
                    toUser, false, false
                )
                convList!!.add(c)
                mChatAdapter!!.notifyDataSetChanged()
                if (conversationId == null) {
                    icon_lay!!.visibility = View.VISIBLE
                    chat_history!!.visibility = View.VISIBLE
                    toUserView.visibility = View.GONE
                    exampleLabel.visibility = View.GONE
                    contact_view!!.visibility = View.GONE
                }
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
                    po.put("to", toUser)
                    // po.put("to", "none")
                    po.put("screen_to", toUser)
                    po.put("is_screening", false)
                    po.put("conversation_id", newConversationId)
                    po.put("message", enCryptedMsg)
                    po.put("is_encrypted", isEncrypted)
                    po.put("is_delivery", false)
                    po.put("is_read", false)
                    po.put("chat_id", chatId)
                    po.put("status", "pending")
                    po.put("direction", "outbound")
                    po.put("delete_from_" + Constants.getDefaultNumber(), false)
                    po.put("delete_for_everyone", false)
                    po.saveEventually { e ->
                        if (applicationContext != null) {
                            if (e == null) {
                                isMessageHistoryLoad = true
                                if (conversationId == null)
                                    checkFirstMsgStatus(c.chat_id)
                            } else
                                c.status = "failed"
                            mChatAdapter!!.notifyDataSetChanged()
                        }
                    }
                    firstMsgSent(newConversationId, toUser)
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


    private fun checkFirstMsgStatus(chatId: String) {
        try {
            runOnUiThread {
                Handler().postDelayed({
                    if (applicationContext != null) {
                        val parseQuery = ParseQuery.getQuery<ParseObject>("messages")
                        parseQuery.whereEqualTo("chat_id", chatId)
                            .findInBackground { li, e ->
                                if (applicationContext != null && e == null)
                                    if (li != null) {
                                        if (li.size > 0) {
                                            val po = li!![0]
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
                                                po.getBoolean("delete_from_" + deleteNumber),
                                                po.getBoolean("delete_for_everyone")
                                            )
                                            val updatedPos = mChatAdapter!!.updateStatus(c)
                                            mChatAdapter!!.notifyItemChanged(updatedPos)
                                        }
                                    }
                            }
                    }
                }, 2000)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun loadContacts() {
        intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, 7)
    }

    override fun onRequestPermissionsResult(RC: Int, per: Array<String>, PResult: IntArray) {

        when (RC) {

            CONTACT_PERMISSION_REQUEST_CODE ->

                if (PResult.isNotEmpty() && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    loadContacts()

                } else {

                    Toast.makeText(
                        this,
                        "Permission Canceled, Now your application cannot access CONTACTS.",
                        Toast.LENGTH_LONG
                    ).show()

                }
        }
    }

    private fun onSearchNumber(searchNumber: String) {
        try {
            if (searchNumber.length > 2) {
                val myList: List<ContactDetails> = Constants.getAllContact()!!
                val searchedDatas: List<ContactDetails> = myList
                    .stream()
                    .filter { p -> p.number.contains(searchNumber) }
                    .collect(Collectors.toList())
                if (searchedDatas.isNotEmpty()) {
                    contact_view!!.visibility = View.VISIBLE
                    UserInterface.retrieveContactPhoto(
                        applicationContext!!,
                        searchedDatas[0].number,
                        iv_user_icon!!,
                        caller_name!!
                    )
                    caller_number!!.text = searchedDatas[0].number
                } else
                    contact_view!!.visibility = View.GONE
            } else
                contact_view!!.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
            contact_view!!.visibility = View.GONE
        }
    }

    private fun checkPermissionForContact(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (perm in contactPermissions) {
                if (ActivityCompat.checkSelfPermission(
                        this,
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
            this,
            contactPermissions,
            CONTACT_PERMISSION_REQUEST_CODE
        )
    }

    public override fun onActivityResult(RequestCode: Int, ResultCode: Int, ResultIntent: Intent?) {

        super.onActivityResult(RequestCode, ResultCode, ResultIntent)

        when (RequestCode) {
            101010 -> {
                if (ResultCode == RESULT_OK)
                    finish()
            }
            7 -> if (ResultCode == Activity.RESULT_OK) {
                txt_new_number!!.setText("")
                val uri: Uri?
                val cursor1: Cursor?
                val cursor2: Cursor?
                val TempNameHolder: String
                var TempNumberHolder: String
                val TempContactID: String
                var IDresult = ""
                val IDresultHolder: Int

                uri = ResultIntent!!.data

                cursor1 = contentResolver.query(uri!!, null, null, null, null)

                if (cursor1!!.moveToFirst()) {

                    TempNameHolder =
                        cursor1!!.getString(cursor1!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    TempContactID =
                        cursor1!!.getString(cursor1!!.getColumnIndex(ContactsContract.Contacts._ID))

                    IDresult =
                        cursor1!!.getString(cursor1!!.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                    IDresultHolder = Integer.valueOf(IDresult)

                    if (IDresultHolder == 1) {

                        cursor2 = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID,
                            null,
                            null
                        )
                        var allDatas: Array<String> = arrayOf()
                        var multipleContacts: HashMap<String, String> = hashMapOf()
                        while (cursor2!!.moveToNext()) {

                            TempNumberHolder =
                                cursor2!!.getString(cursor2!!.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            multipleContacts.put(TempNumberHolder, TempNameHolder)

                        }
                        multipleContacts.forEach { (key, value) -> allDatas += (key) }
                        if (allDatas.size == 1) {
                            txt_new_number!!.setText(allDatas[0].toString())
                            txt_new_number!!.setSelection(txt_new_number!!.text.length)
                        } else {
                            onMultiContactNumber(TempNameHolder, allDatas)
                        }
                    }

                }
            }
        }
    }

    private fun onMultiContactNumber(name: String, arrayAdapter: Array<String>) {
        try {
            // setup the alert builder
            val builder = AlertDialog.Builder(this@NewMessageActivity)
            builder.setTitle(name)
            // create and show the alert dialog
            builder.setItems(arrayAdapter) { dialog, which ->
                // Get the dialog selected item
                val selected = arrayAdapter[which]
                txt_new_number!!.setText(selected)
                txt_new_number!!.setSelection(txt_new_number!!.text.length)
                dialog.dismiss()

            }


            builder.setNegativeButton("cancel") { dialogInterface, which ->
                // d.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun msgSeenCallBack() {
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

        val list = java.util.ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)
        list.add(query3)

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
                    po.getBoolean("delete_from_" + deleteNumber),
                    po.getBoolean("delete_for_everyone")
                )
                Log.d("chatList", "receive" + 1)
                if (c.isDeleted!!) run { runOnUiThread { mChatAdapter?.updateStatus(c) } }
                else if (c.deletedForEveryOne!! || c.from == Constants.getDefaultNumber()) {
                    val updatedPos = mChatAdapter!!.updateStatus(c)
                    Log.d("chatList", "receive status1 " + updatedPos)
                    runOnUiThread {
                        if (updatedPos == -1) {
                            Handler().postDelayed({
                                if (applicationContext != null) {
                                    Log.d("chatList", "receive" + 2)
                                    val updatedPos = mChatAdapter!!.updateStatus(c)
                                    Log.d("chatList", "receive status2 " + updatedPos)
                                    mChatAdapter!!.notifyItemChanged(updatedPos)
                                }
                            }, 3000)
                        } else
                            mChatAdapter!!.notifyItemChanged(updatedPos)
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

        val query3 = ParseQuery.getQuery<ParseObject>("messages")
        query3.whereContainedIn("from", Constants.getAllNumbers())
        query3.whereEqualTo("to", "none")
        query3.whereEqualTo("screen_to", toUser)

        val list = java.util.ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)
        list.add(query3)

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
                    po.getBoolean("delete_from_" + deleteNumber),
                    po.getBoolean("delete_for_everyone")
                )
                lastMsgDate = po.createdAt
                if (po.getString("to") == Constants.getDefaultNumber()) {
                    if (!containsChatId(c.chat_id)) {
                        convList!!.add(c)
                        po.put("is_read", true)
                        po.put("status", "success")
                        po.saveEventually()
                        // RecyclerView updates need to be run on the UI thread
                        runOnUiThread {
                            mChatAdapter!!.notifyDataSetChanged()
                            chat_history!!.smoothScrollToPosition(mChatAdapter!!.getItemCount() - 1)
                        }
                    }
                }
                isMessageHistoryLoad = true
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

        val list = java.util.ArrayList<ParseQuery<ParseObject>>()
        list.add(query1)
        list.add(query2)
        list.add(query3)

        val parseQuery = ParseQuery.or(list)
        for (number in Constants.getAllNumbers()!!) {
            parseQuery.whereNotEqualTo("delete_from_" + number, true)
        }
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
                                    po.getBoolean("delete_from_" + deleteNumber),
                                    po.getBoolean("delete_for_everyone")
                                )
                                oldMessages.add(c)
                                if (po.getString("to") == Constants.getDefaultNumber())
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

        val list = java.util.ArrayList<ParseQuery<ParseObject>>()
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
                        if (lastMsgDate == null) {
                            if (mChatAdapter != null) {
                                mChatAdapter!!.removeAllItem()
                                //convList = ArrayList<Conversation>()
                            }
                        }
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
                                po.getBoolean("delete_from_" + deleteNumber),
                                po.getBoolean("delete_for_everyone")
                            )
                            if (!containsChatId(c.chat_id)) {
                                convList!!.add(c)
                            }
                            if (po.getString("to") == Constants.getDefaultNumber())
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
                        chat_history!!.scrollToPosition(mChatAdapter!!.getItemCount() - 1)
                        Log.d("chatList", "size--> " + li!!.size)
                    }
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            PesaApplication.currentActivityContext = this@NewMessageActivity
            Constants.CHAT_TO_NUMBER = ""
            if (conversationId != null) {
                if (!receiveCallBack!!) {
                    receivedCallBack()
                    msgSeenCallBack()
                    receiveCallBack = true
                }
                onLoadPreviousChats()
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        if (isMessageHistoryLoad) {
            isMessageHistoryLoad = false
            UserInterface.onRefreshMessageHistory()
        }
        receiveCallBack = false
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
        receiveCallBack = false
        if (conversationId != null) {
            Constants.CHAT_TO_NUMBER = ""
            stopReceivedCallBack()
            //stopMsgSeenCallBack();
        }
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

}