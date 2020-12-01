package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import finance.pesa.sdk.Model.InvestData
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface


class RequestMoneySuccessFragment : Fragment() {

    private var usdValue: Double? = null
    private var coinValue: Double? = 0.00
    private var notes: String? = null
    private var toAddress: String? = null
    private var marketData: InvestData? = null
    private var tvUSD: TextView? = null
    private var tvCoin: TextView? = null
    private var tvSendTo: TextView? = null
    private var tvNote: TextView? = null
    private var ivCoin: ImageView? = null
    private var tvCoinName: TextView? = null
    private var btnDone: Button? = null
    private var noteView: LinearLayout? = null

    companion object {
        fun newInstance(
            usdValue: Double,
            coinValue: Double,
            notes: String,
            toAddress: String,
            marketData: InvestData
        ): RequestMoneySuccessFragment {
            val fragment = RequestMoneySuccessFragment()
            fragment.usdValue = usdValue
            fragment.coinValue = coinValue
            fragment.notes = notes
            fragment.toAddress = toAddress
            fragment.marketData = marketData
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserInterface.activitiesRefresh()
        UserInterface.changeStatusBar(activity!!, R.color.app_green)
        UserInterface.changeNavigationBar(activity!!, R.color.app_green)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.request_success, null)
        tvUSD = view.findViewById(R.id.tv_usd)
        tvCoin = view.findViewById(R.id.tv_coin)
        tvSendTo = view.findViewById(R.id.tv_send_to)
        tvNote = view.findViewById(R.id.tv_note)
        ivCoin = view.findViewById(R.id.iv_coin)
        tvCoinName = view.findViewById(R.id.tv_coin_name)
        btnDone = view.findViewById(R.id.btn_done)
        noteView = view.findViewById(R.id.note_view)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        btnDone?.setOnClickListener { activity!!.onBackPressed() }
        onUpdateUI()
        return view
    }

    //update UI
    private fun onUpdateUI() {
        try {
            tvUSD?.text = "$" + UserInterface.roundTwo(usdValue!!)
            tvCoin?.text = UserInterface.round(coinValue!!) + " " + marketData?.underlyingSymbol
            tvSendTo?.text = toAddress
            tvNote?.text = notes
            tvCoinName?.text = marketData!!.underlyingName
            ivCoin?.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(marketData!!.underlyingSymbol)))
            if (notes.isNullOrEmpty())
                noteView?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}