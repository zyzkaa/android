package pl.edu.am_projekt.fragment.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import pl.edu.am_projekt.R
import pl.edu.am_projekt.activity.WorkoutActivity
import pl.edu.am_projekt.databinding.MainMenuFragmentBinding

class MainMenuFragment : Fragment(){

    private var _binding: MainMenuFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainMenuFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.trainingsButton.setOnClickListener{
            val intent = Intent(requireContext(), WorkoutActivity::class.java)
            startActivity(intent)
        }

        binding.settingsButton.setOnClickListener{
            findNavController().navigate(R.id.action_mainMenu_to_appSettings)
        }


    }
}