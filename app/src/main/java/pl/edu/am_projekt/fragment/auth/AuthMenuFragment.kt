package pl.edu.am_projekt.fragment.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.edu.am_projekt.R
import pl.edu.am_projekt.databinding.AuthMenuFragmentBinding

class AuthMenuFragment : Fragment() {

    private var _binding: AuthMenuFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AuthMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogin.setOnClickListener{
            findNavController().navigate(R.id.action_authMenu_to_login)
        }

        binding.buttonRegister.setOnClickListener {
            findNavController().navigate(R.id.action_authMenu_to_register)
        }
    }
}