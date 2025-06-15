package pl.edu.am_projekt

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//
//        remoteMessage.notification?.let {
//            showNotification(it.title, it.body)
//        }
//    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Odebrano wiadomość")

        val title = remoteMessage.notification?.title ?: "Brak tytułu"
        val body = remoteMessage.notification?.body ?: "Brak treści"

        Log.d("FCM", "Tytuł: $title, Treść: $body")

        showNotification(title, body)
    }


    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    private fun showNotification(title: String?, message: String?) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "default_channel"

        val channel = NotificationChannel(
            channelId,
            "Notifications",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        var resId = resources.getIdentifier(title, "string", packageName)
        val titleString = if (resId != 0) getString(resId) else title

        resId = resources.getIdentifier(message, "string", packageName)
        val bodyString = if (resId != 0) getString(resId) else message

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(titleString)
            .setContentText(bodyString)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }

}
