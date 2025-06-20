package pl.edu.am_projekt.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.am_projekt.LanguageManager
import pl.edu.am_projekt.databinding.WorkoutLayoutBinding

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: WorkoutLayoutBinding

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext = newBase?.let { LanguageManager.applyLanguage(it) }
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WorkoutLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}