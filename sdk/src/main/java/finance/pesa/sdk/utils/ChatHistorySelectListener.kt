package finance.pesa.sdk.utils

import finance.pesa.sdk.Model.Conversation

interface ChatHistorySelectListener {
     fun onSelected(conversatfinancen: Conversation, pos: Int)
}