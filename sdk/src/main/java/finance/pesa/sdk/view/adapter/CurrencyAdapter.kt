package finance.pesa.sdk.view.adapter;

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface


class CurrencyAdapter(
    context: Context,
    allData: List<InvestData>,
    lastCheckedPosition: Int
) : RecyclerView.Adapter<CurrencyAdapter.MViewHolder>() {

    var allData: List<InvestData>
    var context: Context? = null
    var lastCheckedPosition: Int? = 0

    init {
        this.context = context
        this.allData = allData
        this.lastCheckedPosition = lastCheckedPosition
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.spinner_row, parent, false)
        return MViewHolder(view)
    }

    fun getMarket(): InvestData {
        return allData[lastCheckedPosition!!]
    }

    fun getLastSelectedPos(): Int {
        return lastCheckedPosition!!
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val markets = allData[position]
        vh.coinName?.text = markets.underlyingName
        vh.ivCoin?.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(markets.underlyingSymbol)))
        vh.radio?.isChecked = position == lastCheckedPosition
        vh.itemView.setOnClickListener {
            val copyOfLastCheckedPosition = lastCheckedPosition
            lastCheckedPosition = position
            notifyItemChanged(copyOfLastCheckedPosition!!)
            notifyItemChanged(lastCheckedPosition!!)
        }
    }

    override fun getItemCount(): Int {
        return allData!!.size
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var coinName: TextView? = view.findViewById(R.id.coin_name)
        var radio: RadioButton? = view.findViewById(R.id.radio)
        var ivCoin: ImageView? = view.findViewById(R.id.iv_coin)
    }
}