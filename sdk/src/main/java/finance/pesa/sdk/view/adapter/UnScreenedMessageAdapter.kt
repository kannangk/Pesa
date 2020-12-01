package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.Conversation
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface

class UnScreenedMessageAdapter(
    context: Context,
    messages: ArrayList<Conversation>
    ) : RecyclerView.Adapter<UnScreenedMessageAdapter.MViewHolder>() {
    private var context: Context? = null
    private var messages: ArrayList<Conversation> = messages


    init {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.screen_left, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val msg = messages[position]
        UserInterface.retrieveContactPhoto(
            context!!,
            msg.from,
            vh.iv_user_icon,
            TextView(context)
        )
        vh.sentTime.text = UserInterface.getChatTime(msg.date)
        vh.message.text = msg.msg

    }

    fun addlisttop(list: List<Conversation>) {
        for (i in list.indices) {
            messages.add(i, list[i])
            notifyItemInserted(i)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val message: TextView = view.findViewById(R.id.message)
        val sentTime: TextView = view.findViewById(R.id.iv_time)
        val iv_user_icon: ImageView = view.findViewById(R.id.iv_user_icon)


    }
}