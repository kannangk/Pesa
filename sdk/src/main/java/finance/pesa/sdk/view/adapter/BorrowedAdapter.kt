package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.BorrowSelectListener
import finance.pesa.sdk.utils.UserInterface

class BorrowedAdapter(
    context: Context,
    borrowData: ArrayList<MarketData>,
    selectListener: BorrowSelectListener,
    borrowedLimit: Double
) : RecyclerView.Adapter<BorrowedAdapter.MViewHolder>() {

    private var mSelectListener: BorrowSelectListener = selectListener
    var borrowData: List<MarketData>
    var context: Context? = null
    var borrowedLimit: Double? = null

    init {
        this.context = context
        this.borrowData = borrowData
        this.borrowedLimit = borrowedLimit
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.borrowed_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val borrowValue = borrowData[position]
        vh.marketName!!.text = borrowValue.markets.underlyingName
        vh.interest!!.text =
            UserInterface.round(borrowValue.markets.lifetimeBorrowInterestAccrued) + " " + borrowValue.markets.underlyingSymbol
        vh.borrowedUSD!!.text =
            "$ " + UserInterface.round(borrowValue.markets.totalstoredBorrowBalanceinUSD)
        vh.borrowedToken!!.text =
            UserInterface.round(borrowValue.markets.storedBorrowBalance) + " " + borrowValue.markets.underlyingSymbol
        vh.apyPer!!.text = UserInterface.roundTwo(borrowValue.markets.borrowRate * 100) + "%"
        vh.marketIcon!!.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(borrowValue.markets.underlyingSymbol)))
        try {
            val limitPercentage = (borrowValue.markets.totalstoredBorrowBalanceinUSD /borrowedLimit!!)*100
            vh.perLimit!!.text =UserInterface.roundTwo(limitPercentage)+"%"
                vh.pbLimit!!.progress=limitPercentage.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        vh.itemView.setOnClickListener {
            mSelectListener.onSBorrowItemSelected(borrowValue)
        }
    }

    override fun getItemCount(): Int {
        return borrowData!!.size
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var borrowedUSD: TextView? = view.findViewById(R.id.borrowed_usd)
        var borrowedToken: TextView? = view.findViewById(R.id.borrowed_token)
        var apyPer: TextView? = view.findViewById(R.id.apy_per)
        var interest: TextView? = view.findViewById(R.id.interest)
        var marketName: TextView? = view.findViewById(R.id.market_name)
        var perLimit: TextView? = view.findViewById(R.id.limit_per)
        var pbLimit: ProgressBar? = view.findViewById(R.id.limit_pb)
        var marketIcon: ImageView? = view.findViewById(R.id.market_icon)
    }
}