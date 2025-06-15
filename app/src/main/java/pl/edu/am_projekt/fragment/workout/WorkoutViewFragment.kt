package pl.edu.am_projekt.fragment.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pl.edu.am_projekt.adapter.ExerciseViewAdapter
import pl.edu.am_projekt.databinding.WorkoutViewFragmentBinding
import pl.edu.am_projekt.model.workout.response.ExerciseResponse
import pl.edu.am_projekt.model.workout.response.WorkoutDetailsResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class WorkoutViewFragment : Fragment() {

    private var _binding: WorkoutViewFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private val args: WorkoutViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WorkoutViewFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getWorkoutDetails(args.id)
    }


    private fun getWorkoutDetails(id: Int){
        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        lifecycleScope.launch {
            val workout = apiService.getTrainingDetails(id)
            setWorkoutData(workout)
        }
    }

    private fun setWorkoutData(workout: WorkoutDetailsResponse){
        binding.name.text = workout.name
        binding.date.text = workout.date
        binding.duration.text = workout.duration
        binding.totalCalories.text = workout.totalCalories.toString()
        println(workout)

        val strengthExercises: List<ExerciseResponse> = workout.strengthExercises
        val cardioExercises: List<ExerciseResponse> = workout.cardioExercises
        val allExercises = strengthExercises + cardioExercises
        val adapter = ExerciseViewAdapter(allExercises, requireContext())
        binding.exercisesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.exercisesRecyclerView.adapter = adapter
    }
}