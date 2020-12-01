package finance.pesa.sdk.view.UI

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greenfrvr.hashtagview.HashtagView
import finance.pesa.sdk.Model.MnemonicData
import finance.pesa.sdk.PesaApplication
import finance.pesa.sdk.R
import finance.pesa.sdk.utils.UserInterface
import finance.pesa.sdk.view.adapter.MnemonicAdapter
import java.util.*


class MnemonicVerifyFragment : Fragment() {
    private var mobileNumber: String? = null
    private var isNewUser: Boolean? = null
    private var mnemonicWord: String? = null
    private var ivBack: ImageView? = null
    private var ivDelete: ImageView? = null
    private var errorMessage: TextView? = null
    private var countMnemonic: TextView? = null
    private var btnConfirm: Button? = null
    private var mnemonicRecycler: RecyclerView? = null
    private var shuffleMnemonicHashTag: HashtagView? = null
    private var mnemonicData: ArrayList<MnemonicData> = arrayListOf()
    private var shuffleData: ArrayList<MnemonicData> = arrayListOf()
    private var mnemonicAdapter: MnemonicAdapter? = null
    private val units = arrayOf(
        "",
        "first",
        "second",
        "third",
        "fourth",
        "fifth",
        "sixth",
        "seventh",
        "eighth",
        "ninth",
        "tenth",
        "eleventh",
        "twelfth"
    )

