package pl.edu.am_projekt.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.edu.am_projekt.LanguageManager
import pl.edu.am_projekt.databinding.MainLayoutBinding
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainLayoutBinding

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LanguageManager.applyLanguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "FCM token: $token")

            val apiService = RetrofitClient.retrofit.create(ApiService::class.java)

            lifecycleScope.launch {
                try {
                    apiService.setFcmToken(token)
                } catch (e: Exception) {
                    Log.e("FCM", "Token error: ${e.message}")
                }
            }
        }


        binding = MainLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        binding.trainingsButton.setOnClickListener{
//            val intent = Intent(this@MainActivity, WorkoutListActivity::class.java)
//            startActivity(intent)
//        }

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

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getString("pref_theme", "system")) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "system" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

}
