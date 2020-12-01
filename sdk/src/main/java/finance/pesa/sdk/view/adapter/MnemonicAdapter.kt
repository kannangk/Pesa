package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.MnemonicData
import finance.pesa.sdk.R

class MnemonicAdapter(
    context: Context,
    mnemonicDatas: ArrayList<MnemonicData>
) : RecyclerView.Adapter<MnemonicAdapter.MViewHolder>() {

    var mnemonicDatas: ArrayList<MnemonicData>
    var context: Context? = null

    init {
        this.context = context
        this.mnemonicDatas = mnemonicDatas
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mnemonic_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val mnemonicWord = mnemonicDatas[position]
        if (mnemonicWord.isSelected) {
            vh.word?.text = mnemonicWord.updatedWord
            vh.word?.setTextColor(context!!.getColor(R.color.app_green))
        } else {
            vh.word?.text = (position + 1).toString()
            vh.word?.setTextColor(context!!.getColor(R.color.light_gray_white))
        }
    }

    override fun getItemCount(): Int {
        return mnemonicDatas!!.size
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var word: TextView? = view.findViewById(R.id.iv_name)
    }
}