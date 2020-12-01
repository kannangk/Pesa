package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface

class CoinSpinnerAdapter(context: Context, markets: List<InvestData>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return markets[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return markets.size
    }

    fun getMarket(pos: Int): InvestData? {
        return if(markets.isNotEmpty())
            markets[pos]
        else
            null
    }

    private val mInflator: LayoutInflater = LayoutInflater.from(context)
    private var markets: List<InvestData>
    private var mContext: Context

    init {
        this.markets = markets
        this.mContext = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val vh: ListRowHolder
        if (convertView == null) {
            view = mInflator.inflate(R.layout.coin_spinner_row, parent, false)
            vh = ListRowHolder(view)
            view.tag = vh
        } else {
            view = convertView
            vh = view.tag as ListRowHolder
        }

        vh.label.text = markets[position].underlyingSymbol
        vh.img.setImageDrawable(mContext.getDrawable(UserInterface.getCoinIcon(markets[position].underlyingSymbol)))

        return view
    }

    fun update(it: List<InvestData>?) {
        this.markets = it!!
        notifyDataSetChanged()
    }
}

class ListRowHolder(row: View?) {
     val label: TextView = row?.findViewById(R.id.coin_name) as TextView
     val img: ImageView = row?.findViewById(R.id.iv_coin) as ImageView

}