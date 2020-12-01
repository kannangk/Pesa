package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import finance.pesa.sdk.Model.WalletData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.UserInterface


class WalletAdapter(
    context: Context,
    borrowData: ArrayList<WalletData>
) : RecyclerView.Adapter<WalletAdapter.MViewHolder>() {

    var borrowData: List<WalletData>
    var context: Context? = null

    init {
        this.context = context
        this.borrowData = borrowData
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.wallet_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val walletData = borrowData[position]
        vh.marketName!!.text = walletData.markets.underlyingName
        try {
            if (Constants.getMarketValues()!!.containsKey(walletData.markets.underlyingSymbol)) {
                vh.walletBalanceShimmer!!.stopShimmerAnimation()
                vh.walletValueShimmer!!.stopShimmerAnimation()
                vh.walletBalanceShimmer!!.visibility = View.GONE
                vh.walletValueShimmer!!.visibility = View.GONE
                vh.walletBalance!!.visibility = View.VISIBLE
                vh.walletValue!!.visibility = View.VISIBLE
                val walletBalance =
                    Constants.getMarketValues()!![walletData.markets.underlyingSymbol]!!.walletBalance
                vh.walletBalance!!.text =
                    UserInterface.round( walletBalance.toDouble()) + " " + walletData.markets.underlyingSymbol
                vh.walletValue!!.text =
                    "$" + UserInterface.roundTwo(walletBalance.toDouble() * walletData.markets.underlyingPriceUSD)
            } else {
                vh.walletBalanceShimmer!!.visibility = View.VISIBLE
                vh.walletValueShimmer!!.visibility = View.VISIBLE
                vh.walletBalance!!.visibility = View.GONE
                vh.walletValue!!.visibility = View.GONE
                vh.walletBalanceShimmer!!.startShimmerAnimation()
                vh.walletValueShimmer!!.startShimmerAnimation()
            }
        } catch (e: Exception) {
            vh.walletValue!!.text = "$0.0000"
            e.printStackTrace()
        }
        vh.marketIcon!!.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(walletData.markets.underlyingSymbol)))

    }

    override fun getItemCount(): Int {
        return borrowData!!.size
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var walletBalance: TextView? = view.findViewById(R.id.wallet_balance)
        var marketName: TextView? = view.findViewById(R.id.market_name)
        var walletValue: TextView? = view.findViewById(R.id.wallet_value)
        var marketIcon: ImageView? = view.findViewById(R.id.market_icon)
        var walletBalanceShimmer: ShimmerFrameLayout? =
            view.findViewById(R.id.shimmer_wallet_balance)
        var walletValueShimmer: ShimmerFrameLayout? = view.findViewById(R.id.shimmer_wallet_value)
    }
}