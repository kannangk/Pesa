package finance.pesa.sdk.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.chauthai.swipereveallayout.SwipeListener
import com.chauthai.swipereveallayout.SwipeRevealLayout
import com.chauthai.swipereveallayout.ViewBinderHelper
import finance.pesa.sdk.Model.ConversationData
import finance.pesa.sdk.Model.MessageHistorySelectListener
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface


class MessageHistoryAdapter(
    context: Context,
    callList: ArrayList<finance.pesa.sdk.Model.ConversationData>,
    selectListener: finance.pesa.sdk.Model.MessageHistorySelectListener
) : RecyclerView.Adapter<MessageHistoryAdapter.MViewHolder>() {
    private val viewBinderHelper = ViewBinderHelper()
    var mSelectListener: finance.pesa.sdk.Model.MessageHistorySelectListener = selectListener
    var context: Context? = null
    var callList: ArrayList<finance.pesa.sdk.Model.ConversationData>


    init {
        this.callList = callList
        this.context = context
        viewBinderHelper.setOpenOnlyOne(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_history_row, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val callDetails = callList[position]
        viewBinderHelper.bind(vh.swipeRevealLayout, callDetails.conversationId)
        // vh.caller_name.text = "Unknown"
        vh.count.visibility = View.INVISIBLE
        vh.caller_time.text = UserInterface.getTime(callDetails.createdDate)
        if (Constants.getAllNumbers()!!.contains(callDetails.from)) {
            UserInterface.retrieveContactPhotoWithName(
                context!!,
                callDetails.to,
                vh.iv_user_icon,
                vh.caller_name
            )
            vh.caller_message.setTextColor(context!!.resources.getColor(R.color.light_gray_white, null))
        } else {
            UserInterface.retrieveContactPhotoWithName(
                context!!,
                callDetails.from,
                vh.iv_user_icon,
                vh.caller_name
            )
            if (callDetails.isRead)
                vh.caller_message.setTextColor(context!!.resources.getColor(R.color.light_gray_white, null))
            else {
                vh.caller_message.setTextColor(
                    context!!.resources.getColor(
                        R.color.app_green,
                        null
                    )
                )
                if (callDetails.unReadCount != 0) {
                    vh.count.visibility = View.VISIBLE
                    vh.count.text = callDetails.unReadCount.toString()
                }
            }
        }

        vh.caller_message.text = callDetails.message
        try {
            if (callDetails.isEncrypted)
                vh.caller_message.text = UserInterface.cyberDecrypt(callDetails.message, context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

       /* vh.swipeRevealLayout.setSwipeListener(object : SwipeListener {
            override fun onOpened(view: SwipeRevealLayout?, dragEdge: Int) {
            }

            override fun onTouchUp(isUp: Boolean) {
            }

            override fun onClosed(view: SwipeRevealLayout?) {
            }

            override fun onSlide(view: SwipeRevealLayout?, slideOffset: Float) {
                if (slideOffset > 0.1) {
                    vh.user_lay.background = context?.getDrawable(R.drawable.swipe_right_half)
                } else {
                    vh.user_lay.background = context?.getDrawable(R.drawable.swipe_right_half_empty)
                }
            }

        })
*/
        vh.bind(callDetails.objectId, position, context!!, mSelectListener, callDetails)
    }


    fun removeItem(pos: Int) {
        callList.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, callList.size)
    }

    override fun getItemCount(): Int {
        return callList.size
    }

    fun addNew(data: finance.pesa.sdk.Model.ConversationData) {
        callList.add(data)
        notifyDataSetChanged()
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val caller_name: TextView = view.findViewById(R.id.caller_name)
        val caller_message: TextView = view.findViewById(R.id.caller_message)
        val caller_time: TextView = view.findViewById(R.id.caller_time)
        val count: TextView = view.findViewById(R.id.count)
        val iv_user_icon: ImageView = view.findViewById(R.id.iv_user_icon)
        val user_lay: LinearLayout = view.findViewById(R.id.user_lay)
        val delete_icon: ImageView = view.findViewById(R.id.delete_icon)
        val viewForeground: FrameLayout = view.findViewById(R.id.view_foreground)
        val swipeRevealLayout: SwipeRevealLayout = view.findViewById(R.id.row_lay)

        fun bind(
            data: String,
            position: Int,
            context: Context,
            mSelectListener: finance.pesa.sdk.Model.MessageHistorySelectListener,
            callDetails: finance.pesa.sdk.Model.ConversationData
        ) {
            swipeRevealLayout.tag = this
            /*call_icon.setOnClickListener {
                swipeRevealLayout.close(true)
                user_lay.background = context?.getDrawable(R.drawable.swipe_right_half_empty)
                mSelectListener.onCall(callDetails)
            }*/

            delete_icon.setOnClickListener {
                mSelectListener.onDeleteMessage(callDetails, adapterPosition)
            }

            viewForeground.setOnClickListener {
                mSelectListener.onSelectedMessage(callDetails)
            }

        }
    }
}