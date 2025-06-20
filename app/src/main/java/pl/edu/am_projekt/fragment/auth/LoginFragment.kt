package pl.edu.am_projekt.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.edu.am_projekt.activity.MainActivity
import pl.edu.am_projekt.databinding.LoginFragmentBinding
import pl.edu.am_projekt.model.LoginRequest
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener {
            val username = binding.emailEditText.text?.toString()?.trim().orEmpty()
            val password = binding.passwordEditText.text?.toString()?.trim().orEmpty()

            if (username.isBlank() || password.isBlank()) {
                return@setOnClickListener
            }

            val loginRequest = LoginRequest(username, password)

            lifecycleScope.launch {
                apiService.login(loginRequest)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }

//            apiService.tempLogin().enqueue(object : Callback<Void> {
//                override fun onResponse(call: Call<Void>, response: Response<Void>) {
//                    if (response.isSuccessful) {
//                        val intent = Intent(requireContext(), MainActivity::class.java)
//                        startActivity(intent)
//                        requireActivity().finish()
//                    } else {
//                        Log.d("HTTP", "Kod odpowiedzi: ${response.code()}")
//                        Toast.makeText(requireContext(), "Error occurred", Toast.LENGTH_LONG).show()
//                    }
//                }
//
//                override fun onFailure(call: Call<Void>, t: Throwable) {
//                    Log.e("API_ERROR", "Failure: ${t.message}", t)
//                    Toast.makeText(requireContext(), "Connection error", Toast.LENGTH_LONG).show()
//                }
//            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
