package finance.pesa.sdk.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.Conversation
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.ChatHistorySelectListener
import finance.pesa.sdk.utils.UserInterface
import java.util.*

class ChatListAdapter(
    private val context: Context,
    private val mUserIds: List<String>,
    private val mConvList: ArrayList<Conversation>,
    private val chatHistorySelectListener: ChatHistorySelectListener
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var selectedConversation = HashMap<String, Conversation>()
    private var isSelected: Boolean? = false
    init {
        selectedConversation = HashMap<String, Conversation>()
    }

    fun addlisttop(list: List<Conversation>) {
        for (i in list.indices) {
            mConvList.add(i, list[i])
            notifyItemInserted(i)
        }
    }

    fun addSelecter(conversation: Conversation) {
        selectedConversation.put(conversation.chat_id, conversation)
    }

    fun removeSelecter(conversation: Conversation) {
        selectedConversation.remove(conversation.chat_id)
    }

    fun removeAllSelecter() {
        this.selectedConversation = HashMap<String, Conversation>()
    }

    fun updateSelected(isSelect: Boolean?) {
        this.isSelected = isSelect
    }

    fun removeItem(position: Int) {
        mConvList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, mConvList.size)
    }


    override fun getItemCount(): Int {
        return mConvList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (mUserIds.contains(getItem(position).from)) 1 else 0
    }

    private fun getItem(arg0: Int): Conversation {
        return mConvList[arg0]
    }

    override fun getItemId(arg0: Int): Long {
        return arg0.toLong()
    }
    fun getSelectedConversation(): HashMap<String, Conversation> {
        return this.selectedConversation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_item_sent, parent, false)
            VHRight(v)
        } else {
            val v =
                LayoutInflater.from(parent.context).inflate(R.layout.chat_item_rcv, parent, false)
            VHLeft(v)
        }
    }


    fun updateStatus(updateConversation: Conversation): Int {
        Log.d("chatList", "updateStatus-->" +mConvList.size)
        if (mConvList.size > 0)
            for (i in mConvList.indices) {
                if (mConvList[i].chat_id == updateConversation.chat_id) {
                    if (updateConversation.isDeleted!!) {
                        removeItem(i)
                        return i
                    }
                    mConvList[i].isDelivery = (updateConversation.isDelivery)
                    mConvList[i].isRead = (updateConversation.isRead)
                    mConvList[i].status = (updateConversation.status)
                    mConvList[i].isDeleted=(updateConversation.isDeleted)?:false
                    mConvList[i].deletedForEveryOne=(updateConversation.deletedForEveryOne)?:false
                    Log.d("chatList", "updateStatus-->" +updateConversation.chat_id)
                    return i
                }
            }
        return -1
    }



    fun removeAllItem() {
        for (pos in 0 until mConvList.size) {
            mConvList.removeAt(pos)
            notifyItemRemoved(pos)
            notifyItemRangeChanged(pos, mConvList.size)
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val c = getItem(pos)
        if (holder is VHRight) {
            if (c.deletedForEveryOne!!) {
                holder.status.visibility = View.GONE
                val img = ContextCompat.getDrawable(context, R.drawable.ic_delete_idendifier)
                img!!.setBounds(0, 0, img.minimumWidth, img.minimumHeight)
                holder.message.setCompoundDrawables(img, null, null, null)
                holder.message.text = "You are deleted this message"
            } else {
                holder.message.setCompoundDrawables(null, null, null, null)
                holder.status.visibility = View.VISIBLE
                holder.message.text = mConvList[pos].msg
                if (c.status == "null") {
                    if (c.isDelivery) {
                        holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_delivery_tick))
                    } else {
                        holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_sent_tick))
                    }
                } else if (c.status == "success") {
                    when {
                        c.isRead!! -> holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_read_tick))
                        c.isDelivery -> holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_delivery_tick))
                        else -> holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_sent_tick))
                    }
                } else if (c.status == "pending") {
                    holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_sending))
                } else {
                    holder.status.setImageDrawable(context.getDrawable(R.drawable.ic_failed))
                }
            }

            holder.sentTime.text = UserInterface.getChatTime(c.date)
            if (selectedConversation.containsKey(c.chat_id))
                holder.itemView.setBackgroundColor(context.getColor(R.color.chat_select_bg))
            else
                holder.itemView.setBackgroundColor(context.getColor(R.color.transparent))

            //Single Tapup
            holder.itemView.setOnClickListener{
                if (isSelected!!) {
                    chatHistorySelectListener.onSelected(c, pos)
                }
            }

            //Long Press
            holder.itemView.setOnLongClickListener {
                chatHistorySelectListener.onSelected(c, pos)
                true
            }

        } else if (holder is VHLeft) {
            if (selectedConversation.containsKey(c.chat_id))
                holder.itemView.setBackgroundColor(context.getColor(R.color.chat_select_bg))
            else
                holder.itemView.setBackgroundColor(context.getColor(R.color.transparent))

            if (c.deletedForEveryOne!!) {
                val img = ContextCompat.getDrawable(context, R.drawable.ic_delete_idendifier)
                img!!.setBounds(0, 0, img.minimumWidth, img.minimumHeight)
                holder.message.setCompoundDrawables(img, null, null, null)
                holder.message.setText("This message was deleted")
            } else {
                holder.message.setCompoundDrawables(null, null, null, null)
                holder.message.text = mConvList[pos].msg
            }

            holder.sentTime.text = UserInterface.getChatTime(c.date)
            //Single Tapup
            holder.itemView.setOnClickListener {
                if (isSelected!!) {
                    chatHistorySelectListener.onSelected(c, pos)
                }
            }

            //Long Press
            holder.itemView.setOnLongClickListener{
                chatHistorySelectListener.onSelected(c, pos)
                true
            }
        }
    }

    internal inner class VHLeft(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentTime: TextView = itemView.findViewById(R.id.iv_time)
        var message: TextView = itemView.findViewById(R.id.message)

    }

    internal inner class VHRight(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var sentTime: TextView = itemView.findViewById(R.id.iv_time)
        var message: TextView = itemView.findViewById(R.id.message)
        var status: ImageView = itemView.findViewById(R.id.status)

    }

}


