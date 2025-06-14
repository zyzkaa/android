package pl.edu.am_projekt.activity

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import pl.edu.am_projekt.ExerciseViewAdapter
import pl.edu.am_projekt.databinding.WorkoutViewLayoutBinding
import pl.edu.am_projekt.model.workout.response.ExerciseResponse
import pl.edu.am_projekt.model.workout.response.WorkoutDetailsResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient

class WorkoutViewActivity: AppCompatActivity() {
    private lateinit var binding : WorkoutViewLayoutBinding
    private lateinit var apiService: ApiService

    private fun initializeActionBar(){
        setSupportActionBar(binding.materialToolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""
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
        val adapter = ExerciseViewAdapter(allExercises, this@WorkoutViewActivity)
        binding.exercisesRecyclerView.layoutManager = LinearLayoutManager(this@WorkoutViewActivity)
        binding.exercisesRecyclerView.adapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WorkoutViewLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeActionBar()
        displayWorkout()
    }

    private fun displayWorkout(){
        val workoutId = intent.getIntExtra("ID", -1)
        getWorkoutDetails(workoutId)
//        if(workoutId != -1){
//            getWorkoutDetails(workoutId)
//        } else {
//            val workoutData: WorkoutDetailsResponse? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                intent.getParcelableExtra("workout", WorkoutDetailsResponse::class.java)
//            } else {
//                @Suppress("DEPRECATION")
//                intent.getParcelableExtra("workout")
//            }
//            workoutData?.let { setWorkoutData(it) }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}