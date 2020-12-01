package finance.pesa.sdk.view.Interface

import finance.pesa.sdk.Model.MnemonicData

interface MnemonicSelectListener {
    fun onSelctMnemonic(pos:Int,nemonicData: MnemonicData)
}