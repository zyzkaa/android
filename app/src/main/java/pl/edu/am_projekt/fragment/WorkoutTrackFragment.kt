package pl.edu.am_projekt.fragment

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pl.edu.am_projekt.R
import pl.edu.am_projekt.TimerViewModel
import pl.edu.am_projekt.databinding.WorkoutTrackFragementBinding
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.service.WorkoutTimerService

class WorkoutTrackFragment : Fragment() {
    private lateinit var apiService : ApiService
    private lateinit var _binding : WorkoutTrackFragementBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = WorkoutTrackFragementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.time.text = "00:00"

        val intent = Intent(requireContext(), WorkoutTimerService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)

        TimerViewModel.time.observe(viewLifecycleOwner) { time ->
            Log.d(TAG, "live data time: " + time)
            binding.time.text = time
        }

        binding.addExerciseButton.setOnClickListener {

//            val fragment = ChooseExerciseFragment {   }
//
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragmentView, fragment) // R.id.fragment_container to np. FrameLayout
//                .addToBackStack(null)
//                .commit()
        }


    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }

    private val timerReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "Broadcast received!")
            val time = intent?.getStringExtra("time") ?: return
            binding.time.text = time
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("WORKOUT_TIMER_UPDATE")
        requireContext().registerReceiver(timerReciever, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
        requireContext().unregisterReceiver(timerReciever)
    }

}