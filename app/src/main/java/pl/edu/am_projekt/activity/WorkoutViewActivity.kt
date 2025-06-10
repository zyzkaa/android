package pl.edu.am_projekt.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pl.edu.am_projekt.ExerciseViewAdapter
import pl.edu.am_projekt.databinding.WorkoutViewLayoutBinding
import pl.edu.am_projekt.model.workout.ExerciseResponse
import pl.edu.am_projekt.model.workout.WorkoutDetailsResponse
import pl.edu.am_projekt.network.ApiService
import pl.edu.am_projekt.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WorkoutViewActivity: AppCompatActivity() {
    private lateinit var binding : WorkoutViewLayoutBinding
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = WorkoutViewLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.materialToolbar3)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val workoutId = intent.getIntExtra("ID", -1)

        apiService = RetrofitClient.retrofit.create(ApiService::class.java)
        val call = apiService.getTrainingDetails(workoutId)
        call.enqueue(object : Callback<WorkoutDetailsResponse> {
            override fun onResponse(
                call: Call<WorkoutDetailsResponse>,
                response: Response<WorkoutDetailsResponse>
            ) {
                if(response.isSuccessful) {
                    val workout : WorkoutDetailsResponse = response.body() ?: return
                    binding.name.text = workout.name
                    binding.date.text = workout.date
                    binding.duration.text = workout.duration
                    binding.totalCalories.text = workout.totalCalories.toString()
                    println(workout)

                    val strengthExercises: List<ExerciseResponse> = workout.strengthExercises ?: emptyList()
                    val cardioExercises: List<ExerciseResponse> = workout.cardioExercises ?: emptyList()
                    val allExercises = strengthExercises + cardioExercises
                    val adapter = ExerciseViewAdapter(allExercises, this@WorkoutViewActivity)
                    binding.exercisesRecyclerView.layoutManager = LinearLayoutManager(this@WorkoutViewActivity)
                    binding.exercisesRecyclerView.adapter = adapter

                } else {
                    Log.d("HTTP", "Kod odpowiedzi: ${response.code()}")
                    Toast.makeText(this@WorkoutViewActivity, "Error occurred", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<WorkoutDetailsResponse>, t: Throwable) {
                Log.e("API_ERROR", "Failure: ${t.message}", t)
                Toast.makeText(this@WorkoutViewActivity, "Connection error", Toast.LENGTH_LONG).show()
            }

        })
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}