package finance.pesa.sdk.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import finance.pesa.sdk.Model.MnemonicData
import finance.pesa.sdk.R
import finance.pesa.sdk.view.Interface.MnemonicSelectListener

class ShuffleMnemonicAdapter(
    context: Context,
    mnemonicDatas: ArrayList<MnemonicData>,
    mnemonicSelectListener: MnemonicSelectListener
) : RecyclerView.Adapter<ShuffleMnemonicAdapter.MViewHolder>() {

     var mnemonicDatas: List<MnemonicData>
    private var context: Context? = null
    private var mnemonicSelectListener: MnemonicSelectListener? = null

    init {
        this.context = context
        this.mnemonicDatas = mnemonicDatas
        this.mnemonicSelectListener = mnemonicSelectListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.mnemonic_shuffle_row, parent, false)
        return MViewHolder(view)
    }


    override fun onBindViewHolder(vh: MViewHolder, position: Int) {
        val mnemonicWord = mnemonicDatas[position]
        if (mnemonicWord.isSelected) {
            vh.word?.visibility = View.INVISIBLE
        } else {
            vh.word?.visibility = View.VISIBLE
            vh.word?.text = mnemonicWord.word
        }

        vh.word?.setOnClickListener {
            mnemonicSelectListener?.onSelctMnemonic(position, mnemonicWord)
        }
    }

    fun onMnemonicUpdate(position: Int,isSelected:Boolean) {
        this.mnemonicDatas[position].isSelected = isSelected
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mnemonicDatas!!.size
    }

    class MViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var word: TextView? = view.findViewById(R.id.iv_name)
    }
}