package pl.edu.am_projekt.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient


class MyFirebaseMessagingService : FirebaseMessagingService() {

//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//
//        remoteMessage.notification?.let {
//            showNotification(it.title, it.body)
//        }
//    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Odebrano wiadomość")

        val allowedKeys = setOf(
            "weekly_reminder_title",
            "weekly_reminder_body",
            "workout_reminder_title",
            "workout_reminder_body"
        )

        val titleKey = remoteMessage.notification?.title.orEmpty()
        val bodyKey = remoteMessage.notification?.body.orEmpty()

        val title = if (titleKey in allowedKeys) getStringByName(titleKey) else "Brak tytułu"
        val body = if (bodyKey in allowedKeys) getStringByName(bodyKey) else "Brak treści"

        Log.d("FCM", "Tytuł: $title, Treść: $body")

        showNotification(title, body)
    }

    private fun getStringByName(name: String): String {
        val resId = applicationContext.resources.getIdentifier(name, "string", applicationContext.packageName)
        return if (resId != 0) applicationContext.getString(resId) else name
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
