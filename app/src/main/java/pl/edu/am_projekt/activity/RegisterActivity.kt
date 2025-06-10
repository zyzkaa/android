package pl.edu.am_projekt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.edu.am_projekt.databinding.RegisterLayoutBinding
import pl.edu.am_projekt.model.RegisterRequest
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(){
    private lateinit var binding : RegisterLayoutBinding
    private lateinit var apiService : ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        binding.confrimButton.setOnClickListener {
            val email = binding.emailInput.editText?.text.toString().trim()
            val password = binding.passwordInput.editText?.text.toString().trim()
            val weight = binding.wieghtInput.editText?.text.toString().toIntOrNull()
            val height = binding.heightInput.editText?.text.toString().toIntOrNull()
            val age = binding.birthdateInput.editText?.text.toString().toIntOrNull()

            if (email.isEmpty() || password.isEmpty() || weight == null ||
                height == null || age == null) {
                Toast.makeText(this, "Provide all inputs", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val call = apiService.registerUser(RegisterRequest(email, password, weight, height, age))
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        val intent = Intent(this@RegisterActivity, StartActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("HTTP", "Kod odpowiedzi: ${response.code()}")
                        Toast.makeText(this@RegisterActivity, "Error occurred", Toast.LENGTH_LONG).show()                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API_ERROR", "Failure: ${t.message}", t)
                    Toast.makeText(this@RegisterActivity, "Connection error", Toast.LENGTH_LONG).show()                }
            })
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}