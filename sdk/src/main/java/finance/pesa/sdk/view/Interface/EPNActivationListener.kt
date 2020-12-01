package finance.pesa.sdk.view.Interface

interface EPNActivationListener {
    fun onEPNRegistered(phoneNumber:String,isClaimUser:Boolean,msg:String)
}