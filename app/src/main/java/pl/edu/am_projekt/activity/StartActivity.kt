package pl.edu.am_projekt.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    }
}
