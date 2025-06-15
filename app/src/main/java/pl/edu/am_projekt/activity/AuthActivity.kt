package pl.edu.am_projekt.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.edu.am_projekt.databinding.AuthLayoutBinding
import pl.edu.am_projekt.databinding.AuthStartLayoutBinding

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: AuthLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = AuthLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}