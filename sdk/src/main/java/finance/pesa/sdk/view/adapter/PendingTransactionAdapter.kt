package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.PendingTransaction
import finance.pesa.sdk.R

class PendingTransactionAdapter(context: Context, transactionData: ArrayList<PendingTransaction>) :
    RecyclerView.Adapter<PendingTransactionAdapter.MViewHolder>() {
    var transactionData: ArrayList<PendingTransaction>
    var context: Context? = null

    init {
        this.context = context
        this.transactionData = transactionData
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pending_transaction, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val transaction = transactionData[position]
        vh.message?.text = transaction.message
    }

    override fun getItemCount(): Int {
        return transactionData!!.size
    }

    fun addPendingTransaction(pendingTransaction:PendingTransaction){
        transactionData.add(pendingTransaction)
        notifyDataSetChanged()
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var message: TextView? = view.findViewById(R.id.pending_transaction_msg)
    }
}