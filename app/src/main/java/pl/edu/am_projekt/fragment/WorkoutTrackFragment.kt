package pl.edu.am_projekt.fragment

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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.am_projekt.R
import pl.edu.am_projekt.TimerViewModel
import pl.edu.am_projekt.WorkoutTrackAdapter
import pl.edu.am_projekt.activity.WorkoutViewActivity
import pl.edu.am_projekt.databinding.WorkoutTrackFragmentBinding
import pl.edu.am_projekt.model.workout.request.CardioExercise
import pl.edu.am_projekt.model.workout.request.CardioParams
import pl.edu.am_projekt.model.workout.request.StrengthExercise
import pl.edu.am_projekt.model.workout.request.StrengthParams
import pl.edu.am_projekt.model.workout.request.WorkoutRequest
import pl.edu.am_projekt.model.workout.response.WorkoutDetailsResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import pl.edu.am_projekt.room.Exercise
import pl.edu.am_projekt.room.ExerciseType
import pl.edu.am_projekt.service.WorkoutTimerService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WorkoutTrackFragment : Fragment() {
    private lateinit var adapter: WorkoutTrackAdapter
    private lateinit var apiService : ApiService
    private lateinit var _binding : WorkoutTrackFragmentBinding
    private val binding get() = _binding
    private var time: String = "00:00"
    private lateinit var timerIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkoutTrackFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
    }

    private fun initializeTimer(){
        binding.time.text = "00:00"
        timerIntent = Intent(requireContext(), WorkoutTimerService::class.java)
        ContextCompat.startForegroundService(requireContext(), timerIntent)

        TimerViewModel.time.observe(viewLifecycleOwner) { newTime ->
            time = newTime
//            Log.d(TAG, "live data time: " + time)
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

        binding.saveButton.setOnClickListener{
            val workout = mapDataToNewWorkout()
            Log.d("new Workout", workout.toString())
            viewLifecycleOwner.lifecycleScope.launch {
                val response = postNewWorkout(workout)
                val intent = Intent(context, WorkoutViewActivity::class.java)
                intent.putExtra("ID", response.id)
                requireContext().startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    private suspend fun postNewWorkout(workout: WorkoutRequest) : WorkoutDetailsResponse{
        return apiService.postWorkout(workout)
    }


    private fun mapDataToNewWorkout() : WorkoutRequest {
        val date = Date()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.format(date).toString()

        val inputText = binding.textInputLayout.editText?.text?.toString()
        val name =  if (inputText!!.isEmpty()) "$formattedDate Workout" else inputText

        val duration = if (time.length < 8) "00:$time" else time

        Log.d("new workout", formattedDate + " " + name + " " + duration)

        val exercisesData = adapter.getExercises()
        val strExercises = mutableListOf<StrengthExercise>()
        val carExercises = mutableListOf<CardioExercise>()

        exercisesData.forEach { exerciseData ->
            val exercise = exerciseData.exercise
            if(exercise.muscles == null){
                val params = mutableListOf<CardioParams>()

                exerciseData.data.forEachIndexed  { idx, param ->
                    var minutes = param.second
                    var hours = minutes / 60
                    minutes %= 60
                    var hoursString = if (hours.toString().length >= 2) "$hours" else "0$hours"
                    var minutesString = if (minutes.toString().length >= 2) "$minutes" else "0$minutes"
                    var durationString = "$hoursString:$minutesString:00"

                    params.add(CardioParams(idx + 1, param.first, durationString))
                }

                val newCardioExercise = CardioExercise(exercise.id, params)
                carExercises.add(newCardioExercise)

            } else {
                val params = mutableListOf<StrengthParams>()

                exerciseData.data.forEachIndexed  { idx, param ->
                    params.add(StrengthParams(idx + 1, param.first, param.second))
                }

                val newStrengthExercise = StrengthExercise(exercise.id, params)
                strExercises.add(newStrengthExercise)
            }
        }

        val newWorkout = WorkoutRequest(name, formattedDate, duration, strExercises, carExercises)
        return newWorkout
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

//    private val timerReciever = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            Log.d(TAG, "Broadcast received!")
//            val newTime = intent?.getStringExtra("time") ?: return
//            binding.time.text = newTime
//            time = newTime
//        }
//    }

    private fun initializeRecyclerView(){
        adapter = WorkoutTrackAdapter(mutableListOf())

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                adapter.removeData(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter("WORKOUT_TIMER_UPDATE")
//        requireContext().registerReceiver(timerReciever, filter, RECEIVER_NOT_EXPORTED)
    }

    override fun onStop() {
        super.onStop()
//        requireContext().unregisterReceiver(timerReciever)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireContext().stopService(timerIntent)
    }

}