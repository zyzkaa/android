package pl.edu.am_projekt.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.am_projekt.databinding.WorkoutLayoutBinding

class WorkoutActivity : AppCompatActivity() {

    private lateinit var binding: WorkoutLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WorkoutLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}