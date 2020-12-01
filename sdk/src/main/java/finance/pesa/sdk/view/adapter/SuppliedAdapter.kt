package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.MarketData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.SupplySelectListener
import finance.pesa.sdk.utils.UserInterface

class SuppliedAdapter(
    context: Context,
    suppliedData: ArrayList<MarketData>,
    selectListener: SupplySelectListener
) : RecyclerView.Adapter<SuppliedAdapter.MViewHolder>() {

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
            .inflate(R.layout.supplied_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val supplyData = suppliedData[position]
        vh.marketName!!.text = supplyData.markets.underlyingName
        vh.interest!!.text =
            UserInterface.round(supplyData.markets.lifetimeSupplyInterestAccrued) + " " + supplyData.markets.underlyingSymbol
        vh.suppliedUSD!!.text =
            "$ " + UserInterface.round(supplyData.markets.totalUnderlyingSuppliedinUSD)
        vh.suppliedToken!!.text =
            UserInterface.round(supplyData.markets.totalUnderlyingSupplied) + " " + supplyData.markets.underlyingSymbol
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

        vh.itemView.setOnClickListener {
            mSelectListener.onSupplyItemSelected(supplyData)
        }
    }

    override fun getItemCount(): Int {
        return suppliedData!!.size
    }


    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var suppliedUSD: TextView? = view.findViewById(R.id.supplied_usd)
        var suppliedToken: TextView? = view.findViewById(R.id.supplied_token)
        var apyPer: TextView? = view.findViewById(R.id.apy_per)
        var interest: TextView? = view.findViewById(R.id.interest)
        var marketName: TextView? = view.findViewById(R.id.market_name)
        var switchCollateral: Switch? = view.findViewById(R.id.switch_collateral)
        var marketIcon: ImageView? = view.findViewById(R.id.market_icon)

    }
}