package pl.edu.am_projekt.fragment.workout

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.edu.am_projekt.TimerViewModel
import pl.edu.am_projekt.adapter.WorkoutTrackAdapter
import pl.edu.am_projekt.databinding.WorkoutTrackFragmentBinding
import pl.edu.am_projekt.model.Exercise
import pl.edu.am_projekt.model.ExerciseType
import pl.edu.am_projekt.model.ExerciseWithTrackData
import pl.edu.am_projekt.model.InputData
import pl.edu.am_projekt.model.workout.request.CardioExercise
import pl.edu.am_projekt.model.workout.request.CardioParams
import pl.edu.am_projekt.model.workout.request.StrengthExercise
import pl.edu.am_projekt.model.workout.request.StrengthParams
import pl.edu.am_projekt.model.workout.request.WorkoutRequest
import pl.edu.am_projekt.model.workout.response.WorkoutDetailsResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
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
    private val exerciseList = mutableListOf<ExerciseWithTrackData>()

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
            binding.time.text = time
        }
    }

    private fun initializeButtons(){
        binding.addStrExerciseButton.setOnClickListener {
            val action = WorkoutTrackFragmentDirections
                .actionWorkoutTrackToChooseExercise(ExerciseType.STRENGTH.name)

            findNavController().navigate(action)
        }

        binding.addCarExerciseButton.setOnClickListener {
            val action = WorkoutTrackFragmentDirections
                .actionWorkoutTrackToChooseExercise(ExerciseType.CARDIO.name)

            findNavController().navigate(action)
        }

        binding.saveButton.setOnClickListener{
            val workout = mapDataToNewWorkout()
            Log.d("new Workout", workout.toString())
            viewLifecycleOwner.lifecycleScope.launch {
                val response = postNewWorkout(workout)
                val action = WorkoutTrackFragmentDirections
                    .actionWorkoutTrackToWorkoutView(response.id)
                requireContext().stopService(timerIntent)
                findNavController().navigate(action)
//                findNavController().popBackStack(R.id.WorkoutTrackFragment, true)
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

        val strExercises = mutableListOf<StrengthExercise>()
        val carExercises = mutableListOf<CardioExercise>()

        exerciseList.forEach { exerciseData ->
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

            if(checkExerecisesContains(exercise!!)) return@setFragmentResultListener

            val newExercise = ExerciseWithTrackData(exercise, mutableListOf(InputData(0.0, 0)))
            exerciseList.add(newExercise)
        }
    }

    private fun checkExerecisesContains(exercise: Exercise): Boolean {
        exerciseList.forEach {
                exerciseData -> if(exerciseData.exercise == exercise) return true
        }
        return false
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
        adapter = WorkoutTrackAdapter(exerciseList)

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
//        requireContext().stopService(timerIntent)
    }

}