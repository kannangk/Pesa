package finance.pesa.sdk

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        storeRegIdInPref(token)
        Log.d("notification_token", token)
    }

    private fun storeRegIdInPref(token: String) {
        Log.d("notification_token_pesa", token)
        val pref = applicationContext.getSharedPreferences("firebase", 0)
        val editor = pref.edit()
        editor.putString("regId", token)
        editor.commit()
    }


    @SuppressLint("WrongThread")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("notification_data", remoteMessage.data.toString())
    }


}