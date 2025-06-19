package pl.edu.am_projekt.fragment.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.edu.am_projekt.activity.MainActivity
import pl.edu.am_projekt.databinding.RegisterFragmentBinding
import pl.edu.am_projekt.model.RegisterRequest
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class RegisterFragment : Fragment() {

    private var _binding: RegisterFragmentBinding? = null
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
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmButton.setOnClickListener {
            val email = binding.emailInput.editText?.text.toString().trim()
            val password = binding.passwordInput.editText?.text.toString().trim()
            val weight = binding.weightInput.editText?.text.toString().toIntOrNull() ?: 0
            val height = binding.heightInput.editText?.text.toString().toIntOrNull() ?: 0
            val age = binding.ageInput.editText?.text.toString().toIntOrNull() ?: 0
            val calories = binding.deficitInput.editText?.text.toString().toIntOrNull() ?: 0

            val registerRequest = RegisterRequest(
                username = email,
                password = password,
                weight = weight,
                height = height,
                age = age,
                calories = calories
            )

            lifecycleScope.launch {
                apiService.registerUser(registerRequest)
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}