    companion object {
        fun newInstance(
            mobileNumber: String,
            mnemonicWord: String,
            isNewUser: Boolean
        ): MnemonicVerifyFragment {
            val fragment = MnemonicVerifyFragment()
            fragment.mobileNumber = mobileNumber
            fragment.mnemonicWord = mnemonicWord
            fragment.isNewUser = isNewUser
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.mnemonic_verify, container, false)
        ivBack = view.findViewById(R.id.iv_back)
        ivDelete = view.findViewById(R.id.iv_delete)
        btnConfirm = view.findViewById(R.id.btn_confirm)
        mnemonicRecycler = view.findViewById(R.id.mnemonic_recycler)
        shuffleMnemonicHashTag = view.findViewById(R.id.hashtag_shuffle_phrase)
        errorMessage = view.findViewById(R.id.error_message)
        countMnemonic = view.findViewById(R.id.count_mnemonic)
        mnemonicData = arrayListOf()
        shuffleData = arrayListOf()
        mnemonicViewUpdate()
        UserInterface.changeStatusBar(activity!!, R.color.white_black)
        ivBack?.setOnClickListener { activity!!.onBackPressed() }
        ivDelete?.setOnClickListener { deleteMnemonic() }
        btnConfirm?.setOnClickListener {
            PesaApplication.getChildFragmentManager()!!.beginTransaction()
                .add(
                    R.id.shareg_main_container,
                    PasswordEnterFragment.newInstance(mobileNumber!!, mnemonicWord!!, isNewUser!!)
                ).addToBackStack("register")
                .commitAllowingStateLoss()
        }
        shuffleMnemonicHashTag?.addOnTagSelectListener(HashtagView.TagsSelectListener { item, selected ->
            try {
                val mnemonicDataValue: MnemonicData = item as MnemonicData
                addMnemonic(mnemonicDataValue)
                ivDelete?.visibility = View.VISIBLE
                checkMnemonic()
                checkHashTagMnemonic(mnemonicDataValue, true)
                try {
                    val addedMnemonicData = mnemonicAdapter?.mnemonicDatas
                    var i = 1
                    if (addedMnemonicData != null) {
                        for (data in addedMnemonicData) {
                            if (data.isSelected) {
                                i++
                            }
                        }
                    }
                   /* if (i == 13) {
                        countMnemonic?.text = ""
                    } else {
                        countMnemonic?.text = "What’s the " + units[i] + " word of your Account Key?"
                    }*/
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        return view
    }

    //onUI update
    private fun mnemonicViewUpdate() {
        val mnemonicSplited = mnemonicWord?.split(" ")
        if (mnemonicSplited != null) {
            for (word in mnemonicSplited) {
                mnemonicData!!.add(MnemonicData(false, word, ""))
                shuffleData!!.add(MnemonicData(false, word, word))
            }
        }
        mnemonicAdapter = MnemonicAdapter(
            activity!!,
            mnemonicData!!
        )
        val smallerLists: ArrayList<MnemonicData>? = arrayListOf()
        smallerLists?.addAll(shuffleData.subList(0, 6))
        smallerLists?.shuffle(Random())
        val manager = GridLayoutManager(context, 4)
        mnemonicRecycler?.layoutManager = manager
        mnemonicRecycler?.adapter = mnemonicAdapter
        if (smallerLists != null) {
            shuffleMnemonicHashTag?.setData(smallerLists, HashtagView.DataTransform {
                it.word
            })
        }
        shuffleMnemonicHashTag?.setRowCount(2)
        shuffleMnemonicHashTag?.invalidate()

    }


    //update mnemonic
    private fun addMnemonic(mnemonicDataValue: MnemonicData) {
        try {
            val addedMnemonicData = mnemonicAdapter?.mnemonicDatas
            if (addedMnemonicData != null) {
                for ((i, data) in addedMnemonicData.withIndex()) {
                    if (data.updatedWord == "") {
                        data.isSelected = true
                        data.updatedWord = mnemonicDataValue.word
                        mnemonicAdapter?.notifyDataSetChanged()
                        return
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //delete added mnemonic
    private fun deleteMnemonic() {
        try {
            val addedMnemonicData = mnemonicAdapter?.mnemonicDatas
            if (addedMnemonicData != null) {
                for (i in addedMnemonicData.size - 1 downTo 0) {
                    if (addedMnemonicData[i].isSelected) {
                        checkHashTagMnemonic(addedMnemonicData[i], false)
                        addedMnemonicData[i].isSelected = false
                        addedMnemonicData[i].updatedWord = ""
                        mnemonicAdapter?.notifyDataSetChanged()
                        break
                    }
                }
                checkIsAddedMnemonic()
                btnConfirm?.isClickable = false
                btnConfirm?.isEnabled = false
                errorMessage?.visibility = View.GONE
                try {
                    var i = 1
                    for (data in addedMnemonicData) {
                        if (data.isSelected) {
                            i++
                        }
                    }
                    /*if (i == 13) {
                        countMnemonic?.text = ""
                    } else {
                        countMnemonic?.text =
                            "What’s the " + units[i] + " word of your Account Key?"
                    }*/
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //check mnemonic added
    private fun checkIsAddedMnemonic() {
        try {
            val addedMnemonicData = mnemonicAdapter?.mnemonicDatas
            if (addedMnemonicData != null) {
                for (data in addedMnemonicData) {
                    if (data.updatedWord != "") {
                        ivDelete?.visibility = View.VISIBLE
                        return
                    }
                }
            }
            ivDelete?.visibility = View.GONE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun checkHashTagMnemonic(mnemonicDataValue: MnemonicData, isAdded: Boolean) {
        try {
            for (data in shuffleData) {
                if (data.word == mnemonicDataValue.updatedWord) {
                    data.isSelected = isAdded
                    break
                }
            }
            var unSelectedMnemonicData: ArrayList<MnemonicData>? = arrayListOf()
            val smallerLists: ArrayList<MnemonicData>? = arrayListOf()
            for (data in shuffleData) {
                if (!data.isSelected) {
                    unSelectedMnemonicData?.add(data)
                }
            }
            if (unSelectedMnemonicData?.size!! > 6) {
                smallerLists?.addAll(unSelectedMnemonicData.subList(0, 6))
                smallerLists?.shuffle(Random())
                if (smallerLists != null) {
                    shuffleMnemonicHashTag?.setData(smallerLists, HashtagView.DataTransform {
                        it.word
                    })
                }
            } else {
                unSelectedMnemonicData?.shuffle(Random())
                if (smallerLists != null) {
                    shuffleMnemonicHashTag?.setData(
                        unSelectedMnemonicData,
                        HashtagView.DataTransform {
                            it.word
                        })
                }
            }
            shuffleMnemonicHashTag?.invalidate()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //check added mnemonic is correct or not
    private fun checkMnemonic() {
        try {
            val addedMnemonicData = mnemonicAdapter?.mnemonicDatas
            if (addedMnemonicData != null) {
                var isAllSelected = true
                var isVerified = true

                for (data in addedMnemonicData) {
                    if (data.updatedWord != data.word) {
                        isVerified = false
                    }
                    if (!data.isSelected) {
                        isAllSelected = false
                    }
                }
                if (isAllSelected && !isVerified) {
                    errorMessage?.visibility = View.VISIBLE
                    UserInterface.errorShow(errorMessage, context)
                }
                if (isAllSelected && isVerified) {
                    btnConfirm?.isClickable = true
                    btnConfirm?.isEnabled = true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}