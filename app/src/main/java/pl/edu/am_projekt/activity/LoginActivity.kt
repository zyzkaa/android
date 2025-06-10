package pl.edu.am_projekt.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.edu.am_projekt.databinding.LoginLayoutBinding
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : LoginLayoutBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        apiService = RetrofitClient.retrofit.create(ApiService::class.java)

        setSupportActionBar(binding.materialToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.buttonLogin.setOnClickListener {
            val call = apiService.tempLogin()
            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if(response.isSuccessful){
                        val intent = Intent(this@LoginActivity, StartActivity::class.java)
                        startActivity(intent)
                    } else {
                        Log.d("HTTP", "Kod odpowiedzi: ${response.code()}")
                        Toast.makeText(this@LoginActivity, "Error occurred", Toast.LENGTH_LONG).show()                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Log.e("API_ERROR", "Failure: ${t.message}", t)
                    Toast.makeText(this@LoginActivity, "Connection error", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish();
        return true;
    }
}