package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.ScreenHistorySelectListener
import finance.pesa.sdk.utils.UserInterface

class ScreeningSMSAdapter(
    context: Context,
    callList: ArrayList<finance.pesa.sdk.Model.ScreenSMS>,
    selectListener: ScreenHistorySelectListener
) : RecyclerView.Adapter<ScreeningSMSAdapter.MViewHolder>() {
    var mSelectListener: ScreenHistorySelectListener = selectListener
    var context: Context? = null
    var callList: ArrayList<finance.pesa.sdk.Model.ScreenSMS>


    init {
        this.callList = callList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.un_screen_msg, parent, false)
        return MViewHolder(view)
    }

    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val callDetails = callList[position]
        // vh.caller_name.text = "Unknown"
        vh.caller_time.text = UserInterface.getTime(callDetails.createdDate)
        if (Constants.getAllNumbers()!!.contains(callDetails.from)) {
            UserInterface.retrieveContactPhotoWithName(
                context!!,
                callDetails.to,
                vh.iv_user_icon,
                vh.caller_name
            )
        } else {
            UserInterface.retrieveContactPhotoWithName(
                context!!,
                callDetails.from,
                vh.iv_user_icon,
                vh.caller_name
            )
        }

        vh.caller_message.text = callDetails.message
        try {
            if (callDetails.isEncrypted)
                vh.caller_message.text = UserInterface.cyberDecrypt(callDetails.message, context!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        vh.accept_lay.setOnClickListener { mSelectListener.onAccept(callDetails, position) }
        vh.decline_lay.setOnClickListener { mSelectListener.onDecline(callDetails, position) }
        vh.itemView.setOnClickListener { mSelectListener.onSelectedMessage(callDetails) }

    }


    fun removeItem(pos: Int) {
        callList.removeAt(pos)
        notifyItemRemoved(pos)
        notifyItemRangeChanged(pos, callList.size)
        if (callList.isEmpty()) {
            mSelectListener.onRemovedAll()
        }
    }

    override fun getItemCount(): Int {
        return callList.size
    }

    fun addNew(data: finance.pesa.sdk.Model.ScreenSMS) {
        callList.add(data)
        notifyDataSetChanged()
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val caller_name: TextView = view.findViewById(R.id.caller_name)
        val caller_message: TextView = view.findViewById(R.id.caller_message)
        val caller_time: TextView = view.findViewById(R.id.caller_time)
        val iv_user_icon: ImageView = view.findViewById(R.id.iv_user_icon)
        val accept_lay: LinearLayout = view.findViewById(R.id.accept_lay)
        val decline_lay: LinearLayout = view.findViewById(R.id.decline_lay)


    }
}