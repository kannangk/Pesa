package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.BorrowSelectListener
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface

class BorrowAdapter(
    context: Context,
    borrowData: ArrayList<MarketData>,
    selectListener: BorrowSelectListener
) : RecyclerView.Adapter<BorrowAdapter.MViewHolder>() {

    private var mSelectListener: BorrowSelectListener = selectListener
    var borrowData: List<MarketData>
    var context: Context? = null

    init {
        this.context = context
        this.borrowData = borrowData
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.borrow_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val borrowValue = borrowData[position]
        vh.marketName!!.text = borrowValue.markets.underlyingName
        try {
            if (Constants.getMarketValues()!!.containsKey(borrowValue.markets.underlyingSymbol)) {
                vh.walletBalanceShimmer!!.stopShimmerAnimation()
                vh.walletBalanceShimmer!!.visibility = View.GONE
                vh.walletBalance!!.visibility = View.VISIBLE
                val walletBalance =
                    Constants.getMarketValues()!![borrowValue.markets.underlyingSymbol]!!.walletBalance
                vh.walletBalance!!.text =
                   UserInterface.round( walletBalance.toDouble() ) +" "+ borrowValue.markets.underlyingSymbol
            } else {
                vh.walletBalanceShimmer!!.visibility = View.VISIBLE
                vh.walletBalance!!.visibility = View.GONE
                vh.walletBalanceShimmer!!.startShimmerAnimation()
            }
        } catch (e: Exception) {
            vh.walletBalance!!.text = "$0.0000"
            e.printStackTrace()
        }
        vh.apyPer!!.text = UserInterface.roundTwo(borrowValue.markets.borrowRate * 100) + "%"
        vh.liquidator!!.text =
            "$" + UserInterface.getFormatedNumber((borrowValue.markets.cash * borrowValue.markets.underlyingPriceUSD))
        vh.marketIcon!!.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(borrowValue.markets.underlyingSymbol)))
        vh.itemView.setOnClickListener {
            mSelectListener.onSBorrowItemSelected(borrowValue)
        }
    }

    override fun getItemCount(): Int {
        return borrowData!!.size
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var walletBalance: TextView? = view.findViewById(R.id.wallet_balance)
        var apyPer: TextView? = view.findViewById(R.id.apy_per)
        var marketName: TextView? = view.findViewById(R.id.market_name)
        var liquidator: TextView? = view.findViewById(R.id.liquidator)
        var marketIcon: ImageView? = view.findViewById(R.id.market_icon)
        var borrowView: LinearLayout? = view.findViewById(R.id.borrow_view)
        var walletBalanceShimmer: ShimmerFrameLayout? =
            view.findViewById(R.id.shimmer_wallet_balance)
    }
}