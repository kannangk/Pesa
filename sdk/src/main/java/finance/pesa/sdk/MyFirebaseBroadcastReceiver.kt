package finance.pesa.sdk

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.google.firebase.messaging.RemoteMessage
import finance.pesa.sdk.utils.Constants
import finance.pesa.sdk.utils.PesaNotificationReceiver
import finance.pesa.sdk.utils.UserInterface

class MyFirebaseBroadcastReceiver : WakefulBroadcastReceiver() {

    val TAG: String = MyFirebaseBroadcastReceiver::class.java.simpleName

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val dataBundle = intent.extras
            /*if (dataBundle != null)
                for (key in dataBundle.keySet()) {
                    Log.d(TAG, "dataBundle: " + key + " : " + dataBundle.get(key))
                }*/
            val remoteMessage = RemoteMessage(dataBundle)
            Log.d(TAG, remoteMessage.data.toString())
            showNotification(
                context,
                remoteMessage.data
            )
            Constants.setIsPendingNotification(true,context)
            UserInterface.updateNotification(true)
            UserInterface.loadWalletDatas(context, true)
            UserInterface.refreshActivities()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun showNotification(
        applicationContext: Context,
        data: Map<String, String>
    ) {
        try {
            val title: String = data["title"].toString()
            val message: String = data["body"].toString()

            var isNavigateToActivity = "false"
            try {
                if (data.containsKey("isNavigateToActivity"))
                    isNavigateToActivity = data["isNavigateToActivity"].toString()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            val NOTIFICATION_ID = (Math.random() * 100).toInt() % 10
            val channelId = "Pesa"
            val connectIntent = Intent(applicationContext, PesaNotificationReceiver::class.java)
            val bundle = Bundle()
            bundle.putInt("notificationId", NOTIFICATION_ID)
            bundle.putString("msg", message)
            bundle.putString(Constants.IS_NAVIGATE_Activity, isNavigateToActivity)
            connectIntent.putExtras(bundle)
            val connectIntentMain =
                PendingIntent.getBroadcast(
                    applicationContext,
                    0,
                    connectIntent,
                    Intent.FILL_IN_DATA
                )
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            val notification = NotificationCompat.Builder(applicationContext, channelId)
            notification.setContentTitle(title)
            notification.setStyle(NotificationCompat.BigTextStyle().bigText(message))
            notification.setContentText(message)
            notification.setAutoCancel(true)
            notification.setSmallIcon(getNotificationIcon(applicationContext, notification))
            notification.setContentIntent(connectIntentMain)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notification.setChannelId(channelId)
                val channel = NotificationChannel(
                    channelId,
                    "Pesa Notification",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationManager!!.createNotificationChannel(channel)
            }

            notificationManager!!.notify(NOTIFICATION_ID, notification.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun getNotificationIcon(
        context: Context,
        notificationBuilder: NotificationCompat.Builder
    ): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.color = context.resources.getColor(R.color.app_green, null)
            return R.drawable.ic_splash_logo
        }
        return R.drawable.ic_splash_logo
    }
}