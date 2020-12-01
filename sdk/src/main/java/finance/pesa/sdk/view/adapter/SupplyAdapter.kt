package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.SupplySelectListener
import finance.pesa.sdk.utils.UserInterface

class SupplyAdapter(
    context: Context,
    suppliedData: ArrayList<MarketData>,
    selectListener: SupplySelectListener
) : RecyclerView.Adapter<SupplyAdapter.MViewHolder>() {

    private var mSelectListener: SupplySelectListener = selectListener
    var suppliedData: List<MarketData>
    var context: Context? = null
    var isSwitchTouch = false

    init {
        this.context = context
        this.suppliedData = suppliedData
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.supply_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val supplyData = suppliedData[position]
        vh.marketName!!.text = supplyData.markets.underlyingName
        try {
            if (Constants.getMarketValues()!!.containsKey(supplyData.markets.underlyingSymbol)) {
                vh.walletBalanceShimmer!!.stopShimmerAnimation()
                vh.walletBalanceShimmer!!.visibility = View.GONE
                vh.walletBalance!!.visibility = View.VISIBLE
                val walletBalance =
                    Constants.getMarketValues()!![supplyData.markets.underlyingSymbol]!!.walletBalance
                vh.walletBalance!!.text =
                    UserInterface.round( walletBalance.toDouble() ) +" "+ supplyData.markets.underlyingSymbol
            } else {
                vh.walletBalanceShimmer!!.visibility = View.VISIBLE
                vh.walletBalance!!.visibility = View.GONE
                vh.walletBalanceShimmer!!.startShimmerAnimation()
            }
        } catch (e: Exception) {
            vh.walletBalance!!.text = "$0.0000"
            e.printStackTrace()
        }
        vh.apyPer!!.text = UserInterface.roundTwo(supplyData.markets.supplyRate * 100) + "%"
        vh.switchCollateral!!.isChecked = supplyData.markets.enteredMarket
        vh.marketIcon!!.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(supplyData.markets.underlyingSymbol)))
        vh.switchCollateral!!.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && isSwitchTouch) {
                isSwitchTouch = false
                mSelectListener.collateralClick(isChecked, vh.switchCollateral!!, supplyData)
            } else if (isSwitchTouch) {
                isSwitchTouch = false
                mSelectListener.collateralClick(isChecked, vh.switchCollateral!!, supplyData)
            }
        }
        vh.switchCollateral!!.setOnTouchListener(View.OnTouchListener { v, event ->
            isSwitchTouch = true
            false
        })
        /*if (supplyData.isSupply)
            vh.supplyLay!!.setBackgroundColor(context!!.getColor(R.color.supply_selector))
        else
            vh.supplyLay!!.setBackgroundColor(context!!.getColor(R.color.white_black))*/

        vh.itemView.setOnClickListener {
            mSelectListener.onSupplyItemSelected(supplyData)
        }
    }

    override fun getItemCount(): Int {
        return suppliedData!!.size
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var walletBalance: TextView? = view.findViewById(R.id.wallet_balance)
        var apyPer: TextView? = view.findViewById(R.id.apy_per)
        var marketName: TextView? = view.findViewById(R.id.market_name)
        var switchCollateral: Switch? = view.findViewById(R.id.switch_collateral)
        var marketIcon: ImageView? = view.findViewById(R.id.market_icon)
        var supplyLay: LinearLayout? = view.findViewById(R.id.supply_lay)
        var walletBalanceShimmer: ShimmerFrameLayout? =
            view.findViewById(R.id.shimmer_wallet_balance)
    }
}