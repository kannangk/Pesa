package finance.pesa.sdk.view.UI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.parse.ParseQuery
import finance.pesa.sdk.Model.ContactUpdatedListener
import finance.pesa.sdk.Model.ConversationData
import finance.pesa.sdk.Model.MessageHistorySelectListener
import finance.pesa.sdk.Model.ScreenSMS
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.adapter.MessageHistoryAdapter
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MessageHistory : Fragment() {
    // var viewModel: MessageHistoryViewModel? = null
    internal var adapter: MessageHistoryAdapter? = null
    var isViewGenerated: Boolean = false
    var bannerLayout: RelativeLayout? = null
    var bannermessage: TextView? = null
    var bannereActionText: TextView? = null
    var unscreen_badge: TextView? = null
    var bannerAction: String? = null
    var recyclerView: RecyclerView? = null
    var newMessage: FloatingActionButton? = null
    var history_view: LinearLayout? = null
    var mainView: RelativeLayout? = null
    var noList: RelativeLayout? = null
    var shimmer_view_container: ShimmerFrameLayout? = null
    var screeningSMS: ImageView? = null

    companion object {
        var messageHistory: MessageHistory? = null
        fun newInstance(): MessageHistory {
            return MessageHistory()
        }
    }

    fun onMessageHistoryTabSelected() {
        if (!isViewGenerated!!) {
            isViewGenerated = true
            UserInterface.changeStatusBar(activity!!, R.color.white_black)
            chatHistoryData()
            screeningHistoryData()
            Constants.setContactUpdatedListener(contactUpdatedListener)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            if (DashboardFragment.dashboardFragment!!.viewPager!!.currentItem == 3) {
                onMessageHistoryTabSelected()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.message_history, container, false)
        messageHistory = this
        unscreen_badge = view.findViewById(R.id.unscreen_badge)
        recyclerView = view.findViewById(R.id.recycler_view)
        newMessage = view.findViewById(R.id.fab_new_message)
        bannerLayout = view.findViewById(R.id.bannerLay)
        bannermessage = view.findViewById(R.id.banner_msg)
        bannereActionText = view.findViewById(R.id.banner_action)
        history_view = view.findViewById(R.id.history_view)
        noList = view.findViewById(R.id.noList)
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container)
        mainView = view.findViewById(R.id.mainView)
        screeningSMS = view.findViewById(R.id.unscreen_notification)
        //setupViewModel()
        // viewModel!!.onMessageHistory(Constants.getDefaultNumber())
        newMessage!!.setOnClickListener {
            val startIntent = Intent(context, NewMessageActivity::class.java)
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(startIntent)
        }

        screeningSMS!!.setOnClickListener {
            PesaApplication.getChildFragmentManager()!!.beginTransaction().add(
                R.id.message_frame,
                ScreeningFragment.newInstance()
            ).addToBackStack("Messages").commitAllowingStateLoss()
        }

        return view
    }

    private fun screeningHistoryData() {
        try {
            activity!!.runOnUiThread {
                val query = ParseQuery<finance.pesa.sdk.Model.ScreenSMS>(finance.pesa.sdk.Model.ScreenSMS::class.java)
                query.whereContainedIn("screen_to", Constants.getAllNumbers())

                val list = java.util.ArrayList<ParseQuery<finance.pesa.sdk.Model.ScreenSMS>>()
                list.add(query)

                val parseQuery = ParseQuery.or(list)
                parseQuery!!.limit = 10000
                parseQuery.findInBackground { li, e ->
                    if (context != null)
                        if (li != null)
                            if (li!!.size > 0) {
                                val historyDatas: Map<String, List<finance.pesa.sdk.Model.ScreenSMS>> =
                                    li!!.stream().collect(
                                        Collectors.groupingBy(finance.pesa.sdk.Model.ScreenSMS::getConversationId)
                                    )
                                var allCallHistory: ArrayList<finance.pesa.sdk.Model.ScreenSMS> = ArrayList()
                                for ((key, value) in historyDatas) {
                                    if ((Constants.getAllNumbers()!!.contains(value[value.size - 1].screenTo))) {
                                        var unReadCount = 0
                                        for (data in value) {
                                            if (Constants.getAllNumbers()!!.contains(data.screenTo) && !data.isRead) {
                                                unReadCount++
                                            }
                                        }
                                        value[value.size - 1].setUnReadCount(unReadCount)
                                        allCallHistory!!.add(value[value.size - 1])
                                    } else {
                                        allCallHistory!!.add(value[value.size - 1])
                                    }
                                }
                                Collections.sort(allCallHistory, Collections.reverseOrder())

                                var allNewCallHistory: ArrayList<finance.pesa.sdk.Model.ScreenSMS> = ArrayList()
                                var flagNumbers: HashMap<String, Int>? = HashMap()
                                for (history in allCallHistory) {
                                    if (Constants.getAllNumbers()!!.contains(history.from)) {
                                        if (!flagNumbers!!.containsKey(history.screenTo)) {
                                            history.setUniqueNumber(history.screenTo)
                                            allNewCallHistory.add(history)
                                        }
                                    } else {
                                        if (!flagNumbers!!.containsKey(history.from)) {
                                            history.setUniqueNumber(history.from)
                                            allNewCallHistory.add(history)
                                        }
                                    }
                                }
                                if (messageHistory != null) {
                                    messageHistory!!.unscreen_badge!!.visibility = View.VISIBLE
                                    messageHistory!!.unscreen_badge!!.text =
                                        allNewCallHistory.size.toString()
                                }
                            } else {
                                if (messageHistory != null)
                                    messageHistory!!.unscreen_badge!!.visibility = View.GONE
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val contactUpdatedListener: finance.pesa.sdk.Model.ContactUpdatedListener = object :
        finance.pesa.sdk.Model.ContactUpdatedListener {
        override fun onContactUpdated() {
            if (adapter != null)
                adapter!!.notifyDataSetChanged()
        }

    }

    private val messageSelectListener: finance.pesa.sdk.Model.MessageHistorySelectListener =
        object : finance.pesa.sdk.Model.MessageHistorySelectListener {
            override fun onCall(messageDetails: finance.pesa.sdk.Model.ConversationData) {
            }

            override fun onDeleteMessage(messageDetails: finance.pesa.sdk.Model.ConversationData, position: Int) {
                try {
                    onDeletedConversation(messageDetails, position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onSelectedMessage(messageDetails: finance.pesa.sdk.Model.ConversationData) {
                try {
                    val intent = Intent(context, ChatActivity::class.java)
                    if (Constants.getAllNumbers()!!.contains(messageDetails.from)) {
                        intent.putExtra(Constants.TO_USER_ID, messageDetails.to)
                        intent.putExtra(Constants.FROM_USER_ID, messageDetails.from)
                    } else {
                        intent.putExtra(Constants.TO_USER_ID, messageDetails.from)
                        intent.putExtra(Constants.FROM_USER_ID, messageDetails.to)
                    }
                    intent.putExtra(Constants.CONVERSATION_ID, messageDetails.conversationId)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        }


    private fun onDeletedConversation(messageDetails: finance.pesa.sdk.Model.ConversationData, position: Int) {
        try {
            if (context == null)
                return
            if (adapter?.callList?.size == 0) {
                mainView?.background =
                    context!!.getDrawable(R.drawable.ic_background_history)
                noList?.visibility = View.VISIBLE
                history_view?.visibility = View.GONE
            }
            activity!!.runOnUiThread {

                val query1 = ParseQuery<finance.pesa.sdk.Model.ConversationData>(finance.pesa.sdk.Model.ConversationData::class.java)
                query1.whereContainedIn("from", Constants.getAllNumbers())
                query1.whereEqualTo("to", messageDetails.uniqueNumber)

                val query2 = ParseQuery<finance.pesa.sdk.Model.ConversationData>(finance.pesa.sdk.Model.ConversationData::class.java)
                query2.whereContainedIn("to", Constants.getAllNumbers())
                query2.whereEqualTo("from", messageDetails.uniqueNumber)

                val list = java.util.ArrayList<ParseQuery<finance.pesa.sdk.Model.ConversationData>>()
                list.add(query1)
                list.add(query2)

                val parseQuery = ParseQuery.or(list)
                for (number in Constants.getAllNumbers()!!) {
                    parseQuery.whereNotEqualTo("delete_from_" + number, true)
                }
                parseQuery!!.limit = 10000
                parseQuery.findInBackground { li, e ->
                    if (context != null)
                        if (li != null)
                            if (li!!.size > 0) {
                                for (messageDetail in li) {
                                    var deleteNumber = messageDetail.from
                                    if (!Constants.getAllNumbers()!!.contains(deleteNumber))
                                        deleteNumber = messageDetail.to
                                    messageDetail.put(
                                        "delete_from_" + deleteNumber.replace(
                                            "+",
                                            ""
                                        ),
                                        true
                                    )
                                    messageDetail.saveInBackground()
                                }
                                try {
                                    adapter!!.removeItem(position)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } else {

                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //observers Response
    /* private val renderDataObserver = Observer<List<MessageDetails>> {
         try {
             if (it.isNotEmpty()) {
                 mainView.setBackgroundColor(context!!.getColor(R.color.tab_bg))
                 noList.visibility = View.GONE
                 history_view.visibility = View.VISIBLE
                 recyclerView!!.visibility = View.VISIBLE
                 adapter = MessageHistoryAdapter(
                     context!!,
                     it ?: emptyList(),
                     messageSelectListener
                 )
                 recyclerView!!.layoutManager = LinearLayoutManager(activity)
                 recyclerView!!.adapter = adapter
             } else {
                 if (!isFirstLoad) {
                     mainView.setBackgroundDrawable(context!!.getDrawable(R.drawable.ic_background_history))
                     noList.visibility = View.VISIBLE
                     history_view.visibility = View.GONE
                 }
                 isFirstLoad = false
             }
         } catch (e: Exception) {
             e.printStackTrace()
         }
     }

     //observers Loading
     private val isViewLoadingObserver = Observer<Boolean> {
         if (it) {
             mainView.setBackgroundColor(context!!.getColor(R.color.tab_bg))
             noList.visibility = View.GONE
             history_view.visibility = View.VISIBLE
             shimmer_view_container.visibility = View.VISIBLE
             shimmer_view_container.startShimmerAnimation()
             recyclerView!!.visibility = View.GONE
         } else {
             // mainView.setBackgroundColor(context!!.getColor(R.color.tab_bg))
             shimmer_view_container.stopShimmerAnimation()
             shimmer_view_container.visibility = View.GONE
             recyclerView!!.visibility = View.VISIBLE
         }
     }

     //observers ErrorMessage
     private val onMessageErrorObserver = Observer<HoomError> {
         if (it.errorCode == 401 || it.errorCode == 402) {
             fragmentManager!!.beginTransaction().replace(
                 R.id.shareg_main_container,
                 SplashFragment()
             ).commitAllowingStateLoss()
         }
     }*/

    fun chatHistoryData() {
        try {
            if (Constants.getDefaultNumber()!!.isEmpty()) {
                mainView?.background =
                    context!!.getDrawable(R.drawable.ic_background_history)
                noList?.visibility = View.VISIBLE
                history_view?.visibility = View.GONE
                return
            }
            activity!!.runOnUiThread {
                mainView?.setBackgroundColor(context!!.getColor(R.color.light_dark))
                noList?.visibility = View.GONE
                history_view?.visibility = View.VISIBLE
                shimmer_view_container?.visibility = View.VISIBLE
                shimmer_view_container?.startShimmerAnimation()
                recyclerView?.visibility = View.GONE

                val query1 = ParseQuery<finance.pesa.sdk.Model.ConversationData>(finance.pesa.sdk.Model.ConversationData::class.java)
                query1.whereContainedIn("from", Constants.getAllNumbers())

                val query2 = ParseQuery<finance.pesa.sdk.Model.ConversationData>(finance.pesa.sdk.Model.ConversationData::class.java)
                query2.whereContainedIn("to", Constants.getAllNumbers())

                val list = java.util.ArrayList<ParseQuery<finance.pesa.sdk.Model.ConversationData>>()
                list.add(query1)
                list.add(query2)

                val parseQuery = ParseQuery.or(list)
                for (number in Constants.getAllNumbers()!!) {
                    parseQuery.whereNotEqualTo("delete_from_" + number, true)
                }
                parseQuery.orderByAscending("createdAt")
                parseQuery!!.limit = 10000
                parseQuery.findInBackground { li, e ->
                    if (context != null)
                        if (li != null)
                            if (li!!.size > 0) {
                                val historyDatas: Map<String, List<finance.pesa.sdk.Model.ConversationData>> =
                                    li!!.stream().collect(
                                        Collectors.groupingBy(finance.pesa.sdk.Model.ConversationData::getConversationId)
                                    )
                                var allCallHistory: ArrayList<finance.pesa.sdk.Model.ConversationData> = ArrayList()
                                for ((key, value) in historyDatas) {
                                    if (value[value.size - 1].to == "none")
                                        value[value.size - 1].to = value[value.size - 1].screenTo
                                    if ((Constants.getAllNumbers()!!.contains(value[value.size - 1].to))) {
                                        var unReadCount = 0
                                        for (data in value) {
                                            if (Constants.getAllNumbers()!!.contains(data.to) && !data.isRead) {
                                                unReadCount++
                                            }
                                        }
                                        value[value.size - 1].setUnReadCount(unReadCount)
                                        allCallHistory!!.add(value[value.size - 1])
                                    } else {
                                        allCallHistory!!.add(value[value.size - 1])
                                    }
                                }
                                Collections.sort(allCallHistory, Collections.reverseOrder())

                                var allNewCallHistory: ArrayList<finance.pesa.sdk.Model.ConversationData> = ArrayList()
                                var flagNumbers: HashMap<String, Int>? = HashMap()
                                for (history in allCallHistory) {
                                    if (Constants.getAllNumbers()!!.contains(history.from)) {
                                        var unReadCount = 0
                                        try {
                                            unReadCount = history.unReadCount
                                            if (flagNumbers!!.containsKey(history.to)) {
                                                val oldCount =
                                                    flagNumbers[history.to]?.plus(unReadCount)
                                                unReadCount = oldCount!!
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        if (!flagNumbers!!.containsKey(history.to)) {
                                            history.setUniqueNumber(history.to)
                                            allNewCallHistory.add(history)
                                        }
                                        flagNumbers!![history.to] = unReadCount
                                    } else {
                                        var unReadCount = 0
                                        try {
                                            unReadCount = history.unReadCount
                                            if (flagNumbers!!.containsKey(history.from)) {
                                                val oldCount =
                                                    flagNumbers[history.from]?.plus(unReadCount)
                                                unReadCount = oldCount!!
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        if (!flagNumbers!!.containsKey(history.from)) {
                                            history.setUniqueNumber(history.from)
                                            allNewCallHistory.add(history)
                                        }
                                        flagNumbers!![history.from] = unReadCount
                                    }
                                }

                                for (data in allNewCallHistory) {
                                    if (flagNumbers!!.containsKey(data.uniqueNumber))
                                        data.setUnReadCount(flagNumbers[data.uniqueNumber]!!)
                                }


                                mainView?.setBackgroundColor(context!!.getColor(R.color.light_dark))
                                noList?.visibility = View.GONE
                                history_view?.visibility = View.VISIBLE
                                recyclerView!!.visibility = View.VISIBLE
                                adapter = MessageHistoryAdapter(
                                    context!!,
                                    allNewCallHistory,
                                    messageSelectListener
                                )
                                recyclerView!!.layoutManager = LinearLayoutManager(activity)
                                recyclerView!!.setHasFixedSize(true)
                                recyclerView!!.adapter = adapter
                            } else {
                                mainView?.background =
                                    context!!.getDrawable(R.drawable.ic_background_history)
                                noList?.visibility = View.VISIBLE
                                history_view?.visibility = View.GONE
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}