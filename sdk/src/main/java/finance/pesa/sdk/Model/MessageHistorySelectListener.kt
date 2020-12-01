package finance.pesa.sdk.Model

interface MessageHistorySelectListener {
    fun onSelectedMessage(messageDetails: finance.pesa.sdk.Model.ConversationData)
    fun onDeleteMessage(messageDetails: finance.pesa.sdk.Model.ConversationData, position:Int)
    fun onCall(messageDetails: finance.pesa.sdk.Model.ConversationData)
}