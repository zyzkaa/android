package pl.edu.am_projekt.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R

abstract class TimerService : Service() {
    protected var seconds = 0
    private var isRunning = true
    open val notificationTitle = "Timer"
    open val notificationText = ""
    open val iconPath = R.drawable.ic_launcher_foreground

    override fun onCreate() {
        super.onCreate()
        startForeground(1, createNotification())
        startTimer()
    }

    private fun createNotification() : Notification{
        val channelId = "Workout"
        val channel = NotificationChannel(
            channelId,
            "Workout",
            NotificationManager.IMPORTANCE_LOW
        )

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setSmallIcon(iconPath)
            .build()
    }

    private fun startTimer(){
        Log.d(TAG, "timer started")
        CoroutineScope(Dispatchers.Default).launch {
            while(isRunning) {
                delay(1000)
                seconds++
                onTick(seconds)
            }
        }
    }

    abstract fun onTick(seconds: Int)

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}