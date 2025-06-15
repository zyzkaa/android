package pl.edu.am_projekt.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import pl.edu.am_projekt.databinding.StartLayoutBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: StartLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = StartLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.trainingsButton.setOnClickListener{
            val intent = Intent(this@StartActivity, WorkoutListActivity::class.java)
            startActivity(intent)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }


        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d("FCM", "Token: $token")
            }
        }

        val channel = NotificationChannel(
            "default_channel",
            "Default Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)

        FirebaseMessaging.getInstance().subscribeToTopic("all")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FCM", "Subskrypcja do tematu 'all' OK")
                } else {
                    Log.e("FCM", "Błąd subskrypcji", task.exception)
                }
            }


    }
}
