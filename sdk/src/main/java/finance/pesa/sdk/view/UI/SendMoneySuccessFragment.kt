package finance.pesa.sdk.view.UI

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
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


class SendMoneySuccessFragment : Fragment() {

    private var usdValue: Double? = null
    private var coinValue: Double? = 0.00
    private var notes: String? = null
    private var toAddress: String? = null
    private var transactionHash: String? = null
    private var marketData: InvestData? = null
    private var tvUSD: TextView? = null
    private var tvCoin: TextView? = null
    private var tvSendTo: TextView? = null
    private var tvNote: TextView? = null
    private var ivCoin: ImageView? = null
    private var tvCoinName: TextView? = null
    private var tvTransactionLink: TextView? = null
    private var errorMessage: TextView? = null
    private var tvGasFee: TextView? = null
    private var btnDone: Button? = null
    private var btnInviteFriend: Button? = null
    private var warningLay: LinearLayout? = null
    private var noteView: LinearLayout? = null
    private var isEscrow: Boolean? = true
    private var gasFee: Double? = 0.00

    companion object {
        fun newInstance(
            usdValue: Double,
            coinValue: Double,
            gasFee: Double,
            notes: String,
            toAddress: String,
            transactionHash: String,
            isEscrow: Boolean,
            marketData: InvestData
        ): SendMoneySuccessFragment {
            val sendMoneySuccessFragment = SendMoneySuccessFragment()
            sendMoneySuccessFragment.usdValue = usdValue
            sendMoneySuccessFragment.coinValue = coinValue
            sendMoneySuccessFragment.notes = notes
            sendMoneySuccessFragment.toAddress = toAddress
            sendMoneySuccessFragment.transactionHash = transactionHash
            sendMoneySuccessFragment.marketData = marketData
            sendMoneySuccessFragment.isEscrow = isEscrow
            sendMoneySuccessFragment.gasFee = gasFee
            return sendMoneySuccessFragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        UserInterface.changeStatusBar(activity!!, R.color.app_green)
        UserInterface.changeNavigationBar(activity!!, R.color.app_green)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = View.inflate(context, R.layout.send_success, null)
        tvUSD = view.findViewById(R.id.tv_usd)
        tvCoin = view.findViewById(R.id.tv_coin)
        tvSendTo = view.findViewById(R.id.tv_send_to)
        tvNote = view.findViewById(R.id.tv_note)
        ivCoin = view.findViewById(R.id.iv_coin)
        noteView = view.findViewById(R.id.note_view)
        tvGasFee = view.findViewById(R.id.tv_gas_fee)
        warningLay = view.findViewById(R.id.warning_lay)
        errorMessage = view.findViewById(R.id.error_message)
        tvCoinName = view.findViewById(R.id.tv_coin_name)
        tvTransactionLink = view.findViewById(R.id.tv_transaction_link)
        btnDone = view.findViewById(R.id.btn_done)
        btnInviteFriend = view.findViewById(R.id.btn_invite)
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        UserInterface.changeNavigationBar(activity!!, R.color.white_black)
        btnDone?.setOnClickListener { activity!!.onBackPressed() }
        tvTransactionLink?.setOnClickListener {
            val intent = Intent(context, TransactionWebViewFragment::class.java)
            intent.putExtra("transactionHash", transactionHash)
            startActivity(intent)
        }
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
            tvGasFee?.text = "GAS fee: " + UserInterface.round(gasFee!!) + " ETH"
            ivCoin?.setImageDrawable(context!!.getDrawable(UserInterface.getCoinIcon(marketData!!.underlyingSymbol)))
            if (notes.isNullOrEmpty())
                noteView?.visibility = View.GONE
            try {
                if (isEscrow!!) {
                    warningLay?.visibility = View.VISIBLE
                    btnInviteFriend?.visibility = View.VISIBLE
                    tvTransactionLink?.visibility = View.GONE
                    setTextViewHTML(
                        errorMessage!!,
                        "This number is not registered, the amount will be in escrow for 7 days. You can reclaim the amount from escrow if it is not claimed by the receiver within 7 days. <a href=learn_more>Learn more.</a>"
                    )
                } else {
                    warningLay?.visibility = View.GONE
                    tvTransactionLink?.visibility = View.VISIBLE
                    btnInviteFriend?.visibility = View.GONE
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setTextViewHTML(text: TextView, html: String) {
        val sequence = Html.fromHtml(html)
        val strBuilder = SpannableStringBuilder(sequence)
        val urls = strBuilder.getSpans(0, sequence.length, URLSpan::class.java)
        for (span in urls) {
            makeLinkClickable(strBuilder, span)
        }
        text.text = strBuilder
        text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun makeLinkClickable(strBuilder: SpannableStringBuilder, span: URLSpan) {
        val start = strBuilder.getSpanStart(span)
        val end = strBuilder.getSpanEnd(span)
        val flags = strBuilder.getSpanFlags(span)
        val clickable = object : ClickableSpan() {
            override fun onClick(view: View) {
                try {
                    if (span.url == "learn_more") {

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        strBuilder.setSpan(clickable, start, end, flags)
        strBuilder.removeSpan(span)
    }


}