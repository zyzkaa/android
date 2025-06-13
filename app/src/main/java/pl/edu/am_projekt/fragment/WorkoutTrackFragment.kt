package pl.edu.am_projekt.fragment

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.am_projekt.ExerciseAdapter
import pl.edu.am_projekt.R
import pl.edu.am_projekt.TimerViewModel
import pl.edu.am_projekt.WorkoutTrackAdapter
import pl.edu.am_projekt.databinding.WorkoutTrackFragmentBinding
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.room.Exercise
import pl.edu.am_projekt.room.ExerciseType
import pl.edu.am_projekt.service.WorkoutTimerService

class WorkoutTrackFragment : Fragment() {
    private lateinit var adapter: WorkoutTrackAdapter
    private lateinit var apiService : ApiService
    private lateinit var _binding : WorkoutTrackFragmentBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkoutTrackFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun initializeTimer(){
        binding.time.text = "00:00"
        val intent = Intent(requireContext(), WorkoutTimerService::class.java)
        ContextCompat.startForegroundService(requireContext(), intent)

        TimerViewModel.time.observe(viewLifecycleOwner) { time ->
            Log.d(TAG, "live data time: " + time)
            binding.time.text = time
        }
    }

    private fun initializeButtons(){
        binding.addStrExerciseButton.setOnClickListener {
            val newFragment = ChooseExerciseFragment.newInstance(ExerciseType.STRENGTH)

            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentView, newFragment)
                .hide(this)
                .addToBackStack(null)
                .commit()
        }

        binding.addCarExerciseButton.setOnClickListener {
            val newFragment = ChooseExerciseFragment.newInstance(ExerciseType.CARDIO)

            parentFragmentManager.beginTransaction()
                .add(R.id.fragmentView, newFragment)
                .hide(this)
                .addToBackStack(null)
                .commit()
        }
    }

    private fun initializeExerciseReciever() {
        parentFragmentManager.setFragmentResultListener("exercise_key", viewLifecycleOwner) { _,
                                                                                              bundle ->
            val exercise = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable("exercise", Exercise::class.java)
            } else {
                @Suppress("DEPRECATION")
                bundle.getParcelable<Exercise>("exercise")
            }
            Log.d("exercise chosen", exercise?.name ?: "")
            adapter.addData(exercise!!);
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeRecyclerView()
        initializeTimer()
        initializeButtons()
        initializeExerciseReciever()

    }

    private val timerReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "Broadcast received!")
            val time = intent?.getStringExtra("time") ?: return
            binding.time.text = time
        }
    }

    private fun initializeRecyclerView(){
        adapter = WorkoutTrackAdapter(mutableListOf())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
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