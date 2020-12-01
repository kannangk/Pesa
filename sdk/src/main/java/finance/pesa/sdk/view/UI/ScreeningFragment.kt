package finance.pesa.sdk.view.UI

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.parse.ParseQuery
import finance.pesa.sdk.Api.ApiClient
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.view.adapter.ScreeningSMSAdapter
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.ScreenHistorySelectListener
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.UI.MessageHistory.Companion.messageHistory
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ScreeningFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var noList: LinearLayout? = null
    private var shimmer_view_container: ShimmerFrameLayout? = null
    private var adapter: ScreeningSMSAdapter? = null
    private var iv_back: ImageView? = null
    private var isMessageHistoryLoad = false


    companion object {
        fun newInstance(): ScreeningFragment {
            return ScreeningFragment()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.screen_msg, container, false)
        recyclerView = view.findViewById(R.id.item_recycler_view)
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container)
        noList = view.findViewById(R.id.no_data_lay)
        iv_back = view.findViewById(R.id.iv_back)
        iv_back!!.setOnClickListener { activity!!.onBackPressed() }
        chatHistoryData()
        return view
    }

    override fun onResume() {
        super.onResume()
        if (Constants.isLoadUnScreen!!) {
            isMessageHistoryLoad = true
            chatHistoryData()
        }
    }


    private fun chatHistoryData() {
        try {
            Constants.isLoadUnScreen = false
            if (Constants.getDefaultNumber()!!.isEmpty()) {
                noList?.visibility = View.VISIBLE
                recyclerView?.visibility = View.GONE
                shimmer_view_container?.visibility = View.GONE
                return
            }
            activity!!.runOnUiThread {
                noList?.visibility = View.GONE
                shimmer_view_container?.visibility = View.VISIBLE
                shimmer_view_container?.startShimmerAnimation()
                recyclerView?.visibility = View.GONE

                /* val query1 = ParseQuery<ScreenSMS>(ScreenSMS::class.java)
                 query1.whereContainedIn("screen_from", Constants.getAllNumbers())
 */
                val query2 =
                    ParseQuery<finance.pesa.sdk.Model.ScreenSMS>(finance.pesa.sdk.Model.ScreenSMS::class.java)
                query2.whereContainedIn("screen_to", Constants.getAllNumbers())

                val list = java.util.ArrayList<ParseQuery<finance.pesa.sdk.Model.ScreenSMS>>()
                //list.add(query1)
                list.add(query2)

                val parseQuery = ParseQuery.or(list)
                parseQuery.orderByAscending("createdAt")
                parseQuery!!.limit = 10000
                parseQuery.findInBackground { li, e ->
                    if (context != null)
                        if (li != null)
                            if (li!!.size > 0) {
                                val historyDatas: Map<String, List<finance.pesa.sdk.Model.ScreenSMS>> =
                                    li!!.stream().collect(
                                        Collectors.groupingBy(finance.pesa.sdk.Model.ScreenSMS::getConversationId)
                                    )
                                var allCallHistory: ArrayList<finance.pesa.sdk.Model.ScreenSMS> =
                                    ArrayList()
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

                                var allNewCallHistory: ArrayList<finance.pesa.sdk.Model.ScreenSMS> =
                                    ArrayList()
                                var flagNumbers: HashMap<String, Int>? = HashMap()
                                for (history in allCallHistory) {
                                    if (Constants.getAllNumbers()!!.contains(history.from)) {
                                        var unReadCount = 0
                                        try {
                                            unReadCount = history.unReadCount
                                            if (flagNumbers!!.containsKey(history.screenTo)) {
                                                val oldCount =
                                                    flagNumbers[history.screenTo]?.plus(unReadCount)
                                                unReadCount = oldCount!!
                                            }
                                        } catch (e: Exception) {
                                            e.printStackTrace()
                                        }
                                        if (!flagNumbers!!.containsKey(history.screenTo)) {
                                            history.setUniqueNumber(history.screenTo)
                                            allNewCallHistory.add(history)
                                        }
                                        flagNumbers!![history.screenTo] = unReadCount
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


                                noList?.visibility = View.GONE
                                shimmer_view_container?.visibility = View.GONE
                                recyclerView!!.visibility = View.VISIBLE
                                adapter = ScreeningSMSAdapter(
                                    context!!,
                                    allNewCallHistory,
                                    messageSelectListener
                                )
                                recyclerView!!.layoutManager = LinearLayoutManager(activity)
                                recyclerView!!.setHasFixedSize(true)
                                recyclerView!!.adapter = adapter
                                if (messageHistory != null) {
                                    messageHistory!!.unscreen_badge!!.visibility = View.VISIBLE
                                    messageHistory!!.unscreen_badge!!.text =
                                        allNewCallHistory.size.toString()
                                }
                            } else {
                                noList?.visibility = View.VISIBLE
                                recyclerView?.visibility = View.GONE
                                shimmer_view_container?.visibility = View.GONE
                                if (messageHistory != null)
                                    messageHistory!!.unscreen_badge!!.visibility = View.GONE
                            }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private val messageSelectListener = object : ScreenHistorySelectListener {
        override fun onRemovedAll() {
            noList?.visibility = View.VISIBLE
            recyclerView?.visibility = View.GONE
            shimmer_view_container?.visibility = View.GONE
            if (messageHistory != null)
                messageHistory!!.unscreen_badge!!.visibility = View.GONE
        }

        override fun onSelectedMessage(messageDetails: finance.pesa.sdk.Model.ScreenSMS) {
            val intent = Intent(context, UnScreenedMessageActivity::class.java)
            intent.putExtra(Constants.TO_USER_ID, messageDetails.from)
            intent.putExtra(Constants.CONVERSATION_ID, messageDetails.conversationId)
            startActivity(intent)
        }

        override fun onAccept(messageDetails: finance.pesa.sdk.Model.ScreenSMS, position: Int) {
            onLoadAccept(1, messageDetails, position)
        }

        override fun onDecline(messageDetails: finance.pesa.sdk.Model.ScreenSMS, position: Int) {
            onLoadDecline(1, messageDetails, position)
        }

    }

    private fun onLoadAccept(
        retryCount: Int,
        messageDetails: finance.pesa.sdk.Model.ScreenSMS,
        position: Int
    ) {
        UserInterface.showProgress("Loading...", context)
        val params = JSONObject()
        try {
            val jsonArray = JSONArray()
            if (Constants.getAllNumbers()!!.size > 1) {
                for (number in Constants.getAllNumbers()!!) {
                    val conversationId =
                        if (messageDetails.uniqueNumber > number) messageDetails.uniqueNumber + number else number + messageDetails.uniqueNumber
                    jsonArray.put(conversationId)
                }
            } else {
                jsonArray.put(messageDetails.conversationId)
            }
            params.put("conversation_id", jsonArray)
            params.put("from", messageDetails.uniqueNumber)
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
                    onLoadAccept(newRetry, messageDetails, position)
                } else {
                    UserInterface.hideProgress(context)
                    UserInterface.showToast(context, t.message.toString())
                }
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(context)
                    if (response.isSuccessful) {
                        try {
                            isMessageHistoryLoad = true
                            adapter!!.removeItem(position)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (!(response.message().contains("Invalid Keys") || response.code() === 400 || response.code() === 401 || response.code() === 402)) {
                            UserInterface.hideProgress(context)
                            fragmentManager!!.beginTransaction().replace(
                                R.id.shareg_main_container,
                                SplashFragment()
                            ).commitAllowingStateLoss()
                        } else {
                            if (retryCount <= 2) {
                                val newRetry = retryCount + 1
                                onLoadAccept(newRetry, messageDetails, position)
                            } else {
                                UserInterface.hideProgress(context)
                                UserInterface.showToast(context, response.message())
                            }
                        }
                    }
                }
            }
        })
    }


    private fun onLoadDecline(
        retryCount: Int,
        messageDetails: finance.pesa.sdk.Model.ScreenSMS,
        position: Int
    ) {
        UserInterface.showProgress("Loading...", context)
        val params = JSONObject()
        try {
            val jsonArray = JSONArray()
            if (Constants.getAllNumbers()!!.size > 1) {
                for (number in Constants.getAllNumbers()!!) {
                    val conversationId =
                        if (messageDetails.uniqueNumber > number) messageDetails.uniqueNumber + number else number + messageDetails.uniqueNumber
                    jsonArray.put(conversationId)
                }
            } else {
                jsonArray.put(messageDetails.conversationId)
            }
            params.put("conversation_id", jsonArray)
            params.put("from", messageDetails.uniqueNumber)
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
                    onLoadDecline(newRetry, messageDetails, position)
                } else {
                    UserInterface.hideProgress(context)
                    UserInterface.showToast(context, t.message.toString())
                }
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                response?.body()?.let {
                    UserInterface.hideProgress(context)
                    if (response.isSuccessful) {
                        try {
                            adapter!!.removeItem(position)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                } ?: run {
                    if (response.body() == null) {
                        if (!(response.message().contains("Invalid Keys") || response.code() === 400 || response.code() === 401 || response.code() === 402)) {
                            UserInterface.hideProgress(context)
                            fragmentManager!!.beginTransaction().replace(
                                R.id.shareg_main_container,
                                SplashFragment()
                            ).commitAllowingStateLoss()
                        } else {
                            if (retryCount <= 2) {
                                val newRetry = retryCount + 1
                                onLoadDecline(newRetry, messageDetails, position)
                            } else {
                                UserInterface.hideProgress(context)
                                UserInterface.showToast(context, response.message())
                            }
                        }
                    }
                }
            }
        })
    }

    override fun onDestroy() {
        if (isMessageHistoryLoad) {
            isMessageHistoryLoad = false
            UserInterface.onRefreshMessageHistory()
        }
        super.onDestroy()
    }

}